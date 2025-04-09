package com.demo.Interface.Cart;
import java.util.List;
import com.demo.DTO.Cart.CartItemDTO;

public interface CartItemService
{
    CartItemDTO createCartItem(CartItemDTO dto);
    CartItemDTO getCartItem(Long ciid);
    List<CartItemDTO> getCartItems();
    CartItemDTO updateCartItem(Long ciid, CartItemDTO dto);
    void deleteCartItem(Long ciid);
}