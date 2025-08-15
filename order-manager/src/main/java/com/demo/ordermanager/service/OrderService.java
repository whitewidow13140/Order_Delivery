package com.demo.ordermanager.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.MDC;
import java.util.Optional;
import java.util.UUID;

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
        // 1) Récupérer (ou générer) le corrId pour cette action
        String correlationId = Optional.ofNullable(MDC.get("corrId"))
                .orElse(UUID.randomUUID().toString());

        Order saved = repo.save(order);

        log.info("ORDER_CREATED id={} item={} qty={} byUser={} corrId={}",
                saved.getId(), saved.getItem(), saved.getQuantity(),
                currentUsername(), correlationId);

        OrderCreatedEvent evt = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .item(saved.getItem())
                .quantity(saved.getQuantity())
                .createdAt(saved.getCreatedAt())
                .correlationId(correlationId) // <-- NEW
                .build();

        try {
            // 2) Propager le corrId en en-tête JMS (et en propriété)
            jmsTemplate.convertAndSend("queue.orders.new", evt, m -> {
                m.setJMSCorrelationID(correlationId);
                m.setStringProperty("X-Correlation-Id", correlationId);
                return m;
            });
            log.info("JMS_EVENT_PUBLISHED orderId={} destination={} corrId={}",
                    saved.getId(), "queue.orders.new", correlationId);
        } catch (Exception e) {
            log.error("JMS send failed, orderId={} corrId={} (order created but event NOT sent)",
                    saved.getId(), correlationId, e);
        }
        return saved;
    }

    private String currentUsername() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }
    
    public List<Order> all() { return repo.findAll(); }
}
