package com.demo.deliverytracker.messaging;

import com.demo.deliverytracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final DeliveryService deliveryService;

    @JmsListener(destination = "queue.orders.new")
    public void onOrderCreated(@Payload OrderCreatedEvent event,
                               @Header(name = JmsHeaders.CORRELATION_ID, required = false) String jmsCorrId,
                               @Header(name = "X-Correlation-Id", required = false) String propCorrId) {
        // 1) Choisir le meilleur corrId dispo
        String corrId = event.getCorrelationId();
        if (corrId == null || corrId.isBlank()) corrId = propCorrId;
        if (corrId == null || corrId.isBlank()) corrId = jmsCorrId;

        // 2) Mettre dans le MDC pour tous les logs en aval
        if (corrId != null) MDC.put("corrId", corrId);

        try {
            log.info("JMS_EVENT_CONSUMED orderId={} item={} qty={} corrId={}",
                    event.getOrderId(), event.getItem(), event.getQuantity(), corrId);
            var d = deliveryService.createFromEvent(event);
            log.info("DELIVERY_CREATED id={} forOrderId={} corrId={}", d.getId(), d.getOrderId(), corrId);
        } finally {
            MDC.remove("corrId");
        }
    }
}
