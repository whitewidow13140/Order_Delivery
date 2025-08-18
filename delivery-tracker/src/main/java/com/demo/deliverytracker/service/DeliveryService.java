package com.demo.deliverytracker.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.deliverytracker.domain.Delivery;
import com.demo.deliverytracker.messaging.OrderCreatedEvent;
import com.demo.deliverytracker.repo.DeliveryRepository;

@Service
public class DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private final DeliveryRepository repo;

    public DeliveryService(DeliveryRepository repo) {
        this.repo = repo;
    }

    public List<Delivery> all() {
        return repo.findAll();
    }

    public Delivery createFromEvent(OrderCreatedEvent evt, String correlationId) {
        return repo.findByOrderId(evt.getOrderId())
        .orElseGet(()-> {
            Delivery d = new Delivery(
                    null,
                    evt.getOrderId(),
                    evt.getItem(),
                    evt.getQuantity(),
                    "RECEIVED",
                    evt.getCreatedAt() != null ? evt.getCreatedAt() : Instant.now(),
                    Instant.now()
            );
            Delivery saved = repo.save(d);
            log.info("DELIVERY_RECEIVED from JMS : orderId={} item={} qty={} corrId={}",
                    saved.getOrderId(), saved.getItem(), saved.getQuantity(), correlationId);
            return saved;
        });
    }

    public Delivery markDelivered(Long id) {
        Delivery d = repo.findById(id).orElseThrow();
        d.setStatus("DELIVERED");
        d.setUpdatedAt(Instant.now());
        Delivery saved = repo.save(d);
        log.info("DELIVERY_MARKED_DELIVERED id={}", saved.getId());
        return saved;
    }

    // public Delivery updateStatus(Long id, String status) {
    //     Delivery d = repo.findById(id).orElseThrow();
    //     d.setStatus(status);
    //     d.setUpdatedAt(Instant.now());
    //     return repo.save(d);
    // }
}
