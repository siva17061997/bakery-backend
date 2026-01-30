package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* ===============================
       PLACE ORDER
    ================================ */
    @PostMapping
    @Transactional
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderDto dto) {

        Order order = orderService.placeOrder(dto);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "orderId", order.getId(),
                "totalAmount", order.getTotalAmount()
        ));
    }

    /* ===============================
       USER – MY ORDERS (JWT)
    ================================ */
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "error", "Unauthorized")
            );
        }

        String email = authentication.getName();

        List<Order> orders = orderService.getUserOrdersByEmail(email);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "orders", orders,
                "count", orders.size()
        ));
    }

    /* ===============================
       ADMIN – ALL ORDERS
    ================================ */
    @GetMapping("/admin")
    public ResponseEntity<?> getAllOrders() {

        List<Order> orders = orderService.getAllOrders();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "orders", orders,
                "count", orders.size()
        ));
    }

    /* ===============================
       ADMIN – ORDER DETAILS
       🔥 REGEX FIX APPLIED
    ================================ */
    @GetMapping("/{orderId:\\d+}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "order", order
        ));
    }

    /* ===============================
       ADMIN – UPDATE STATUS
    ================================ */
    @PutMapping("/{orderId:\\d+}/status")
    @Transactional
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> body) {

        Order updated = orderService.updateOrderStatus(
                orderId, body.get("status")
        );

        return ResponseEntity.ok(Map.of(
                "success", true,
                "orderId", updated.getId(),
                "status", updated.getStatus()
        ));
    }
}
