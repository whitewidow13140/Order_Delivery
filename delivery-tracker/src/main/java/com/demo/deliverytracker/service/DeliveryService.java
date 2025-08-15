package com.demo.deliverytracker.service;

import com.demo.deliverytracker.domain.Delivery;
import com.demo.deliverytracker.repo.DeliveryRepository;
import com.demo.deliverytracker.messaging.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository repo;

    public Delivery createFromEvent(OrderCreatedEvent evt) {
        Delivery d = Delivery.builder()
                .orderId(evt.getOrderId())
                .item(evt.getItem())
                .quantity(evt.getQuantity())
                .status("PREPARING")
                .build();
        return repo.save(d);
    }

    public Delivery updateStatus(Long id, String status) {
        Delivery d = repo.findById(id).orElseThrow();
        d.setStatus(status);
        return repo.save(d);
    }

    public List<Delivery> all() { return repo.findAll(); }
}
