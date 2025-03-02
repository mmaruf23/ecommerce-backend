package com.solo.ecommerce.util;

import com.solo.ecommerce.dto.response.CartResponse;
import com.solo.ecommerce.dto.response.OrderResponse;
import com.solo.ecommerce.dto.response.ProductResponse;
import com.solo.ecommerce.dto.response.UserResponse;
import com.solo.ecommerce.model.Cart;
import com.solo.ecommerce.model.Order;
import com.solo.ecommerce.model.Product;
import com.solo.ecommerce.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertToResponse {

    public static ProductResponse productToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setPrice(product.getPrice());
        response.setImage(product.getImage());
        response.setStock(product.getStock());
        return response;
    }

    public static UserResponse userToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public static OrderResponse orderToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus().name());
        response.setTotalPrice(order.getTotalPrice());

        List<OrderResponse.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                    itemResponse.setProductName(item.getProductName());
                    itemResponse.setProductPrice(item.getProductPrice());
                    itemResponse.setQuantity(item.getQty());
                    return itemResponse;
                })
                .toList();

        response.setOrderItems(itemResponses);
        return response;
    }

    public static CartResponse cartToResponse(Cart cart) {
        List<CartResponse.CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> new CartResponse.CartItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQty()
                ))
                .collect(Collectors.toList()); // Convert CartItem to CartItemResponse


        return new CartResponse(cart.getId(), itemResponses);
    }

}
