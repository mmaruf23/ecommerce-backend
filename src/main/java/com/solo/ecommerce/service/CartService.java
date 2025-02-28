package com.solo.ecommerce.service;

import com.solo.ecommerce.exception.DataNotFoundException;
import com.solo.ecommerce.model.*;
import com.solo.ecommerce.repository.*;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public void addProductToCart(User user, Long productId, int quantity) {
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> createCart(user));
        Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));

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

    public void updateProductCart(User user, Long productIs, int quantity) {
        Product product = productRepository.findById(productIs).orElseThrow(() -> new DataNotFoundException("Product not found"));
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

    public void deleteProductCart(User user, Long productId) {
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException("Cart not found for user"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));
        if (cart.getCartItems().removeIf(item -> item.getProduct().equals(product))){
            cartRepository.save(cart);
        } else  {
            throw new DataNotFoundException("Product not found in cart");
        }
    }






}
