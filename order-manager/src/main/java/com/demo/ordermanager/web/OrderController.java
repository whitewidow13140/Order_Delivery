package com.demo.ordermanager.web;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    // UI
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("orders", service.all());
        model.addAttribute("order", new Order());
        return "index";
    }

    @PostMapping("/ui/orders")
    public String createFromUI(@ModelAttribute Order order) {
        service.create(order);
        return "redirect:/";
    }

    // -------- REST API --------
    @RestController
    @RequestMapping("/orders")
    @RequiredArgsConstructor
    static class Api {
        private final OrderService service;

        @PostMapping
        public ResponseEntity<Order> create(@RequestBody Order order) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(order));
        }

        @GetMapping
        public List<Order> list() {
            return service.all();
        }
    }
}
