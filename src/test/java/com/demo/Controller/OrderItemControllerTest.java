package com.demo.Controller;
import com.demo.DTO.OrderItemDTO;
import com.demo.Interface.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)                     //Using MockitoExtension to enable Mockito annotations in tests
class OrderItemControllerTest 
{
    @Mock
    private OrderItemService orderItemService;          //Mocking the OrderItemService for test scenarios

    @InjectMocks
    private OrderItemController orderItemController;    //Injecting the mocked service into the controller

    private OrderItemDTO orderItemDTO;                  //DTO to use for test cases

    @BeforeEach
    void setUp() 
    {
        //Initialize OrderItemDTO for testing before each test
        orderItemDTO = OrderItemDTO.builder().oiid(1L).oid(1L)
        .iid(2L).build();
    }

    @Test   //Positive test case: Successfully create OrderItem
    void testCreateOrderItem_success() 
    {
        //Given: Simulate the creation of OrderItem and return the same DTO
        when(orderItemService.createOrderItem(any(OrderItemDTO.class))).thenReturn(orderItemDTO);

        //When: Call the controller method
        ResponseEntity<OrderItemDTO> response = orderItemController.createOrderItem(orderItemDTO);

        //Then: Assert that the response is not null, status is OK, and body matches expected DTO
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());              //Expect status code OK (200)
        assertEquals(orderItemDTO.getOiid(), response.getBody().getOiid()); //Check if the IDs match
        assertEquals(orderItemDTO.getOid(), response.getBody().getOid());   //Check if order IDs match
        assertEquals(orderItemDTO.getIid(), response.getBody().getIid());   //Check if item IDs match
    }

    @Test   //Negative test case: Failed to create OrderItem due to service failure
    void testCreateOrderItem_failure() 
    {
        //Given: Simulate service failure by throwing an exception
        when(orderItemService.createOrderItem(any(OrderItemDTO.class))).thenThrow(new RuntimeException("Service failure"));

        //When & Then: Call controller and verify that exception is thrown
        try 
        {
            orderItemController.createOrderItem(orderItemDTO);
            fail("Exception expected");                         //Fail the test if no exception is thrown
        } 
        
        catch (Exception e) 
        {
            assertTrue(e instanceof RuntimeException);                  //Assert that the exception is a RuntimeException
            assertEquals("Service failure", e.getMessage());    //Verify exception message
        }
    }

    @Test   //Positive test case: Successfully get an OrderItem by ID
    void testGetOrderItem_success() 
    {
        //Given: Simulate getting an order item from service
        when(orderItemService.getOrderItem(1L)).thenReturn(orderItemDTO);

        //When: Call the controller method
        ResponseEntity<OrderItemDTO> response = orderItemController.getOrderItem(1L);

        //Then: Assert that response is not null, status is OK, and body matches expected DTO
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());              //Expect status code OK (200)
        assertEquals(orderItemDTO.getOiid(), response.getBody().getOiid()); //Check if order item ID matches
    }
    
    @Test   //Negative test case: OrderItem not found
    void testGetOrderItem_notFound() 
    {
        //Given: Simulate order item not found by returning null from service
        when(orderItemService.getOrderItem(1L)).thenReturn(null);   //Simulate not found

        //When: Call the controller method
        ResponseEntity<OrderItemDTO> response = orderItemController.getOrderItem(1L);

        //Then: Assert that response is not null, status is NOT_FOUND (404)
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());           //Expect status code Not Found (404)
    }

    @Test   //Positive test case: Successfully get all OrderItems
    void testGetAllOrderItems_success() 
    {
        //Given: Simulate getting all order items from service
        when(orderItemService.getOrderItems()).thenReturn(Arrays.asList(orderItemDTO));

        //When: Call the controller method
        ResponseEntity<List<OrderItemDTO>> response = orderItemController.getAllOrderItems();

        //Then: Assert that response is not null, status is OK, and list size is 1
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());  //Expect status code OK (200)
        assertEquals(1, response.getBody().size());     //Size of the list should be 1
    }

    @Test   //Negative test case: No OrderItems available
    void testGetAllOrderItems_empty() 
    {
        //Given: Simulate no order items available by returning an empty list from service
        when(orderItemService.getOrderItems()).thenReturn(Arrays.asList());  //Empty list

        //When: Call the controller method
        ResponseEntity<List<OrderItemDTO>> response = orderItemController.getAllOrderItems();

        //Then: Assert that response is not null, status is OK, and list is empty
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());  //Expect status code OK (200)
        assertTrue(response.getBody().isEmpty());               //The list should be empty
    }

    @Test   //Positive test case: Successfully update an OrderItem
    void testUpdateOrderItem_success() 
    {
        //Given: Simulate updating an order item with new values
        OrderItemDTO updatedDTO = OrderItemDTO.builder().oiid(1L).oid(2L).iid(3L).build();
        when(orderItemService.updateOrderItem(eq(1L), any(OrderItemDTO.class))).thenReturn(updatedDTO);

        //When: Call the controller method
        ResponseEntity<OrderItemDTO> response = orderItemController.updateOrderItem(1L, updatedDTO);

        //Then: Assert that response is not null, status is OK, and body matches updated DTO
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());              //Expect status code OK (200)
        assertEquals(updatedDTO.getOiid(), response.getBody().getOiid());   //Check if updated order item ID matches
        assertEquals(updatedDTO.getOid(), response.getBody().getOid());     //Check if updated order ID matches
        assertEquals(updatedDTO.getIid(), response.getBody().getIid());     //Check if updated item ID matches
    }

    @Test   //Negative test case: Failed to update OrderItem due to non-existing ID
    void testUpdateOrderItem_notFound() 
    {
        //Given: Simulate updating a non-existing order item by returning null
        OrderItemDTO updatedDTO = OrderItemDTO.builder().oiid(1L).oid(2L).iid(3L).build();
        when(orderItemService.updateOrderItem(eq(1L), any(OrderItemDTO.class))).thenReturn(null);   //Simulate not found

        //When: Call the controller method
        ResponseEntity<OrderItemDTO> response = orderItemController.updateOrderItem(1L, updatedDTO);

        //Then: Assert that response is not null, status is NOT_FOUND (404)
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());   //Expect status code Not Found (404)
    }

    @Test   //Positive test case: Successfully delete an OrderItem
    void testDeleteOrderItem_success() 
    {
        //Given: Simulate successful deletion of order item
        doNothing().when(orderItemService).deleteOrderItem(1L);

        //When: Call the controller method
        ResponseEntity<Void> response = orderItemController.deleteOrderItem(1L);

        //Then: Assert that response is not null, status is NO_CONTENT (204)
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  //Expect status code No Content (204)
    }

    @Test   //Negative test case: Failed to delete an OrderItem due to service error
    void testDeleteOrderItem_failure() 
    {
        //Given: Simulate service error during deletion by throwing an exception
        doThrow(new RuntimeException("Service error")).when(orderItemService).deleteOrderItem(1L);

        //When & Then: Call controller and verify that exception is thrown
        try 
        {
            orderItemController.deleteOrderItem(1L);
            fail("Exception expected");                     //Fail the test if no exception is thrown
        } 
        
        catch (Exception e) 
        {
            assertTrue(e instanceof RuntimeException);              //Assert that the exception is a RuntimeException
            assertEquals("Service error", e.getMessage());  //Verify exception message
        }
    }
}