package com.demo.deliverytracker.messaging;

import com.demo.deliverytracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class OrderCreatedListener {

    private final DeliveryService deliveryService;

    @JmsListener(destination = "queue.orders.new")
    public void onOrderCreated(OrderCreatedEvent event) {
        deliveryService.createFromEvent(event);
        System.out.println("Received OrderCreatedEvent for order " + event.getOrderId());
    }
}
