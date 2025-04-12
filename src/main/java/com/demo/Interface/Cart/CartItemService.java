package com.demo.Interface.Cart;
import java.util.List;
import com.demo.DTO.Cart.CartItemDTO;

//This interface defines the contract for operations related to Cart Items
public interface CartItemService 
{
    CartItemDTO createCartItem(CartItemDTO dto);            //Creates a new cart item from the provided DTO and returns the saved DTO
    CartItemDTO getCartItem(Long ciid);                     //Retrieves a specific cart item by its ID and returns it as a DTO
    List<CartItemDTO> getCartItems();                       //Retrieves all cart items and returns them as a list of DTOs
    CartItemDTO updateCartItem(Long ciid, CartItemDTO dto); //Updates a cart item identified by its ID using the provided DTO and returns the updated DTO
    void deleteCartItem(Long ciid);                         //Deletes a cart item identified by its ID
}