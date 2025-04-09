package com.demo.Repository.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entity.Cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> 
{
    //Custom method to find a cart by its ID
    Optional<Cart> findById(Long cid);  //Returns an Optional of Cart to handle the case where the cart might not be found
}