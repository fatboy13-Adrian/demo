package com.demo.Controller.Cart;
import com.demo.DTO.Cart.CartItemDTO;
import com.demo.Exception.Cart.CartItemNotFoundException;
import com.demo.Interface.Cart.CartItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)                 //This annotation is used to enable Mockito extensions for JUnit 5.
class CartItemControllerTest 
{
    @Mock
    private CartItemService cartItemService;        //Mocking the CartItemService to simulate the service layer.

    @InjectMocks
    private CartItemController cartItemController;  //Injecting the mock into the controller to test its behavior.

    @Test
    void createCartItem_Positive() 
    {
        //Creating a CartItemDTO instance for a successful test case
        CartItemDTO inputDTO = CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build();

        //Mocking the behavior of cartItemService.createCartItem to return the inputDTO
        when(cartItemService.createCartItem(inputDTO)).thenReturn(inputDTO);

        //Calling the controller method to test its behavior
        ResponseEntity<CartItemDTO> response = cartItemController.createCartItem(inputDTO);

        //Asserting that the status code is 201 CREATED and the response body is the same as the input DTO
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inputDTO, response.getBody());
    }

    @Test
    void createCartItem_Negative() 
    {
        //Setting up the DTO and mocking the service to throw CartItemNotFoundException
        CartItemDTO inputDTO = CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build();
        when(cartItemService.createCartItem(inputDTO)).thenThrow(new CartItemNotFoundException(1L));

        //Calling the controller method and expecting a 404 NOT_FOUND status
        ResponseEntity<CartItemDTO> response = cartItemController.createCartItem(inputDTO);

        //Verifying that the response returns the NOT_FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCartItem_Positive() 
    {
        //Creating a CartItemDTO instance for the successful test case
        CartItemDTO dto = CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build();

        //Mocking the cartItemService to return the created CartItemDTO when called with ID 1
        when(cartItemService.getCartItem(1L)).thenReturn(dto);

        //Calling the controller method and getting the response
        ResponseEntity<CartItemDTO> response = cartItemController.getCartItem(1L);

        //Asserting that the response contains the correct status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getCartItem_Negative() 
    {
        //Mocking cartItemService to throw CartItemNotFoundException when called with ID 1
        when(cartItemService.getCartItem(1L)).thenThrow(new CartItemNotFoundException(1L));

        //Calling the controller method expecting a 404 NOT_FOUND status
        ResponseEntity<CartItemDTO> response = cartItemController.getCartItem(1L);

        //Asserting that the response returns the NOT_FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getCartItems_Positive() 
    {
        //Creating a list of CartItemDTO instances for the positive test case
        List<CartItemDTO> list = Arrays.asList
        (
                CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build(),
                CartItemDTO.builder().ciid(2L).cid(101L).iid(201L).build()
        );
        //Mocking the service to return the list when getCartItems is called
        when(cartItemService.getCartItems()).thenReturn(list);

        //Calling the controller method and getting the response
        ResponseEntity<List<CartItemDTO>> response = cartItemController.getCartItems();

        //Asserting that the status code is OK and the list size matches the expected value
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateCartItem_Positive() 
    {
        //Creating a CartItemDTO instance for updating the cart item
        CartItemDTO dto = CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build();

        //Mocking the service to return the updated DTO when updateCartItem is called
        when(cartItemService.updateCartItem(1L, dto)).thenReturn(dto);

        //Calling the controller method to update the cart item
        ResponseEntity<CartItemDTO> response = cartItemController.updateCartItem(1L, dto);

        //Asserting that the status code is OK and the response body matches the updated DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateCartItem_Negative() 
    {
        //Creating a CartItemDTO instance for updating the cart item
        CartItemDTO dto = CartItemDTO.builder().ciid(1L).cid(100L).iid(200L).build();

        //Mocking the service to throw CartItemNotFoundException when updateCartItem is called
        when(cartItemService.updateCartItem(1L, dto)).thenThrow(new CartItemNotFoundException(1L));

        //Calling the controller method expecting a 404 NOT_FOUND status
        ResponseEntity<CartItemDTO> response = cartItemController.updateCartItem(1L, dto);

        //Verifying that the response returns a NOT_FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCartItem_Positive() 
    {
        //Calling the deleteCartItem method on the controller
        ResponseEntity<Void> response = cartItemController.deleteCartItem(1L);

        //Asserting that the status code is NO_CONTENT and the service method was called exactly once
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartItemService, times(1)).deleteCartItem(1L); //Verifying the interaction with the mock service
    }

    @Test
    void deleteCartItem_Negative() 
    {
        //Mocking the service to throw CartItemNotFoundException when deleteCartItem is called
        doThrow(new CartItemNotFoundException(1L)).when(cartItemService).deleteCartItem(1L);

        //Calling the controller method expecting a 404 NOT_FOUND status
        ResponseEntity<Void> response = cartItemController.deleteCartItem(1L);

        //Verifying that the response returns a NOT_FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}