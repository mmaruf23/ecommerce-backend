package com.solo.ecommerce.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * request for update data product. each are optional.
 */
@Data
public class UpdateProductRequest {
    private String name;

    private String description;

    private Double price;

    private Long categoryId;

    private Integer stock;

    private MultipartFile imagePath;

}
