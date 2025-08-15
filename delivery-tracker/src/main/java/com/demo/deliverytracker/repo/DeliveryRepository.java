package com.demo.deliverytracker.repo;

import com.demo.deliverytracker.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {}
