package com.demo.Controller.Order;
import com.demo.DTO.Order.OrderPaymentDTO;
import com.demo.Interface.Order.OrderPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class) //This annotation allows Mockito to manage dependencies in the test class
public class OrderPaymentControllerTest 
{
    @Mock
    private OrderPaymentService orderPaymentService;        //Mocked OrderPaymentService to simulate service layer

    @InjectMocks
    private OrderPaymentController orderPaymentController;  //Inject the mocked service into the controller

    private OrderPaymentDTO orderPaymentDTO;                //The object to be used in test cases

    @BeforeEach
    public void setUp() 
    {
        //Setup the orderPaymentDTO before each test
        orderPaymentDTO = OrderPaymentDTO.builder().poid(1L).oid(1L).pid(2L).build();
    }

    @Test
    public void testCreateOrderPayment_shouldReturnCreatedStatus_whenOrderPaymentIsSuccessful() 
    {
        //Arrange: Define behavior of the mocked service
        when(orderPaymentService.createOrderPayment(any(OrderPaymentDTO.class))).thenReturn(orderPaymentDTO);

        //Act: Call the controller method to test
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.createOrderPayment(orderPaymentDTO);

        //Assert: Verify the status code and the returned object
        assertEquals(HttpStatus.CREATED, response.getStatusCode());             //Expect HTTP Created status
        assertNotNull(response.getBody());                                      //Ensure the response body is not null
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());  //Check if the Poid matches
    }

    @Test
    public void testCreateOrderPayment_shouldThrowRuntimeException_whenServiceThrowsException() 
    {
        //Arrange: Mock service to throw exception
        when(orderPaymentService.createOrderPayment(any(OrderPaymentDTO.class))).thenThrow(new RuntimeException("Service unavailable"));

        //Act & Assert: Assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.createOrderPayment(orderPaymentDTO));
        assertEquals("Service unavailable", exception.getMessage());    //Ensure the exception message is correct
    }

    @Test
    public void testGetOrderPayment_shouldReturnOrderPayment_whenFound() 
    {
        //Arrange: Mock service to return a specific order payment DTO
        when(orderPaymentService.getOrderPayment(1L)).thenReturn(orderPaymentDTO);

        //Act: Call the controller to get the order payment
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.getOrderPayment(1L);

        //Assert: Verify the response's status and data
        assertEquals(HttpStatus.OK, response.getStatusCode());                  //Expect HTTP OK status
        assertNotNull(response.getBody());                                      //Ensure the response body is not null
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());  //Check if Poid matches
    }

    @Test
    public void testGetOrderPayment_shouldThrowRuntimeException_whenOrderPaymentNotFound() 
    {
        //Arrange: Mock service to throw an exception when not found
        when(orderPaymentService.getOrderPayment(1L)).thenThrow(new RuntimeException("OrderPayment not found"));

        //Act & Assert: Assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.getOrderPayment(1L));
        assertEquals("OrderPayment not found", exception.getMessage()); //Ensure correct exception message
    }

    @Test
    public void testGetAllOrderPayments_shouldReturnListOfOrderPayments_whenServiceReturnsValidData() 
    {
        //Arrange: Mock service to return a list of order payment DTOs
        List<OrderPaymentDTO> orderPayments = Arrays.asList(orderPaymentDTO);
        when(orderPaymentService.getOrderPayments()).thenReturn(orderPayments);

        //Act: Call the controller to get all order payments
        ResponseEntity<List<OrderPaymentDTO>> response = orderPaymentController.getAllOrderPayments();

        //Assert: Verify the response status and the size of the list
        assertEquals(HttpStatus.OK, response.getStatusCode());  //Expect HTTP OK status
        assertNotNull(response.getBody());                      //Ensure the response body is not null
        assertEquals(1, response.getBody().size());     //Ensure the size is 1
    }

    @Test
    public void testGetAllOrderPayments_shouldThrowRuntimeException_whenServiceThrowsException() 
    {
        //Arrange: Mock service to throw an exception
        when(orderPaymentService.getOrderPayments()).thenThrow(new RuntimeException("Service unavailable"));

        //Act & Assert: Assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.getAllOrderPayments());
        assertEquals("Service unavailable", exception.getMessage());    //Ensure correct exception message
    }

    @Test
    public void testUpdateOrderPayment_shouldReturnUpdatedOrderPayment_whenSuccessful() 
    {
        //Arrange: Mock service to return the updated order payment DTO
        when(orderPaymentService.updateOrderPayment(1L, orderPaymentDTO)).thenReturn(orderPaymentDTO);

        //Act: Call the controller to update the order payment
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.updateOrderPayment(1L, orderPaymentDTO);

        //Assert: Verify the response status and data
        assertEquals(HttpStatus.OK, response.getStatusCode());                  //Expect HTTP OK status
        assertNotNull(response.getBody());                                      //Ensure the response body is not null
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());  //Check if Poid matches
    }

    @Test
    public void testUpdateOrderPayment_shouldThrowRuntimeException_whenOrderPaymentNotFound() 
    {
        //Arrange: Mock service to throw an exception when order payment is not found
        when(orderPaymentService.updateOrderPayment(1L, orderPaymentDTO)).thenThrow(new RuntimeException("OrderPayment not found"));

        //Act & Assert: Assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.updateOrderPayment(1L, orderPaymentDTO));
        assertEquals("OrderPayment not found", exception.getMessage()); //Ensure correct exception message
    }

    @Test
    public void testDeleteOrderPayment_shouldReturnNoContent_whenSuccessful() 
    {
        //Arrange: Mock service to perform deletion
        doNothing().when(orderPaymentService).deleteOrderPayment(1L);

        //Act: Call the controller to delete the order payment
        ResponseEntity<Void> response = orderPaymentController.deleteOrderPayment(1L);

        //Assert: Verify the response status is "No Content"
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());  //Expect HTTP No Content status
    }

    @Test
    public void testDeleteOrderPayment_shouldThrowRuntimeException_whenOrderPaymentNotFound() 
    {
        //Arrange: Mock service to throw an exception when order payment is not found
        doThrow(new RuntimeException("OrderPayment not found")).when(orderPaymentService).deleteOrderPayment(1L);

        //Act & Assert: Assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.deleteOrderPayment(1L));
        assertEquals("OrderPayment not found", exception.getMessage()); //Ensure correct exception message
    }
}