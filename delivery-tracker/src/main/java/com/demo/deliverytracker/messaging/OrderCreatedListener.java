package com.demo.deliverytracker.messaging;

import com.demo.deliverytracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final DeliveryService deliveryService;

    @JmsListener(destination = "queue.orders.new")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("JMS_EVENT_CONSUMED orderId={} item={} qty={}",
                event.getOrderId(), event.getItem(), event.getQuantity());
        var d = deliveryService.createFromEvent(event);
        log.info("DELIVERY_CREATED id={} forOrderId={}", d.getId(), d.getOrderId());
    }
}
