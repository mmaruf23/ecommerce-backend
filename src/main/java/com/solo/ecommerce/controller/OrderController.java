package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.OrderStatusRequest;
import com.solo.ecommerce.dto.response.OrderResponse;
import com.solo.ecommerce.model.Order;
import com.solo.ecommerce.model.OrderHistory;
import com.solo.ecommerce.model.Status;
import com.solo.ecommerce.model.User;
import com.solo.ecommerce.security.CustomUserDetails;
import com.solo.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        OrderResponse response = orderService.checkoutCart(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getOrderByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Order> orders = orderService.getOrdersByUser(userDetails.getUser());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) throws AccessDeniedException {
        OrderResponse response = orderService.getOrderById(userDetails.getUser(), id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateOrderStatus(@Valid @RequestBody OrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        OrderResponse response = orderService.cancelOrder(userDetails.getUser(), id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable Status status) {
        List<OrderResponse> responses = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder() {
        List<OrderResponse> orders = orderService.getAllOrder();
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
