package com.demo.DTO.User;

import com.demo.Enum.User.Role; 
import lombok.AllArgsConstructor; 
import lombok.Builder; 
import lombok.Getter; 
import lombok.NoArgsConstructor; 
import lombok.Setter; 

@Getter             //Automatically generates getter methods for all fields
@Setter             //Automatically generates setter methods for all fields
@NoArgsConstructor  //Generates a no-argument constructor, useful for frameworks like JPA and model binding
@AllArgsConstructor //Generates a constructor that initializes all fields, making it easy to create instances with values
@Builder            //Enables the builder pattern for creating instances of UserDTO in a more readable and flexible way
public class UserDTO 
{
    private Long uid;                           //Unique identifier for the user
    private String username, email, password;   //Username, email, and password fields for the user
    private Role role;                          //Role of the user (using the Role enum)
}
