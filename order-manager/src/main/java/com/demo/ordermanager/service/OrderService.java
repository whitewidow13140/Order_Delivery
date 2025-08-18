package com.demo.ordermanager.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
// import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.events.EventLogService;
import com.demo.ordermanager.messaging.OrderCreatedEvent;
import com.demo.ordermanager.messaging.OrderEventsPublisher;
import com.demo.ordermanager.repo.OrderRepository;

import com.demo.ordermanager.metrics.OrderMetrics;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final EventLogService eventLogService;
    private final OrderEventsPublisher eventsPublisher;

    private final OrderRepository orderRepository;
    // private final JmsTemplate jmsTemplate;

    private final OrderMetrics orderMetrics;

    public OrderService(OrderRepository orderRepository, EventLogService eventLogService, OrderEventsPublisher eventsPublisher, OrderMetrics orderMetrics) {
        this.orderRepository = orderRepository;
        // this.jmsTemplate = jmsTemplate;
        this.eventLogService = eventLogService;
        this.eventsPublisher = eventsPublisher;
        this.orderMetrics = orderMetrics;
    }

    /** Utilisé par l'UI et l'API (le controller) */
    public List<Order> all() {
        return orderRepository.findAll();
    }

    /** Utilisé par l'UI et l'API (le controller) */
    @Transactional
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

        OrderCreatedEvent evt = new OrderCreatedEvent(
                saved.getId(), saved.getItem(), saved.getQuantity(), saved.getCreatedAt()
        );

        // jmsTemplate.convertAndSend("queue.orders.new", evt, msg -> {
        //     msg.setStringProperty("_type", "OrderCreatedEvent");
        //     msg.setJMSCorrelationID(correlationId);
        //     msg.setStringProperty("corrId", correlationId);
        //     return msg;
        // });

        orderMetrics.markCreated();

        log.info("ORDER_CREATED id={} item={} qty={} byUser={} corrId={}",
            saved.getId(), saved.getItem(), saved.getQuantity(), username, correlationId);
        
        // ✅ journal évènement
        eventLogService.record(correlationId, saved.getId(), "ORDER_CREATED",
        Map.of("item", saved.getItem(), "quantity", saved.getQuantity(), "user", username));

        // ✅ publish JMS (avec createdAt pour la latence E2E)
        eventsPublisher.publishOrderCreated(
        new OrderCreatedEvent(saved.getId(), saved.getItem(), saved.getQuantity(), Instant.now())
        );

        return saved;
    }
}
