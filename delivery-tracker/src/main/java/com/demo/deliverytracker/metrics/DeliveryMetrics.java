package com.demo.deliverytracker.metrics;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class DeliveryMetrics {
  private final DistributionSummary e2eMs;

  public DeliveryMetrics(MeterRegistry reg) {
    this.e2eMs = DistributionSummary.builder("dt.delivery.e2e_ms")
        .description("End-to-end latency order->delivery in ms")
        .serviceLevelObjectives(100.0, 250.0, 500.0, 1000.0, 2000.0)
        .register(reg);
  }

  public void recordE2E(Instant createdAt) {
    if (createdAt == null) return;
    long ms = Duration.between(createdAt, Instant.now()).toMillis();
    if (ms >= 0) e2eMs.record(ms);
  }
}
