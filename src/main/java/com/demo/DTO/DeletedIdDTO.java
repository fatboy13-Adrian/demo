package com.demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter              //Automatically generates getter methods for all fields
@Setter              //Automatically generates setter methods for all fields
@NoArgsConstructor   //Automatically generates a no-argument constructor for the class
@AllArgsConstructor  //Automatically generates a constructor with arguments for all fields
@Builder             //Enables the Builder pattern for this class, making it easier to instantiate
public class DeletedIdDTO 
{
    private Long deletedId;     //Represents the ID of the deleted entity (e.g., Order, Customer)
    private String entityType;  //Represents the type of the entity that was deleted (e.g., Order, Customer)
}