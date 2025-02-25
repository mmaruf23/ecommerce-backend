package com.solo.ecommerce.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    @NotBlank(message = "Nama produk tidak boleh kosong")
    private String name;

//    @NotBlank -- description boleh kosong.
    private String description;

    @NotNull(message = "Harga produk tidak boleh kosong")
    private Double price;

    @NotNull(message = "Category tidak boleh kosong")
    private Long categoryId;

//    @NotNull(message = "Stok tidak boleh kosong") pake nilai default 0 aja deh
    private Integer stock;

    @NotNull
    private MultipartFile imagePath;

    @JsonSetter
    public void setStock(Integer stock) {
        this.stock = (stock == null) ? 0 : stock;
    }
}

