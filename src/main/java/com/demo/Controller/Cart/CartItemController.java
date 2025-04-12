package com.demo.Controller.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.demo.DTO.Cart.CartItemDTO;
import com.demo.Exception.Cart.CartItemNotFoundException;
import com.demo.Interface.Cart.CartItemService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController                     //Indicates this class handles RESTful web requests
@RequestMapping("/item-inventory")  //Base path for all endpoints in this controller
@Slf4j                              //Lombok annotation for logging (generates a logger instance)
public class CartItemController 
{
    private final CartItemService cartItemService;

    //Constructor-based dependency injection
    public CartItemController(CartItemService cartItemService) 
    {
        this.cartItemService = cartItemService;
    }

    @PostMapping    //Create a new CartItem
    public ResponseEntity<CartItemDTO> createCartItem(@RequestBody CartItemDTO cartItemDTO) 
    {
        try 
        {
            log.info("Creating CartItem with details: {}", cartItemDTO);
            CartItemDTO createdCartItem = cartItemService.createCartItem(cartItemDTO);
            return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);   //201 Created
        } 
        
        catch (CartItemNotFoundException e) 
        {
            log.error("Error creating CartItem: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //404 Not Found if related cart/item doesn't exist
        }
    }

    @GetMapping("/{ciid}")  //Get a single CartItem by its ID
    public ResponseEntity<CartItemDTO> getCartItem(@PathVariable Long ciid) {
        try 
        {
            log.info("Retrieving CartItem with ciid: {}", ciid);
            CartItemDTO cartItemDTO = cartItemService.getCartItem(ciid);
            return ResponseEntity.ok(cartItemDTO);  //200 OK
        } 
        
        catch (CartItemNotFoundException e) 
        {
            log.error("CartItem with ciid {} not found: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();   //404 Not Found
        }
    }

    @GetMapping //Get all CartItems
    public ResponseEntity<List<CartItemDTO>> getCartItems() 
    {
        log.info("Request to retrieve all CartItems");
        List<CartItemDTO> cartItemDTOList = cartItemService.getCartItems();
        return ResponseEntity.ok(cartItemDTOList);  //200 OK
    }

    @PutMapping("/{ciid}")  //Update a CartItem by its ID
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long ciid, @RequestBody CartItemDTO cartItemDTO) 
    {
        try 
        {
            log.info("Updating CartItem with ciid: {} and new details: {}", ciid, cartItemDTO);
            CartItemDTO updatedCartItem = cartItemService.updateCartItem(ciid, cartItemDTO);
            return ResponseEntity.ok(updatedCartItem);  //200 OK with updated data
        } 
        
        catch (CartItemNotFoundException e) 
        {
            log.error("CartItem with ciid {} not found for update: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();   //404 Not Found
        }
    }

    @DeleteMapping("/{ciid}")   //Delete a CartItem by its ID
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long ciid) 
    {
        try 
        {
            log.info("Deleting CartItem with ciid: {}", ciid);
            cartItemService.deleteCartItem(ciid);
            return ResponseEntity.noContent().build();  //204 No Content on successful deletion
        } 

        catch (CartItemNotFoundException e) 
        {
            log.error("CartItem with ciid {} not found for deletion: {}", ciid, e.getMessage());
            return ResponseEntity.notFound().build();   //404 Not Found
        }
    }
}