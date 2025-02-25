package com.solo.ecommerce.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private CategoryData category;
    private Integer stock;
    private String image;

    public ProductResponse() {
        this.category = new CategoryData();
    }

    public void setCategoryId(Long id){
        this.category.setId(id);
    }

    public void setCategoryName(String name) {
        this.category.setName(name);
    }
}

@Data
class CategoryData {
    private Long id;
    private String name;
}