package com.solo.ecommerce.service;

import com.solo.ecommerce.dto.request.CategoryRequest;
import com.solo.ecommerce.dto.response.CategoryResponse;
import com.solo.ecommerce.exception.DuplicateDataException;
import com.solo.ecommerce.model.Category;
import com.solo.ecommerce.repository.CategoryRepository;
import com.solo.ecommerce.util.ConvertToResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateDataException("Category su dah ada!");
        }

        Category category = new Category();
        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return ConvertToResponse.categoryToResponse(savedCategory);

    }

    @Transactional
    public CategoryResponse updateCategory(Long id, @Valid CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new DuplicateDataException("Category su dah ada!"));
        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return ConvertToResponse.categoryToResponse(savedCategory);
    }

    public List<CategoryResponse> findAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(ConvertToResponse::categoryToResponse).toList();
    }

}
