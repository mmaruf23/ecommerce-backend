package com.solo.ecommerce.controller;

import com.solo.ecommerce.dto.request.CartRequest;
import com.solo.ecommerce.dto.response.CartResponse;
import com.solo.ecommerce.security.CustomUserDetails;
import com.solo.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody CartRequest request) {
        if (userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        cartService.addProductToCart(userDetails.getUser(), request.getProductId(), request.getQty());
        return ResponseEntity.ok("Product added to cart");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProductCart(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody CartRequest request) {
        if (userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        cartService.updateProductCart(userDetails.getUser(), request.getProductId(), request.getQty());
        return ResponseEntity.ok("Cart product updated");
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProductCart(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long productId) {
        if (userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        cartService.deleteProductCart(userDetails.getUser(), productId);
        return ResponseEntity.ok("Produk berhasil dihapus dari cart");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCartByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        CartResponse cartResponse = cartService.getCartByUser(userDetails.getUser());
        return ResponseEntity.ok(cartResponse);
    }

}
