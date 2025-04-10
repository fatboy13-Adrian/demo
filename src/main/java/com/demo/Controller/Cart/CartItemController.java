package com.demo.Controller.Cart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.demo.DTO.Cart.CartItemDTO;
import com.demo.Exception.Cart.CartItemNotFoundException;
import com.demo.Interface.Cart.CartItemService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/item-inventory")
@Slf4j
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> createCartItem(@RequestBody CartItemDTO cartItemDTO) {
        try {
            log.info("Creating CartItem with details: {}", cartItemDTO);
            CartItemDTO createdCartItem = cartItemService.createCartItem(cartItemDTO);
            return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
        } catch (CartItemNotFoundException e) {
            log.error("Error creating CartItem: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{ciid}")
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable Long ciid) {
        try {
            log.info("Retrieving CartItem with ciid: {}", ciid);
            CartItemDTO cartItemDTO = cartItemService.getCartItem(ciid);
            return ResponseEntity.ok(cartItemDTO);
        } catch (CartItemNotFoundException e) {
            log.error("CartItem with ciid {} not found: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        log.info("Request to retrieve all CartItems");
        List<CartItemDTO> cartItemDTOList = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemDTOList);
    }

    @PutMapping("/{ciid}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long ciid, @RequestBody CartItemDTO cartItemDTO) {
        try {
            log.info("Updating CartItem with ciid: {} and new details: {}", ciid, cartItemDTO);
            CartItemDTO updatedCartItem = cartItemService.updateCartItem(ciid, cartItemDTO);
            return ResponseEntity.ok(updatedCartItem);
        } catch (CartItemNotFoundException e) {
            log.error("CartItem with ciid {} not found for update: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{ciid}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long ciid) {
        try {
            log.info("Deleting CartItem with ciid: {}", ciid);
            cartItemService.deleteCartItem(ciid);
            return ResponseEntity.noContent().build();
        } catch (CartItemNotFoundException e) {
            log.error("CartItem with ciid {} not found for deletion: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}