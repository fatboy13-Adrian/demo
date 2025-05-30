package com.demo.DTO.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //Generates getters for all fields
@NoArgsConstructor  //Generates a no-argument constructor
@AllArgsConstructor //Generates a constructor with all arguments
@Builder            //Generates a builder pattern for creating instances
public class OrderItemDTO 
{
    //Unique identifier for the order item, typically auto-generated by the database
    //The ID of the order in the item
    //The ID of the item where the order is stored
    private Long oiid, oid, iid;
}