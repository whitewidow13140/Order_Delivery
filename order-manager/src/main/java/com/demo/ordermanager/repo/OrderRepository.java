package com.demo.ordermanager.repo;

import com.demo.ordermanager.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
