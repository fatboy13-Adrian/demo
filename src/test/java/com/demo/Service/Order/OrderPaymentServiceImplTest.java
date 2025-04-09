package com.demo.Service.Order;
import com.demo.Entity.Order.Order;
import com.demo.Entity.Order.OrderPayment;
import com.demo.Entity.Payment.Payment;
import com.demo.DTO.Order.OrderPaymentDTO;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Exception.Order.OrderPaymentNotFoundException;
import com.demo.Repository.Order.OrderPaymentRepository;
import com.demo.Repository.Payment.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Use Mockito extension for JUnit 5
class OrderPaymentServiceImplTest 
{
    @Mock
    private OrderPaymentRepository orderPaymentRepository;  //Mock the OrderPayment repository

    @Mock
    private PaymentRepository paymentRepository;            //Mock the Payment repository

    @InjectMocks
    private OrderPaymentServiceImpl orderPaymentService;    //Inject mocks into the service being tested

    private Payment payment;
    private Order order;
    private OrderPaymentDTO validOrderPaymentDTO;

    //Setup method to initialize test data
    @BeforeEach
    void setUp() 
    {
        payment = new Payment();                        //Create a new Payment object
        payment.setPid(1L);                         //Set Payment ID to 1L
        order = new Order();                            //Create a new Order object
        order.setOid(1L);                           //Set Order ID to 1L
        validOrderPaymentDTO = new OrderPaymentDTO();   //Create a valid OrderPaymentDTO object
        validOrderPaymentDTO.setOid(1L);            //Set Order ID to 1L
        validOrderPaymentDTO.setPid(1L);            //Set Payment ID to 1L
    }

