package com.solo.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private List<CartItemResponse> itemResponses;

    @Data
    @AllArgsConstructor
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Double productPrice;
        private int qty;
    }
}
