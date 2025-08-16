package com.demo.shared.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.deliverytracker.domain.Delivery;
import com.demo.deliverytracker.service.DeliveryService;

@Controller
public class DeliveryController {

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    // UI
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("deliveries", service.all());
        return "index";
    }

    // REST API
    @RestController
    @RequestMapping("/deliveries")
    static class Api {
        private final DeliveryService service;

        public Api(DeliveryService service) {
            this.service = service;
        }

        @GetMapping
        public List<Delivery> list() {
            return service.all();
        }

        @PostMapping("/{id}/delivered")
        public ResponseEntity<Delivery> markDelivered(@PathVariable Long id) {
            return ResponseEntity.status(HttpStatus.OK).body(service.markDelivered(id));
        }
    }
}
