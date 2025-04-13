package com.demo.Service.Cart;
import com.demo.DTO.Cart.CartDTO;
import com.demo.Entity.Cart.Cart;
import com.demo.Entity.User.User;
import com.demo.Exception.Cart.CartNotFoundException;
import com.demo.Exception.User.UserNotFoundException;
import com.demo.Repository.Cart.CartRepository;
import com.demo.Repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Enables Mockito support with JUnit 5
class CartServiceImplTest 
{
    @Mock
    private CartRepository cartRepository;  //Mocking the CartRepository

    @Mock
    private UserRepository userRepository;  //Mocking the UserRepository

    @InjectMocks
    private CartServiceImpl cartService; //The class being tested, which will have mocks injected

    @Test
    void createCart_ShouldReturnSavedCartDTO_WhenUserExists() 
    {
        //Arrange
        CartDTO cartDTO = new CartDTO(1L, 100L);    //Simulate request DTO
        User user = new User();
        user.setUid(100L);                              //Simulate existing user with UID 100

        Cart savedCart = new Cart();
        savedCart.setCid(1L);                           //Simulate saved cart with generated CID
        savedCart.setUid(user);                             //Set user on saved cart

        //Mock the behavior of the user repository
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));  //User found

        //Mock the behavior of the cart repository to return the saved cart
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        //Act
        CartDTO result = cartService.createCart(cartDTO);   //Calling the service method

        //Assert
        assertNotNull(result);                              //Assert that result is not null
        assertEquals(1L, result.getCid());          //Assert that the cart ID is correct
        assertEquals(100L, result.getUid());        //Assert that the user ID is correct
    }

    @Test
    void getCart_ShouldReturnCartDTO_WhenCartExists() 
    {
        //Arrange
        User user = new User();
        user.setUid(200L);  //Simulate user with UID 200
        Cart cart = new Cart();
        cart.setCid(1L);    //Set cart ID
        cart.setUid(user);      //Set the user for this cart

        //Mock the behavior of the cart repository
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));    //Cart found

        //Act
        CartDTO result = cartService.getCart(1L);   //Calling the service method

        //Assert
        assertNotNull(result);                          //Assert that the result is not null
        assertEquals(1L, result.getCid());      //Assert that the cart ID matches
        assertEquals(200L, result.getUid());    //Assert that the user ID matches
    }

    @Test
    void getCarts_ShouldReturnListOfCartDTOs_WhenCartsExist() 
    {
        //Arrange
        User user = new User();
        user.setUid(300L);  //Simulate user with UID 300

        //Create two cart instances
        Cart cart1 = new Cart();
        cart1.setCid(1L);
        cart1.setUid(user);

        Cart cart2 = new Cart();
        cart2.setCid(2L);
        cart2.setUid(user);

        //Mock the behavior of the cart repository to return a list of carts
        when(cartRepository.findAll()).thenReturn(Arrays.asList(cart1, cart2));

        //Act
        List<CartDTO> result = cartService.getCarts();          //Calling the service method

        //Assert
        assertEquals(2, result.size());                 //Assert that there are 2 carts in the list
        assertEquals(1L, result.get(0).getCid());   //Assert that the first cart ID is 1
        assertEquals(2L, result.get(1).getCid());   //Assert that the second cart ID is 2
    }

    @Test
    void updateCart_ShouldReturnUpdatedCartDTO_WhenCartAndUserExist() 
    {
        //Arrange
        Long cid = 1L;      //The ID of the cart to update
        Long newUid = 400L; //The new user ID to update the cart with

        User newUser = new User();
        newUser.setUid(newUid);             //Create a new user with the new UID

        Cart existingCart = new Cart();
        existingCart.setCid(cid);           //Set the existing cart ID
        existingCart.setUid(new User());    //Set an initial user for the cart

        Cart updatedCart = new Cart();
        updatedCart.setCid(cid);        //Keep the same cart ID
        updatedCart.setUid(newUser);    //Set the updated user

        //Mock the behavior of the repositories
        when(cartRepository.findById(cid)).thenReturn(Optional.of(existingCart));   //Cart exists
        when(userRepository.findById(newUid)).thenReturn(Optional.of(newUser));     //New user exists
        when(cartRepository.save(any(Cart.class))).thenReturn(updatedCart);     //Save the updated cart

        //Act
        CartDTO updatedDTO = cartService.updateCart(cid, new CartDTO(cid, newUid)); //Calling the service method

        //Assert
        assertEquals(cid, updatedDTO.getCid());     //Assert that the cart ID remains the same
        assertEquals(newUid, updatedDTO.getUid());  //Assert that the updated user ID is correct
    }

    @Test
    void deleteCart_ShouldDeleteCart_WhenCartExists() 
    {
        //Arrange
        Long cid = 5L;              //The ID of the cart to delete
        Cart cart = new Cart();
        cart.setCid(cid);           //Set cart ID
        cart.setUid(new User());    //Assign a user to the cart

        //Mock the behavior of the cart repository
        when(cartRepository.findById(cid)).thenReturn(Optional.of(cart));   //Cart exists

        //Act
        cartService.deleteCart(cid);    //Calling the service method to delete the cart

        //Assert
        verify(cartRepository, times(1)).delete(cart);  //Verify that the delete method was called once
    }

    @Test
    void createCart_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() 
    {
        //Arrange
        CartDTO cartDTO = new CartDTO(1L, 999L);                                                //Simulate request with a non-existent user ID
        when(userRepository.findById(999L)).thenReturn(Optional.empty());                            //Mock user not found

        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> cartService.createCart(cartDTO));   //Assert that exception is thrown
    }

    @Test
    void getCart_ShouldThrowCartNotFoundException_WhenCartDoesNotExist() 
    {
        //Arrange
        when(cartRepository.findById(404L)).thenReturn(Optional.empty());                       //Mock cart not found

        //Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.getCart(404L)); //Assert that exception is thrown
    }

    @Test
    void updateCart_ShouldThrowCartNotFoundException_WhenCartDoesNotExist() 
    {
        //Arrange
        when(cartRepository.findById(10L)).thenReturn(Optional.empty()); //Mock cart not found

        //Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.updateCart(10L, new CartDTO(10L, 500L)));   //Assert that exception is thrown
    }

    @Test
    void updateCart_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() 
    {
        //Arrange
        Long cid = 2L;
        Cart cart = new Cart();
        cart.setCid(cid);       //Set existing cart

        //Mock repository behavior
        when(cartRepository.findById(cid)).thenReturn(Optional.of(cart));       //Cart exists
        when(userRepository.findById(888L)).thenReturn(Optional.empty());   //Mock user not found

        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> cartService.updateCart(cid, new CartDTO(cid, 888L)));   //Assert that exception is thrown
    }

    @Test
    void deleteCart_ShouldThrowCartNotFoundException_WhenCartDoesNotExist() 
    {
        //Arrange
        when(cartRepository.findById(777L)).thenReturn(Optional.empty());                           //Mock cart not found

        //Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.deleteCart(777L));  //Assert that exception is thrown
    }
}