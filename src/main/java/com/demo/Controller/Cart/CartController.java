package com.demo.Controller.Cart;
import com.demo.DTO.Cart.CartDTO; 
import com.demo.Interface.Cart.CartService; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import java.util.List; 

@RestController             //Marks this class as a controller in a RESTful service
@RequestMapping("/carts")   //Specifies the base URL for the cart-related endpoints
public class CartController 
{
    private final CartService cartService;  //Declaring a CartService instance for business logic

    //Constructor to inject the CartService into the controller
    public CartController(CartService cartService) 
    {
        this.cartService = cartService;
    }

    @PostMapping    //Endpoint to create a new cart (HTTP POST)
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) 
    {
        //Creating a new cart and returning a ResponseEntity with the created CartDTO
        return ResponseEntity.ok(cartService.createCart(cartDTO));
    }

    @GetMapping("/{cid}")   //Endpoint to retrieve a specific cart by its ID (HTTP GET)
    public ResponseEntity<CartDTO> getCart(@PathVariable Long cid) 
    {
        //Retrieving a cart by ID and returning a ResponseEntity with the found CartDTO
        return ResponseEntity.ok(cartService.getCart(cid));
    }

    @GetMapping //Endpoint to retrieve all carts (HTTP GET)
    public ResponseEntity<List<CartDTO>> getAllCarts() 
    {
        //Retrieving all carts and returning a ResponseEntity with a list of CartDTOs
        return ResponseEntity.ok(cartService.getCarts());
    }

    @PutMapping("/{cid}")   //Endpoint to update an existing cart by its ID (HTTP PUT)
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long cid, @RequestBody CartDTO cartDTO) 
    {
        //Updating an existing cart and returning a ResponseEntity with the updated CartDTO
        return ResponseEntity.ok(cartService.updateCart(cid, cartDTO));
    }

    @DeleteMapping("/{cid}")    //Endpoint to delete a specific cart by its ID (HTTP DELETE)
    public ResponseEntity<Void> deleteCart(@PathVariable Long cid) 
    {
        //Deleting a cart and returning a ResponseEntity with no content (HTTP 204)
        cartService.deleteCart(cid);
        return ResponseEntity.noContent().build();
    }
}