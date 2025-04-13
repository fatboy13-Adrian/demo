package com.demo.Interface.User;
import java.util.List;
import com.demo.DTO.User.UserDTO;

public interface UserService 
{
    UserDTO createUser(UserDTO userDTO);                    //Method to create a new user.
    UserDTO getUser(Long uid);                              //Method to get a user by their unique ID (uid). 
    List<UserDTO> getUsers();                               //Method to retrieve a list of all users. 
    UserDTO partialUpdateUser(Long uid, UserDTO userDTO);   //Method to partially update an existing user's information. 
    void deleteUser(Long uid);                              //Method to delete a user by their unique ID (uid).
}