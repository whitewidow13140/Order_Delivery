package com.demo.ordermanager.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OrderMetrics {
  private final Counter ordersCreated;
  public OrderMetrics(MeterRegistry reg) {
    this.ordersCreated = Counter.builder("om.orders.created")
        .description("Number of orders created")
        .register(reg);
  }
  public void markCreated() { ordersCreated.increment(); }
}