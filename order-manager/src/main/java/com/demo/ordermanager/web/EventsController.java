package com.demo.ordermanager.web;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ordermanager.events.EventEntity;
import com.demo.ordermanager.events.EventRepository;

@RestController
@RequestMapping("/events")
public class EventsController {

  private final EventRepository repo;

  public EventsController(EventRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<EventEntity> search(@RequestParam(required = false) Long orderId,
                                  @RequestParam(required = false) UUID correlationId) {
    if (correlationId != null) {
      return repo.findByCorrelationIdOrderByOccurredAt(correlationId);
    }
    if (orderId != null) {
      return repo.findByOrderIdOrderByOccurredAt(orderId);
    }
    // par défaut, renvoie les 100 derniers
    return repo.findAll().stream()
      .sorted((a, b) -> a.getOccurredAt().compareTo(b.getOccurredAt()))
      .limit(100)
      .toList();
  }
}
