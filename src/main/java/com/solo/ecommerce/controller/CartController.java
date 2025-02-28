package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.AddToCartRequest;
import com.solo.ecommerce.model.User;
import com.solo.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@AuthenticationPrincipal User user, @Valid @RequestBody AddToCartRequest request) {
        cartService.addProductToCart(user, request.getProductId(), request.getQty());
        return ResponseEntity.ok("Product added to cart");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProductCart(@AuthenticationPrincipal User user, @Valid @RequestBody AddToCartRequest request) {
        cartService.updateProductCart(user, request.getProductId(), request.getQty());
        return ResponseEntity.ok("Cart product updated");
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProductCart(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        cartService.deleteProductCart(user, productId);
        return ResponseEntity.ok("Produk berhasil dihapus dari cart");
    }

}
