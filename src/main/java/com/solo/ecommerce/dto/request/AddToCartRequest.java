package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotBlank(message = "User id is required!")
    private String username;
    @NotBlank(message = "Product id is required!")
    private Long productId;
    @NotBlank(message = "Quantity is required!")
    private int qty;
}
