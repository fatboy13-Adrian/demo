package com.demo.Controller.Order;
import com.demo.DTO.Order.OrderDTO;
import com.demo.Enum.Order.OrderStatus;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Interface.Order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)             //Tells JUnit to use MockitoExtension for mock creation
public class OrderControllerTest 
{
    @Mock
    private OrderService orderService;          //Mocking OrderService to isolate controller testing

    @InjectMocks
    private OrderController orderController;    //Inject the mocked OrderService into the controller

    private OrderDTO sampleOrder;

    @BeforeEach
    void setUp() 
    {
        sampleOrder = createSampleOrder();  //Setting up a sample order object before each test
    }

    //Helper method to create a sample OrderDTO
    private OrderDTO createSampleOrder() 
    {
        return OrderDTO.builder().oid(1L).totalPrice(new BigDecimal("100.50")).orderStatus(OrderStatus.PROCESSING).build();
    }

    @Test
    void testCreateOrder_Success() 
    {
        //Arrange: Mock the service call to return the sample order when createOrder is called
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(sampleOrder);

        //Act: Simulate a POST request to create an order
        ResponseEntity<OrderDTO> response = orderController.createOrder(sampleOrder);

        //Assert: Check that the response status is CREATED (201) and the order matches the sample order
        assertResponse(response, HttpStatus.CREATED, sampleOrder);
    }

    @Test
    void testGetOrder_Success() 
    {
        //Arrange: Mock the service call to return the sample order for the given order ID
        when(orderService.getOrder(1L)).thenReturn(sampleOrder);

        //Act: Simulate a GET request to fetch an order by its ID
        ResponseEntity<OrderDTO> response = orderController.getOrder(1L);

        //Assert: Verify the response status is OK (200) and the order matches the expected one
        assertResponse(response, HttpStatus.OK, sampleOrder);
    }

    @Test
    void testGetOrder_NotFound() 
    {
        //Arrange: Mock the service to throw OrderNotFoundException when an invalid order ID is used
        when(orderService.getOrder(2L)).thenThrow(new OrderNotFoundException(2L));

        //Act: Simulate a GET request with an invalid order ID
        ResponseEntity<OrderDTO> response = orderController.getOrder(2L);

        //Assert: Check that the response status is NOT_FOUND (404) and body is null
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetOrders_Success() 
    {
        //Arrange: Mock the service call to return a list containing the sample order
        List<OrderDTO> orders = Arrays.asList(sampleOrder);
        when(orderService.getOrders()).thenReturn(orders);

        //Act: Simulate a GET request to fetch all orders
        ResponseEntity<List<OrderDTO>> response = orderController.getOrders();

        //Assert: Verify the response status is OK (200) and the list contains one order
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testPartialUpdateOrder_Success() 
    {
        //Arrange: Create an updated order object and mock the service call to return the updated order
        OrderDTO updatedOrder = createUpdatedOrder();
        when(orderService.partialUpdateOrder(1L, updatedOrder)).thenReturn(updatedOrder);

        //Act: Simulate a PATCH request to partially update an existing order
        ResponseEntity<OrderDTO> response = orderController.partialUpdateOrder(1L, updatedOrder);

        //Assert: Verify the response status is OK (200) and the updated order is returned
        assertResponse(response, HttpStatus.OK, updatedOrder);
    }

    @Test
    void testPartialUpdateOrder_NotFound() 
    {
        //Arrange: Mock the service call to throw OrderNotFoundException for update of an invalid order ID
        when(orderService.partialUpdateOrder(2L, sampleOrder)).thenThrow(new OrderNotFoundException(2L));

        //Act: Simulate a PATCH request to partially update an invalid order
        ResponseEntity<OrderDTO> response = orderController.partialUpdateOrder(2L, sampleOrder);

        //Assert: Verify the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteOrder_Success() 
    {
        //Arrange: Mock the service call to delete the order (no return value)
        doNothing().when(orderService).deleteOrder(1L);

        //Act: Simulate a DELETE request to remove the order
        ResponseEntity<Void> response = orderController.deleteOrder(1L);

        //Assert: Verify the response status is NO_CONTENT (204)
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteOrder_NotFound() 
    {
        //Arrange: Mock the service call to throw OrderNotFoundException for deletion of an invalid order ID
        doThrow(new OrderNotFoundException(2L)).when(orderService).deleteOrder(2L);

        //Act: Simulate a DELETE request with an invalid order ID
        ResponseEntity<Void> response = orderController.deleteOrder(2L);

        //Assert: Verify the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //Helper method to assert responses for OrderDTO
    private void assertResponse(ResponseEntity<OrderDTO> response, HttpStatus expectedStatus, OrderDTO expectedOrder) 
    {
        assertNotNull(response.getBody());                                                  //Ensure the response body is not null
        assertEquals(expectedStatus, response.getStatusCode());                             //Verify the status code matches the expected status
        assertEquals(expectedOrder.getOid(), response.getBody().getOid());                  //Check if order IDs match
        assertEquals(expectedOrder.getTotalPrice(), response.getBody().getTotalPrice());    //Check if total prices match
        assertEquals(expectedOrder.getOrderStatus(), response.getBody().getOrderStatus());  //Check if statuses match
    }

    //Helper method to create an updated OrderDTO
    private OrderDTO createUpdatedOrder() 
    {
        return OrderDTO.builder().oid(1L).totalPrice(new BigDecimal("150.75")).orderStatus(OrderStatus.SHIPPED).build(); 
    }
}