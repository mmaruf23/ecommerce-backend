package com.solo.ecommerce.service;

import com.solo.ecommerce.dto.request.ProductRequest;
import com.solo.ecommerce.dto.request.UpdateProductRequest;
import com.solo.ecommerce.dto.response.ProductResponse;
import com.solo.ecommerce.exception.DataNotFoundException;
import com.solo.ecommerce.model.Category;
import com.solo.ecommerce.model.Product;
import com.solo.ecommerce.repository.CategoryRepository;
import com.solo.ecommerce.repository.ProductRepository;
import com.solo.ecommerce.util.ConvertToResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private static final String imageDir ="src/main/resources/static/image/";
    private static final List<String>  allowedFileType = List.of("image/jpeg", "image/jpg", "image/png");

    @Transactional
    public ProductResponse addProduct(ProductRequest request) throws IOException {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new DataNotFoundException("Category ti dak valid"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setImage(saveImage(request.getImagePath()));
        product.setStock(request.getStock());

        Product savedProduct = productRepository.save(product);
        return ConvertToResponse.productToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse editProduct(Long id, UpdateProductRequest request) throws IOException {

        Product product = productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Product tidak ditemukan"));
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new DataNotFoundException("Category ti dak valid"));
            product.setCategory(category);
        }
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getImagePath() != null) product.setImage(saveImage(request.getImagePath()));
        if (request.getStock() != null) product.setStock(request.getStock());

        Product savedProduct = productRepository.save(product);
        return ConvertToResponse.productToResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)){
            throw new DataNotFoundException("Product dengan id " + id + " tidak ditemukan");
        }
        productRepository.deleteById(id);
    }

    public Page<ProductResponse> findAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ConvertToResponse::productToResponse);
    }


    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image tidak boleh kosong");
        }
        if (!allowedFileType.contains(file.getContentType())){
            throw new IllegalArgumentException("Format file tidak valid");
        }

        String customFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Path.of(imageDir + customFileName);
        Files.copy(file.getInputStream(), path);

        return customFileName;
    }

    public List<Product> findAvailableProduct() {
        return productRepository.findByStockGreaterThan(0);
    }

    public List<Product> findOutOfStockProduct() {
        return productRepository.findByStockLessThanEqual(0);
    }

    public Product findByName(String name) {

    }

}
