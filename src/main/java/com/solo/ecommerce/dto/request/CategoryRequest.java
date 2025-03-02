package com.solo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request for add or update a category
 */
@Data
public class CategoryRequest {
    @NotBlank(message = "Nama category tidak boleh kosong!")
    private String name;
}
