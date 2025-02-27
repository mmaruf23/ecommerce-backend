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

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> create(@ModelAttribute @Valid ProductRequest request, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            throw new ValidationException("Validasi gagal : + " + result.getFieldError().getDefaultMessage());
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
}
