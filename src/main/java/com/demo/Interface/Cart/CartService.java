package com.demo.Interface.Cart;
import java.util.List;
import com.demo.DTO.Cart.CartDTO;   //Importing CartDTO to be used as the data transfer object for cart-related operations

//Interface for CartService, defining the methods related to cart operations
public interface CartService 
{
    CartDTO createCart(CartDTO cartDTO);            //Method to create a new cart based on CartDTO input
    CartDTO getCart(Long cid);                      //Method to retrieve a specific cart by its ID
    List<CartDTO> getCarts();                       //Method to retrieve a list of all carts
    CartDTO updateCart(Long cid, CartDTO cartDTO);  //Method to update an existing cart with new details
    void deleteCart(Long cid);                      //Method to delete a cart by its ID
}