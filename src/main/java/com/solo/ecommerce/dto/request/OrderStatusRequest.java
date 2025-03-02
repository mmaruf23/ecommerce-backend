package com.solo.ecommerce.dto.request;

import com.solo.ecommerce.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request for update order status
 */
@Data
public class OrderStatusRequest {

    @NotNull(message = "Order id tidak boleh kosong")
    private Long orderId;
    @NotNull(message = "Status harus diisi")
    private Status status;
}
