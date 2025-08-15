package com.demo.ordermanager.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.messaging.OrderCreatedEvent;
import com.demo.ordermanager.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository repo;
    private final JmsTemplate jmsTemplate;

    public Order create(Order order) {
        // Qui est l’utilisateur ?
        String username = "anonymous";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        }

        Order saved = repo.save(order);

        log.info("ORDER_CREATED id={} item={} qty={} byUser={}",
                saved.getId(), saved.getItem(), saved.getQuantity(), username);

        OrderCreatedEvent evt = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .item(saved.getItem())
                .quantity(saved.getQuantity())
                .createdAt(saved.getCreatedAt())
                .build();
        try {
            jmsTemplate.convertAndSend("queue.orders.new", evt);
            log.info("JMS_EVENT_PUBLISHED orderId={} destination={}", saved.getId(), "queue.orders.new");
        } catch (Exception e) {
            log.error("JMS send failed, orderId={} (order created but event NOT sent)", saved.getId(), e);
        }
        return saved;
    }

    public List<Order> all() { return repo.findAll(); }
}
