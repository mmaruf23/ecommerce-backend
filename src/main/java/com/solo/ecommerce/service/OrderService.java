package com.solo.ecommerce.service;

import com.solo.ecommerce.exception.DataNotFoundException;
import com.solo.ecommerce.model.*;
import com.solo.ecommerce.repository.CartRepository;
import com.solo.ecommerce.repository.OrderHistoryRepository;
import com.solo.ecommerce.repository.OrderRepository;
import com.solo.ecommerce.repository.ProductRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;


    public Order checkoutCart(User user) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Cart not found for user"));
        if (cart.getCartItems().isEmpty()) {
            throw new DataNotFoundException("Cart is empty, cannot checkout");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQty()).sum());
        order.setStatus(Status.PENDING);
        order = orderRepository.save(order);


        List<OrderItems> orderItems = new ArrayList<>();
        for (CartItems item : cart.getCartItems()) {
            if (item.getProduct().getStock() < item.getQty()) throw new DataIntegrityViolationException("Stok tidak cukup!");
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setProductPrice(item.getProduct().getPrice());
            orderItem.setQty(item.getQty());
            orderItems.add(orderItem);
        }

        for (OrderItems orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() - orderItem.getQty());
            productRepository.save(product);
        }

        order.setOrderItems(orderItems);
        order = orderRepository.save(order);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public Order updateOrderStatus(Long OrderId, Status newStatus) {
        Order order = orderRepository.findById(OrderId).orElseThrow(() -> new DataNotFoundException("Order not found"));

        if (order.getStatus() == newStatus) throw new IllegalStateException("Status tidak berubah");

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(newStatus);
        orderHistory.setChangeAt(LocalDateTime.now());
        orderHistoryRepository.save(orderHistory);

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order cancelOrder(User user, Long orderId) {
        Order order = orderRepository.findByUserAndId(user,orderId).orElseThrow(() -> new DataNotFoundException("Order not found"));
        if (order.getStatus() == Status.SHIPPED || order.getStatus() == Status.DELIVERED) {
            throw new IllegalStateException("Cannot cancel order that has  or delivered");
        }
        order.setStatus(Status.CANCELED);
        return orderRepository.save(order);
    }

    public List<OrderHistory> getOrderHistory(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("Order not found"));
        return orderHistoryRepository.findByOrder(order);
    }


    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }
    public List<OrderHistory> getAllOrderHistory() {
        return orderHistoryRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Order not found"));
    }

    public List<Order> getOrdersByStatus(Status status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
