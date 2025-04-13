package com.demo.Controller.Cart;
import com.demo.DTO.Cart.CartDTO;
import com.demo.Interface.Cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)         //This annotation ensures that Mockito initializes the mocks and injects them into the test class
class CartControllerTest 
{
    @Mock
    private CartService cartService;        //Mocking the CartService to simulate the service layer

    @InjectMocks
    private CartController cartController;  //Injecting the mocked cartService into the cartController

    private CartDTO cartDTO;                //CartDTO object to be used in the tests

    //This method runs before each test to set up the initial data
    @BeforeEach
    void setUp() 
    {
        cartDTO = new CartDTO(1L, 100L);    //Initialize a CartDTO with a sample cart ID and user ID
    }

    @Test   //Test for successfully creating a cart
    void createCart_Positive() 
    {
        when(cartService.createCart(cartDTO)).thenReturn(cartDTO);              //Mocking the CartService's createCart method to return the cartDTO

        ResponseEntity<CartDTO> response = cartController.createCart(cartDTO);  //Calling the controller method

        //Asserting that the response has a 200 OK status and the response body contains the correct cartDTO
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cartDTO, response.getBody());
    }

    @Test   //Test for successfully retrieving a cart by its ID
    void getCart_Positive() 
    {
        when(cartService.getCart(1L)).thenReturn(cartDTO);              //Mocking the CartService's getCart method to return the cartDTO

        ResponseEntity<CartDTO> response = cartController.getCart(1L);  //Calling the controller method

        //Asserting that the response has a 200 OK status and the correct cartDTO is returned
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cartDTO, response.getBody());
    }

    @Test   //Test for successfully retrieving all carts
    void getAllCarts_Positive() 
    {
        List<CartDTO> cartList = Arrays.asList(cartDTO);                        //Create a list with a single cartDTO
        when(cartService.getCarts()).thenReturn(cartList);                      //Mocking the CartService's getCarts method to return the list

        ResponseEntity<List<CartDTO>> response = cartController.getAllCarts();  //Calling the controller method

        //Asserting that the response has a 200 OK status and the correct list of carts is returned
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test   //Test for successfully updating a cart
    void updateCart_Positive() 
    {
        when(cartService.updateCart(1L, cartDTO)).thenReturn(cartDTO);              //Mocking the CartService's updateCart method to return the updated cartDTO

        ResponseEntity<CartDTO> response = cartController.updateCart(1L, cartDTO);  //Calling the controller method

        //Asserting that the response has a 200 OK status and the updated cartDTO is returned
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cartDTO, response.getBody());
    }

    @Test   //Test for successfully deleting a cart
    void deleteCart_Positive() 
    {
        doNothing().when(cartService).deleteCart(1L);                   //Mocking the CartService's deleteCart method to do nothing

        ResponseEntity<Void> response = cartController.deleteCart(1L);  //Calling the controller method

        //Asserting that the response has a 204 No Content status and no response body
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test   //Test for getting a cart that does not exist
    void getCart_Negative_CartNotFound() 
    {
        //Mocking the CartService's getCart method to throw an exception
        when(cartService.getCart(999L)).thenThrow(new RuntimeException("Cart not found")); 

        Exception exception = assertThrows(RuntimeException.class, () -> 
        {
            cartController.getCart(999L);   //Calling the controller method with a non-existent cart ID
        });

        //Asserting that the exception message matches the expected message
        assertEquals("Cart not found", exception.getMessage());
    }

    @Test   //Test for updating a cart when the user does not exist
    void updateCart_Negative_UserNotFound() 
    {
        //Mocking the CartService's updateCart method to throw an exception
        when(cartService.updateCart(eq(1L), any(CartDTO.class))).thenThrow(new RuntimeException("User not found")); 

        Exception exception = assertThrows(RuntimeException.class, () -> 
        {
            cartController.updateCart(1L, cartDTO); //Calling the controller method to update a cart with a non-existent user
        });

        //Asserting that the exception message matches the expected message
        assertEquals("User not found", exception.getMessage());
    }

    @Test   //Test for deleting a cart that does not exist
    void deleteCart_Negative_CartNotFound() 
    {
        //Mocking the CartService's deleteCart method to throw an exception
        doThrow(new RuntimeException("Cart not found")).when(cartService).deleteCart(999L); 

        Exception exception = assertThrows(RuntimeException.class, () -> 
        {
            cartController.deleteCart(999L);    //Calling the controller method with a non-existent cart ID
        });

        //Asserting that the exception message matches the expected message
        assertEquals("Cart not found", exception.getMessage());
    }
}