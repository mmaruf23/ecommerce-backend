package com.solo.ecommerce.dto.response;

import lombok.Data;
import java.util.List;

/**
 * response for order request.
 */
@Data
public class OrderResponse {
    private Long id;
    private String status;
    private Double totalPrice;
    private List<OrderItemResponse> orderItems;

    @Data
    public static class OrderItemResponse {
        private String productName;
        private Double productPrice;
        private int quantity;
    }
}


