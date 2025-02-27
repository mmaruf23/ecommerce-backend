package com.solo.ecommerce.util;

import com.solo.ecommerce.dto.response.ProductResponse;
import com.solo.ecommerce.dto.response.UserResponse;
import com.solo.ecommerce.model.Product;
import com.solo.ecommerce.model.User;

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
}
