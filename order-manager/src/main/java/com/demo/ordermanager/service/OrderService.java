package com.demo.ordermanager.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.messaging.OrderCreatedEvent;
import com.demo.ordermanager.repo.OrderRepository;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final JmsTemplate jmsTemplate;

    public OrderService(OrderRepository orderRepository, JmsTemplate jmsTemplate) {
        this.orderRepository = orderRepository;
        this.jmsTemplate = jmsTemplate;
    }

    /** Utilisé par l'UI et l'API (ton contrôleur) */
    public List<Order> all() {
        return orderRepository.findAll(); // ou trier si tu veux
    }

    /** Utilisé par l'UI et l'API (ton contrôleur) */
    public Order create(Order order) {
        String username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName).orElse("anonymous");
        String correlationId = Optional.ofNullable(MDC.get("corrId"))
                .orElse(UUID.randomUUID().toString());
        return create(order.getItem(), order.getQuantity(), username, correlationId);
    }

    /** Brique métier “bas niveau” appelée par la méthode au-dessus */
    public Order create(String item, int quantity, String username, String correlationId) {
        Order saved = orderRepository.save(new Order(null, item, quantity, Instant.now()));

        log.info("ORDER_CREATED id={} item={} qty={} byUser={} corrId={}",
                saved.getId(), saved.getItem(), saved.getQuantity(), username, correlationId);

        OrderCreatedEvent evt = new OrderCreatedEvent(
                saved.getId(), saved.getItem(), saved.getQuantity(), saved.getCreatedAt()
        );

        jmsTemplate.convertAndSend("queue.orders.new", evt, msg -> {
            msg.setStringProperty("_type", "OrderCreatedEvent");
            msg.setJMSCorrelationID(correlationId);
            msg.setStringProperty("corrId", correlationId);
            return msg;
        });

        return saved;
    }
}
