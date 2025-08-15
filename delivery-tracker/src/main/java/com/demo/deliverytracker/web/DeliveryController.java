package com.demo.deliverytracker.web;

import com.demo.deliverytracker.domain.Delivery;
import com.demo.deliverytracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller @RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService service;

    // UI
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("deliveries", service.all());
        return "index";
    }

    // -------- REST API --------
    @RestController
    @RequestMapping("/deliveries")
    @RequiredArgsConstructor
    static class Api {
        private final DeliveryService service;

        @GetMapping
        public List<Delivery> list() { return service.all(); }

        @PutMapping("/{id}/status")
        public ResponseEntity<Delivery> updateStatus(@PathVariable Long id,
                                                     @RequestBody Map<String,String> body) {
            return ResponseEntity.ok(service.updateStatus(id, body.getOrDefault("status","PREPARING")));
        }
    }
}
