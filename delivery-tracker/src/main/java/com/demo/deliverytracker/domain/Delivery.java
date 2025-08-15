package com.demo.deliverytracker.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="deliveries")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String item;
    private Integer quantity;
    private String status; // PREPARING, SHIPPED, DELIVERED

    @PrePersist
    public void prePersist() {
        if (status == null) status = "PREPARING";
    }
}
