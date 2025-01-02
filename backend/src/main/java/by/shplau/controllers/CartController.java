package by.shplau.controllers;

import by.shplau.dto.requests.CartItemRequest;
import by.shplau.entities.Cart;
import by.shplau.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Cart cart = cartService.getCartForUser(authentication.getName());
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(Authentication authentication, @RequestBody CartItemRequest request) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        try {
            if (request.getQuantity() <= 0) {
                return ResponseEntity.badRequest()
                        .build();
            }
            Cart cart = cartService.addItemToCart(
                    authentication.getName(),
                    request.getProductId(),
                    request.getQuantity()
            );
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(Authentication authentication, @PathVariable Long productId) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Cart cart = cartService.removeItemFromCart(authentication.getName(), productId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/clear")
    public void clearCart(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        try {
            cartService.clearCart(authentication.getName());
        } catch (Exception e) {
            return;
        }
    }
}
