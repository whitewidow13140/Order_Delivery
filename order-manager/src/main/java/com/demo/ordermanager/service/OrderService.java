package com.demo.ordermanager.service;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.messaging.OrderCreatedEvent;
import com.demo.ordermanager.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repo;
    private final JmsTemplate jmsTemplate;

    public Order create(Order order) {
        Order saved = repo.save(order);
        OrderCreatedEvent evt = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .item(saved.getItem())
                .quantity(saved.getQuantity())
                .createdAt(saved.getCreatedAt())
                .build();
        jmsTemplate.convertAndSend("queue.orders.new", evt);
        return saved;
    }

    public List<Order> all() { return repo.findAll(); }
}
