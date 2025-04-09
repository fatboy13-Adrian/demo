package com.demo.Repository.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entity.Cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>
{

}