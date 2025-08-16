package com.demo.deliverytracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.deliverytracker.domain.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
