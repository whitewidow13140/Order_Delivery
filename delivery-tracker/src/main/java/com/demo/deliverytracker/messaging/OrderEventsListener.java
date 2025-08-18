package com.demo.deliverytracker.messaging;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.demo.deliverytracker.events.EventLogService;
import com.demo.deliverytracker.service.DeliveryService;

import com.demo.deliverytracker.metrics.DeliveryMetrics;
import io.micrometer.core.annotation.Timed;

@Component
public class OrderEventsListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsListener.class);
    private final DeliveryService deliveryService;
    private final EventLogService eventLogService;
    private final DeliveryMetrics deliveryMetrics;


    public OrderEventsListener(DeliveryService deliveryService, EventLogService eventLogService, DeliveryMetrics deliveryMetrics) {
        this.deliveryService = deliveryService;
        this.eventLogService = eventLogService;
        this.deliveryMetrics = deliveryMetrics;
    }

    @Timed(value = "dt.jms.consume", extraTags = { "queue", "queue.orders.new" })
    @JmsListener(destination = "queue.orders.new")
    public void onOrderCreated(OrderCreatedEvent evt,
                             @Header(name = JmsHeaders.CORRELATION_ID, required = false) String corrId,
                             @Headers Map<String, Object> headers) {
    String corr = (corrId == null || corrId.isBlank()) ? UUID.randomUUID().toString() : corrId;
    MDC.put("correlationId", corr);
    MDC.put("corrId", corr);
    try {
      eventLogService.record(corr, evt.getOrderId(), "DELIVERY_RECEIVED", evt, headers);

    //   log.info("JMS EVENT RECEIVED orderId={} item={} qty={} corrId={}", evt.getOrderId(), evt.getItem(), evt.getQuantity(), corrId);
      deliveryService.createFromEvent(evt, corr);

      deliveryMetrics.recordE2E(evt.getCreatedAt());

      eventLogService.record(corr, evt.getOrderId(), "DELIVERY_PERSISTED",
          Map.of("status", "PREPARING"));
    } finally {
      MDC.remove("correlationId");
      MDC.remove("corrId");
    }
  }
}
