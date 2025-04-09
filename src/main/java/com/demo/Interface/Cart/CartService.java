package com.demo.Interface.Cart;
import java.util.List;
import com.demo.DTO.Cart.CartDTO;

public interface CartService 
{
    CartDTO createCart(CartDTO dto);
    CartDTO getCart(Long cid);
    List<CartDTO> getCarts();
    CartDTO updateCart(Long cid, CartDTO dto);
    void deleteCart(Long cid);
}