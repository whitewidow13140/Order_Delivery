package com.demo.ordermanager.events;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.jms.support.JmsHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.Destination;

@Service
public class EventLogService {
  private final ObjectMapper om;
  private final EventRepository repo;

  public EventLogService(ObjectMapper om, EventRepository repo) {
    this.om = om;
    this.repo = repo;
  }

  public void record(String correlationId, Long orderId, String type, Object payload) {
    record(correlationId, orderId, type, payload, Map.of());
  }

  public void record(String correlationId, Long orderId, String type,
                     Object payload, Map<String, ?> headers) {
    EventEntity e = new EventEntity();
    e.setCorrelationId(UUID.fromString(correlationId));
    e.setOrderId(orderId);
    e.setEventType(type);
    e.setPayload(toJson(payload));
    e.setHeaders(toJson(sanitizeHeaders(headers)));
    repo.save(e);
  }

    private Map<String, Object> sanitizeHeaders(Map<String, ?> headers) {
    if (headers == null || headers.isEmpty()) return Map.of();
    Map<String, Object> out = new LinkedHashMap<>();

    headers.forEach((k, v) -> {
      if (v == null) return;
      // On garde uniquement les types simples
      if (v instanceof CharSequence || v instanceof Number || v instanceof Boolean || v.getClass().isEnum()) {
        out.put(k, v.toString());
      } else if (JmsHeaders.CORRELATION_ID.equals(k) || "jms_correlationId".equalsIgnoreCase(k)) {
        out.put("jms_correlationId", String.valueOf(v));
      } else if ("jms_messageId".equalsIgnoreCase(k) || JmsHeaders.MESSAGE_ID.equals(k)) {
        out.put("jms_messageId", String.valueOf(v));
      } else if ("jms_timestamp".equalsIgnoreCase(k) || JmsHeaders.TIMESTAMP.equals(k)) {
        out.put("jms_timestamp", String.valueOf(v));
      } else if ("jms_destination".equalsIgnoreCase(k) || v instanceof Destination) {
        out.put("jms_destination", String.valueOf(v)); // toString() suffit
      }
      // tout le reste est ignoré pour éviter des objets non-sérialisables
    });
    return out;
  }

  private JsonNode toJson(Object o) {
    return om.valueToTree(o); // renvoie un JsonNode
  }
}
