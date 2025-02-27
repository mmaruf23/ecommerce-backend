package com.solo.ecommerce.service;

import com.solo.ecommerce.exception.DataNotFoundException;
import com.solo.ecommerce.model.*;
import com.solo.ecommerce.repository.CartItemsRepository;
import com.solo.ecommerce.repository.CartRepository;
import com.solo.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public void addProductToCart(User user, Product product, int quantity) {
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> createCart(user));

        cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .ifPresentOrElse(
                        cartItem -> cartItem.setQty(cartItem.getQty() + quantity),
                        () -> {
                            CartItems newItem = new CartItems();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQty(quantity);
                            cart.getCartItems().add(newItem);
                        }
                );

        cartRepository.save(cart);

    }

    public void updateProductCart(User user, Product product, int quantity) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Cart not found for user"));
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .ifPresent(cartItem -> {
                    if (quantity == 0) {
                        cart.getCartItems().remove(cartItem); // Hapus jika qty = 0
                    } else {
                        cartItem.setQty(quantity);
                    }
                    cartRepository.save(cart);
                });
    }

    public void deleteProductCart(User user ,Product product) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Cart not found for user"));
        if (cart.getCartItems().removeIf(item -> item.getProduct().equals(product))){
            cartRepository.save(cart);
        } else  {
            throw new DataNotFoundException("Product not found in cart");
        }
    }

    public Order checkoutCart(User user) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Cart not found for user"));
        if (cart.getCartItems().isEmpty()) {
            throw new DataNotFoundException("Cart is empty, cannot checkout");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQty()).sum());
        order.setStatus(Status.PROCESS);
        order = orderRepository.save(order);

        // Buat orderItems setelah order tersimpan
        List<OrderItems> orderItems = new ArrayList<>();
        for (CartItems item : cart.getCartItems()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQty(item.getQty());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order = orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return order;
    }




}
