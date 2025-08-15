package com.demo.deliverytracker.messaging;

import lombok.*;
import java.time.Instant;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderCreatedEvent {
    private Long orderId;
    private String item;
    private Integer quantity;
    private Instant createdAt;
}
