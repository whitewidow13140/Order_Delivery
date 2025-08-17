package com.demo.ordermanager.events;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
  List<EventEntity> findByCorrelationIdOrderByOccurredAt(UUID correlationId);
  List<EventEntity> findByOrderIdOrderByOccurredAt(Long orderId);
}
