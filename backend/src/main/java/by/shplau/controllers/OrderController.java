package by.shplau.controllers;

import by.shplau.dto.requests.OrderRequest;
import by.shplau.entities.Order;
import by.shplau.services.EmailService;
import by.shplau.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        try {
            String username = authentication.getName();
            Order order = orderService.createOrder(username, request);

            emailService.sendToCustomer(order);
            emailService.sendToSeller(order);

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
