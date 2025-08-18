package com.demo.ordermanager.events;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events", schema = "order_manager")
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "correlation_id", nullable = false)
  private UUID correlationId;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "event_type", nullable = false)
  private String eventType;

  @Column(name = "occurred_at", nullable = false)
  private OffsetDateTime occurredAt = OffsetDateTime.now();

  @Column(name = "produced_by", nullable = false)
  private String producedBy = "order-manager";

  @Column(columnDefinition = "jsonb", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private JsonNode payload;

  @Column(columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private JsonNode headers;

  @Column(nullable = false)
  private Integer version = 1;

  // Getters/Setters
  public Long getId() { return id; }
  public UUID getCorrelationId() { return correlationId; }
  public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
  public Long getOrderId() { return orderId; }
  public void setOrderId(Long orderId) { this.orderId = orderId; }
  public String getEventType() { return eventType; }
  public void setEventType(String eventType) { this.eventType = eventType; }
  public OffsetDateTime getOccurredAt() { return occurredAt; }
  public void setOccurredAt(OffsetDateTime occurredAt) { this.occurredAt = occurredAt; }
  public String getProducedBy() { return producedBy; }
  public void setProducedBy(String producedBy) { this.producedBy = producedBy; }
  public JsonNode getPayload() { return payload; }
  public void setPayload(JsonNode payload) { this.payload = payload; }
  public JsonNode getHeaders() { return headers; }
  public void setHeaders(JsonNode headers) { this.headers = headers; }
  public Integer getVersion() { return version; }
  public void setVersion(Integer version) { this.version = version; }
}
