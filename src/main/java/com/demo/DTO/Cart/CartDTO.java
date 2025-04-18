package com.demo.DTO.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //Generates getters for all fields
@NoArgsConstructor  //Generates a no-argument constructor
@AllArgsConstructor //Generates a constructor with all arguments
@Builder            //Generates a builder pattern for creating instances
public class CartDTO 
{
    //Unique identifier for the user, cart, typically auto-generated by the database
    //The ID of the user in the cart
    private Long cid, uid;
}