    @Test   //Test case to verify successful creation of OrderPayment
    void testCreateOrderPayment_success() 
    {
        //Arrange: Mock the behavior of the payment repository and order payment repository
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));                      //Return a valid payment when queried
        when(orderPaymentRepository.save(any(OrderPayment.class))).thenReturn(new OrderPayment());  //Simulate saving an OrderPayment entity

        //Act: Call the method to test
        OrderPaymentDTO result = orderPaymentService.createOrderPayment(validOrderPaymentDTO);

        //Assert: Verify that the result is not null and has the correct Order ID and Payment ID
        assertNotNull(result);
        assertEquals(1L, result.getOid());                                                          //Check if the Order ID is correct
        assertEquals(1L, result.getPid());                                                          //Check if the Payment ID is correct
        verify(paymentRepository, times(1)).findById(1L);                       //Verify that the paymentRepository was queried once
        verify(orderPaymentRepository, times(1)).save(any(OrderPayment.class)); //Verify that the orderPaymentRepository was used once
    }

    @Test   //Test case to handle PaymentNotFoundException during OrderPayment creation
    void testCreateOrderPayment_paymentNotFound() 
    {
        //Arrange: Mock the behavior to return an empty payment from the repository
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert: Verify that the exception is thrown when attempting to create an OrderPayment
        PaymentNotFoundException thrown = assertThrows(PaymentNotFoundException.class, () -> 
        {
            orderPaymentService.createOrderPayment(validOrderPaymentDTO);
        });
        assertEquals("Payment with ID 1 not found", thrown.getMessage());   //Assert that the exception message is as expected
    }

    @Test   //Test case to verify successful retrieval of an OrderPayment by POID
    void testGetOrderPayment_success() 
    {
        //Arrange: Create a valid OrderPayment and mock repository behavior
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setPoid(1L);
        orderPayment.setOid(order);
        orderPayment.setPid(payment);

        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.of(orderPayment));    //Mock repository to return the OrderPayment
        when(paymentRepository.findById(payment.getPid())).thenReturn(Optional.of(payment));    //Mock the payment retrieval

        //Act: Call the method to test
        OrderPaymentDTO result = orderPaymentService.getOrderPayment(1L);

        //Assert: Verify that the result is not null and has the expected POID, Order ID, and Payment ID
        assertNotNull(result);
        assertEquals(1L, result.getPoid());
        assertEquals(1L, result.getOid());
        assertEquals(1L, result.getPid());
        verify(orderPaymentRepository, times(1)).findById(1L);  //Verify that the repository was queried once
    }

    @Test   //Test case to handle OrderPaymentNotFoundException when the OrderPayment is not found
    void testGetOrderPayment_orderPaymentNotFound() 
    {
        //Arrange: Mock the behavior to return an empty OrderPayment from the repository
        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert: Verify that the exception is thrown when the OrderPayment is not found
        OrderPaymentNotFoundException thrown = assertThrows(OrderPaymentNotFoundException.class, () -> 
        {
            orderPaymentService.getOrderPayment(1L);
        });
        assertEquals("Order payment with ID 1 not found", thrown.getMessage()); //Assert the exception message
    }

    @Test   //Test case to verify successful update of an OrderPayment
    void testUpdateOrderPayment_success() 
    {
        //Arrange: Create an existing OrderPayment to be updated and mock repository behaviors
        OrderPayment existingOrderPayment = new OrderPayment();
        existingOrderPayment.setPoid(1L);
        existingOrderPayment.setOid(order);
        existingOrderPayment.setPid(payment);

        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.of(existingOrderPayment));        //Return existing OrderPayment
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));                          //Mock the payment retrieval
        when(orderPaymentRepository.save(any(OrderPayment.class))).thenReturn(existingOrderPayment);    //Mock saving the updated OrderPayment

        //Act: Call the method to test
        OrderPaymentDTO result = orderPaymentService.updateOrderPayment(1L, validOrderPaymentDTO);

        //Assert: Verify the updated result has the expected POID, Order ID, and Payment ID
        assertNotNull(result);
        assertEquals(1L, result.getPoid());
        assertEquals(1L, result.getOid());
        assertEquals(1L, result.getPid());
        verify(orderPaymentRepository, times(1)).findById(1L);                  //Verify repository access
        verify(orderPaymentRepository, times(1)).save(any(OrderPayment.class)); //Verify save operation
    }

    @Test   //Test case to handle PaymentNotFoundException during OrderPayment update
    void testUpdateOrderPayment_paymentNotFound() 
    {
        //Arrange: Mock the behavior to return an empty payment from the repository
        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.of(new OrderPayment()));  //Return existing OrderPayment
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());                      //Return no payment

        //Act & Assert: Verify that the exception is thrown when the payment is not found
        PaymentNotFoundException thrown = assertThrows(PaymentNotFoundException.class, () -> 
        {
            orderPaymentService.updateOrderPayment(1L, validOrderPaymentDTO);
        });
        assertEquals("Payment with ID 1 not found", thrown.getMessage());   //Assert exception message
    }
    
    @Test   //Test case to verify successful deletion of an OrderPayment
    void testDeleteOrderPayment_success() 
    {
        //Arrange: Create an OrderPayment to be deleted
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setPoid(1L);

        //Return existing OrderPayment
        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.of(orderPayment)); 

        //Act: Call the method to delete the OrderPayment
        orderPaymentService.deleteOrderPayment(1L);

        //Assert: Verify that the delete method was called on the repository
        verify(orderPaymentRepository, times(1)).delete(orderPayment);
    }

    @Test   //Test case to handle OrderPaymentNotFoundException when deleting an OrderPayment
    void testDeleteOrderPayment_orderPaymentNotFound() 
    {
        //Arrange: Mock the behavior to return an empty OrderPayment from the repository
        when(orderPaymentRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert: Verify that the exception is thrown when the OrderPayment is not found
        OrderPaymentNotFoundException thrown = assertThrows(OrderPaymentNotFoundException.class, () -> 
        {
            orderPaymentService.deleteOrderPayment(1L);
        });
        assertEquals("Order payment with ID 1 not found", thrown.getMessage()); //Assert exception message
    }
    
    @Test   //Test case to handle invalid OrderPaymentDTO during creation
    void testCreateOrderPayment_invalidOrderPaymentDTO() 
    {
        //Arrange: Create an invalid OrderPaymentDTO with null Order ID and Payment ID
        OrderPaymentDTO invalidDTO = new OrderPaymentDTO();
        invalidDTO.setOid(null);
        invalidDTO.setPid(null);

        //Act & Assert: Verify that the exception is thrown for invalid DTO
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> 
        {
            orderPaymentService.createOrderPayment(invalidDTO);
        });
        assertEquals("Invalid Order ID", thrown.getMessage());  //Assert the exception message for invalid Order ID
    }
}