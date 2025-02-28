package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.OrderStatusRequest;
import com.solo.ecommerce.model.Order;
import com.solo.ecommerce.model.OrderHistory;
import com.solo.ecommerce.model.Status;
import com.solo.ecommerce.model.User;
import com.solo.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal User user) {
        Order order = orderService.checkoutCart(user);
        return ResponseEntity.ok(order);
    }



    @GetMapping("/me")
    public ResponseEntity<?> getOrderByUser(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateOrderStatus(@Valid @RequestBody OrderStatusRequest request) {
        orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
        return ResponseEntity.ok("Berhasil update status order");
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Order order = orderService.cancelOrder(user, id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable Status status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder() {
        List<Order> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all-histories")
    public ResponseEntity<?> getAllOrderHistory() {
        List<OrderHistory> orders = orderService.getAllOrderHistory();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getOrderHistoryByOrderId(@PathVariable Long id) {
        List<OrderHistory> orderHistories = orderService.getOrderHistory(id);
        return ResponseEntity.ok(orderHistories);
    }




}
