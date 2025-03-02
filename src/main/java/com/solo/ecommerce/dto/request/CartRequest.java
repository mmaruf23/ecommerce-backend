package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * request for add product to cart
 */
@Data
public class CartRequest {

    @NotNull(message = "Product id is required!")
    private Long productId;
    @NotNull(message = "Quantity is required!")
    private int qty;
}
