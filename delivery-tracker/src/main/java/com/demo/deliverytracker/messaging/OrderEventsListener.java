package com.demo.deliverytracker.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.demo.deliverytracker.service.DeliveryService;

import jakarta.jms.Message;

@Component
public class OrderEventsListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsListener.class);
    private final DeliveryService deliveryService;

    public OrderEventsListener(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @JmsListener(destination = "queue.orders.new")
    public void onOrderCreated(@Payload OrderCreatedEvent evt, Message jmsMessage) {
        String corrId;
        try {
            corrId = jmsMessage.getJMSCorrelationID();
        } catch (Exception e) {
            corrId = null;
        }
        log.info("JMS EVENT RECEIVED orderId={} item={} qty={} corrId={}", evt.getOrderId(), evt.getItem(), evt.getQuantity(), corrId);
        deliveryService.createFromEvent(evt, corrId);
    }
}
