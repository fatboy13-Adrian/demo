package com.demo.Controller.User;

import com.demo.DTO.User.UserDTO;
import com.demo.Service.User.UserServiceImpl;
import com.demo.Exception.User.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Endpoint to get a single user by ID
    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long uid) {
        try {
            UserDTO user = userService.getUser(uid);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to update a user's details (partial update)
    @PatchMapping("/{uid}")
    public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable Long uid, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.partialUpdateUser(uid, userDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a user
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long uid) {
        try {
            userService.deleteUser(uid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}