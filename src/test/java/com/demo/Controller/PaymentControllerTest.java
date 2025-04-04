package com.demo.Controller;
import com.demo.DTO.PaymentDTO;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import com.demo.Exception.CustomValidationException;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)                 //This annotation ensures Mockito is used to mock dependencies
class PaymentControllerTest 
{
    @Mock
    private PaymentServiceImpl paymentService;      //Mocking the PaymentServiceImpl to isolate the controller for testing

    @InjectMocks
    private PaymentController paymentController;    //Injecting the mocked service into the controller

    private PaymentDTO paymentDTO;                  //A PaymentDTO instance to be used in tests

    @BeforeEach
    void setUp() 
    {
        //Initialize a PaymentDTO with sample data before each test
        paymentDTO = PaymentDTO.builder().pid(1L).amount(new BigDecimal("100.00"))
        .paymentMode(PaymentMode.CREDIT_CARD).paymentStatus(PaymentStatus.PAID).build();
    }

    @Test   //Positive test case: Creating a payment successfully
    void testCreatePayment_Success() 
    {
        //Arrange: Mock the paymentService to return the paymentDTO when createPayment is called
        when(paymentService.createPayment(paymentDTO)).thenReturn(paymentDTO);

        //Act: Call the controller's createPayment method
        ResponseEntity<PaymentDTO> response = paymentController.createPayment(paymentDTO);

        //Assert: Check that the response is OK and the body is the expected paymentDTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDTO, response.getBody());
    }

    @Test   //Negative test case: Creating a payment with invalid data (amount is null)
    void testCreatePayment_Failure_InvalidData() 
    {
        //Arrange: Create a paymentDTO without an amount, which is invalid
        PaymentDTO invalidPaymentDTO = PaymentDTO.builder().paymentMode(PaymentMode.CREDIT_CARD)
        .paymentStatus(PaymentStatus.PAID).build();

        //Mock the paymentService to throw a validation exception when invalidPaymentDTO is passed
        when(paymentService.createPayment(invalidPaymentDTO)).thenThrow(new CustomValidationException("Amount is mandatory"));

        //Act & Assert: Expect a CustomValidationException when trying to create the payment
        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> 
        {
            paymentController.createPayment(invalidPaymentDTO);
        });

        //Assert: Verify the exception message
        assertEquals("Amount is mandatory", exception.getMessage());
    }

    @Test   //Positive test case: Retrieve payment by ID successfully
    void testGetPayment_Success() 
    {
        //Arrange: Mock the paymentService to return the paymentDTO when getPayment is called
        when(paymentService.getPayment(1L)).thenReturn(paymentDTO);

        //Act: Call the controller's getPayment method
        ResponseEntity<PaymentDTO> response = paymentController.getPayment(1L);

        //Assert: Check that the response is OK and the body contains the expected paymentDTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDTO, response.getBody());
    }

    @Test   //Negative test case: Retrieve payment by ID, but payment is not found
    void testGetPayment_Failure_NotFound() 
    {
        //Arrange: Mock the paymentService to throw PaymentNotFoundException when the payment ID is not found
        when(paymentService.getPayment(1L)).thenThrow(new PaymentNotFoundException(1L));

        //Act & Assert: Expect a PaymentNotFoundException when trying to retrieve the payment
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentController.getPayment(1L);
        });

        //Assert: Verify the exception message
        assertEquals("Payment with ID 1 not found", exception.getMessage());
    }
    
    @Test   //Positive test case: Get all payments successfully
    void testGetPayments_Success() 
    {
        //Arrange: Mock the paymentService to return a list containing paymentDTO
        when(paymentService.getPayments()).thenReturn(Collections.singletonList(paymentDTO));

        //Act: Call the controller's getPayments method
        ResponseEntity<List<PaymentDTO>> response = paymentController.getPayments();

        //Assert: Check that the response is OK and contains exactly one paymentDTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(paymentDTO, response.getBody().get(0));
    }

    @Test   //Negative test case: No payments available (empty list)
    void testGetPayments_Failure_NoPayments() 
    {
        //Arrange: Mock the paymentService to return an empty list
        when(paymentService.getPayments()).thenReturn(Collections.emptyList());

        //Act: Call the controller's getPayments method
        ResponseEntity<List<PaymentDTO>> response = paymentController.getPayments();

        //Assert: Check that the response is OK and the body is an empty list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test   //Positive test case: Partially update payment successfully
    void testPartialUpdatePayment_Success() 
    {
        //Arrange: Create an updated PaymentDTO
        PaymentDTO updatedPaymentDTO = PaymentDTO.builder().pid(1L).amount(new BigDecimal("120.00"))
        .paymentMode(PaymentMode.DEBIT_CARD).paymentStatus(PaymentStatus.PENDING).build();

        //Mock the paymentService to return the updated paymentDTO when partialUpdatePayment is called
        when(paymentService.partialUpdatePayment(1L, updatedPaymentDTO)).thenReturn(updatedPaymentDTO);

        //Act: Call the controller's partialUpdatePayment method
        ResponseEntity<PaymentDTO> response = paymentController.partialUpdatePayment(1L, updatedPaymentDTO);

        //Assert: Check that the response is OK and the body contains the updated paymentDTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPaymentDTO, response.getBody());
    }

    @Test   //Negative test case: Partially update payment with payment not found
    void testPartialUpdatePayment_Failure_NotFound() 
    {
        //Arrange: Create an updated PaymentDTO
        PaymentDTO updatedPaymentDTO = PaymentDTO.builder().pid(1L).amount(new BigDecimal("120.00"))
        .paymentMode(PaymentMode.DEBIT_CARD).paymentStatus(PaymentStatus.PAID).build();

        //Mock the paymentService to throw PaymentNotFoundException when partialUpdatePayment is called
        when(paymentService.partialUpdatePayment(1L, updatedPaymentDTO)).thenThrow(new PaymentNotFoundException(1L));

        //Act & Assert: Expect a PaymentNotFoundException when trying to update the payment
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentController.partialUpdatePayment(1L, updatedPaymentDTO);
        });

        //Assert: Verify the exception message
        assertEquals("Payment with ID 1 not found", exception.getMessage());
    }

    @Test   //Positive test case: Successfully delete a payment
    void testDeletePayment_Success() 
    {
        //Arrange: Mock the paymentService to do nothing when deletePayment is called
        doNothing().when(paymentService).deletePayment(1L);

        //Act: Call the controller's deletePayment method
        ResponseEntity<Void> response = paymentController.deletePayment(1L);

        //Assert: Check that the response status is NO_CONTENT indicating successful deletion
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Negative test case: Delete payment that does not exist
    void testDeletePayment_Failure_NotFound() 
    {
        //Arrange: Mock the paymentService to throw PaymentNotFoundException when deletePayment is called
        doThrow(new PaymentNotFoundException(1L)).when(paymentService).deletePayment(1L);

        //Act & Assert: Expect a PaymentNotFoundException when trying to delete the payment
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentController.deletePayment(1L);
        });

        //Assert: Verify the exception message
        assertEquals("Payment with ID 1 not found", exception.getMessage());
    }
}