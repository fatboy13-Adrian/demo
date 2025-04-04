package com.demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //Generates getters for all fields
@NoArgsConstructor  //Generates a no-argument constructor
@AllArgsConstructor //Generates a constructor with all arguments
@Builder            //Generates a builder pattern for creating instances
public class ItemInventoryDTO 
{
    private Long iid;   //The ID of the item in the inventory
    private Long sid;   //The ID of the inventory where the item is stored
}
