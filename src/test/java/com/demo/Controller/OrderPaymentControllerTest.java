package com.demo.Controller;
import com.demo.DTO.OrderPaymentDTO;
import com.demo.Interface.OrderPaymentService;
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

@ExtendWith(MockitoExtension.class)
public class OrderPaymentControllerTest 
{
    @Mock
    private OrderPaymentService orderPaymentService;        //Mock service layer

    @InjectMocks
    private OrderPaymentController orderPaymentController;  //Controller to test

    private OrderPaymentDTO orderPaymentDTO;

    @BeforeEach
    public void setUp() 
    {
        orderPaymentDTO = OrderPaymentDTO.builder().poid(1L).oid(1L).pid(2L).build();
    }

    @Test   //Positive Test Case: Create Order Payment
    public void testCreateOrderPayment_Success() 
    {
        //Arrange
        when(orderPaymentService.createOrderPayment(any(OrderPaymentDTO.class))).thenReturn(orderPaymentDTO);

        //Act
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.createOrderPayment(orderPaymentDTO);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());
    }

    @Test   //Negative Test Case: Create Order Payment (Service Throws Exception)
    public void testCreateOrderPayment_Failure() 
    {
        //Arrange
        when(orderPaymentService.createOrderPayment(any(OrderPaymentDTO.class))).thenThrow(new RuntimeException("Service unavailable"));

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.createOrderPayment(orderPaymentDTO));
        assertEquals("Service unavailable", exception.getMessage());
    }

    @Test   //Positive Test Case: Get Order Payment by POID
    public void testGetOrderPayment_Success() 
    {
        //Arrange
        when(orderPaymentService.getOrderPayment(1L)).thenReturn(orderPaymentDTO);

        //Act
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.getOrderPayment(1L);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());
    }

    @Test   //Negative Test Case: Get Order Payment by POID (Order Payment Not Found)
    public void testGetOrderPayment_NotFound() 
    {
        //Arrange
        when(orderPaymentService.getOrderPayment(1L)).thenThrow(new RuntimeException("OrderPayment not found"));

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.getOrderPayment(1L));
        assertEquals("OrderPayment not found", exception.getMessage());
    }
    
    @Test   //Positive Test Case: Get All Order Payments
    public void testGetAllOrderPayments_Success() 
    {
        //Arrange
        List<OrderPaymentDTO> orderPayments = Arrays.asList(orderPaymentDTO);
        when(orderPaymentService.getOrderPayments()).thenReturn(orderPayments);

        //Act
        ResponseEntity<List<OrderPaymentDTO>> response = orderPaymentController.getAllOrderPayments();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test   //Negative Test Case: Get All Order Payments (Service Throws Exception)
    public void testGetAllOrderPayments_Failure() 
    {
        //Arrange
        when(orderPaymentService.getOrderPayments()).thenThrow(new RuntimeException("Service unavailable"));

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.getAllOrderPayments());
        assertEquals("Service unavailable", exception.getMessage());
    }

    @Test   //Positive Test Case: Update Order Payment
    public void testUpdateOrderPayment_Success() 
    {
        //Arrange
        when(orderPaymentService.updateOrderPayment(1L, orderPaymentDTO)).thenReturn(orderPaymentDTO);

        //Act
        ResponseEntity<OrderPaymentDTO> response = orderPaymentController.updateOrderPayment(1L, orderPaymentDTO);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderPaymentDTO.getPoid(), response.getBody().getPoid());
    }

   
    @Test    //Negative Test Case: Update Order Payment (Order Payment Not Found)
    public void testUpdateOrderPayment_NotFound() 
    {
        //Arrange
        when(orderPaymentService.updateOrderPayment(1L, orderPaymentDTO)).thenThrow(new RuntimeException("OrderPayment not found"));

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.updateOrderPayment(1L, orderPaymentDTO));
        assertEquals("OrderPayment not found", exception.getMessage());
    }

    @Test   //Positive Test Case: Delete Order Payment
    public void testDeleteOrderPayment_Success() 
    {
        //Arrange
        doNothing().when(orderPaymentService).deleteOrderPayment(1L);

        //Act
        ResponseEntity<Void> response = orderPaymentController.deleteOrderPayment(1L);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Negative Test Case: Delete Order Payment (Order Payment Not Found)
    public void testDeleteOrderPayment_NotFound() 
    {
        //Arrange
        doThrow(new RuntimeException("OrderPayment not found")).when(orderPaymentService).deleteOrderPayment(1L);

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderPaymentController.deleteOrderPayment(1L));
        assertEquals("OrderPayment not found", exception.getMessage());
    }
}