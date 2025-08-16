package com.demo.ordermanager.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ordermanager.domain.Order;
import com.demo.ordermanager.service.OrderService;

@Controller
public class OrderController {

    private final OrderService service;

    // Injection par constructeur (remplace @RequiredArgsConstructor)
    public OrderController(OrderService service) {
        this.service = service;
    }

    // ---- UI ----
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

    // ---- REST API ----
    @RestController
    @RequestMapping("/orders")
    static class Api {
        private final OrderService service;

        // Injection par constructeur (remplace @RequiredArgsConstructor)
        public Api(OrderService service) {
            this.service = service;
        }

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