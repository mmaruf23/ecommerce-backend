package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.ProductRequest;
import com.solo.ecommerce.dto.request.UpdateProductRequest;
import com.solo.ecommerce.dto.response.PaginatedResponse;
import com.solo.ecommerce.dto.response.ProductResponse;
import com.solo.ecommerce.model.Product;
import com.solo.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> create(@ModelAttribute @Valid ProductRequest request, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            throw new ValidationException("Validasi gagal : " + Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        ProductResponse response = productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @ModelAttribute @Valid UpdateProductRequest request) throws IOException {
        ProductResponse response = productService.editProduct(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (id != null) {
            productService.deleteProduct(id);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Berhasil hapus product");
    }

    @GetMapping("/all")
    public ResponseEntity<?> allProduct(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        Page<ProductResponse> responses = productService.findAllProduct(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedResponse<>(HttpStatus.OK.value(), responses));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> productByCategory(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        Page<ProductResponse> responses = productService.findProductsByCategory(id, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedResponse<>(HttpStatus.OK.value(), responses));
    }

    @GetMapping(value = "/images/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) throws IOException {

        byte[] image = productService.getImage(filename);

        String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        MediaType mediaType = switch (fileExtension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM; // nilai default -> data byte
        };

        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(image);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProduct(keyword));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> findProduct(@PathVariable String slug) {
        return ResponseEntity.ok(productService.findByName(slug));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }


}
