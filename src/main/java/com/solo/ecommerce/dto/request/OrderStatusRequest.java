package com.solo.ecommerce.dto.request;

import com.solo.ecommerce.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequest {

    @NotBlank(message = "Order id tidak boleh kosong")
    private Long orderId;
    @NotBlank(message = "Status harus diisi")
    private Status status;
}
