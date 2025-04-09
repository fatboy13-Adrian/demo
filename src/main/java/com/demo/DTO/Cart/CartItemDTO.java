package com.demo.DTO.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //Generates getters for all fields
@NoArgsConstructor  //Generates a no-argument constructor
@AllArgsConstructor //Generates a constructor with all arguments
@Builder            //Generates a builder pattern for creating instances
public class CartItemDTO 
{
    //Unique identifier for the cart item, typically auto-generated by the database
    //The ID of the cart in the item
    //The ID of the item where the cart is stored
    private Long ciid, cid, iid;
}