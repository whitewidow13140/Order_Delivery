package com.demo.deliverytracker.messaging;

import java.time.Instant;

public class OrderCreatedEvent {
    private Long orderId;
    private String item;
    private int quantity;
    private Instant createdAt;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(Long orderId, String item, int quantity, Instant createdAt) {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
