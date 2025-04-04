package com.demo.Service;
import com.demo.DTO.PaymentDTO;
import com.demo.Entity.Payment;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import com.demo.Exception.CustomValidationException;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)                 //Extends with Mockito for mocking
class PaymentServiceImplTest 
{
    @Mock
    private PaymentRepository paymentRepository;    //Mock the PaymentRepository for test isolation

    @InjectMocks
    private PaymentServiceImpl paymentService;      //Inject the mocked repository into the service

    private PaymentDTO paymentDTO;
    private Payment payment;

    @BeforeEach
    void setUp() 
    {
        //Create a sample paymentDTO and payment entity for testing
        paymentDTO = PaymentDTO.builder().pid(1L).amount(new BigDecimal("100.00")).paymentMode(PaymentMode.CREDIT_CARD)
        .paymentStatus(PaymentStatus.PAID).paymentDateTime(LocalDateTime.now()).build();

        payment = Payment.builder().pid(1L).amount(new BigDecimal("100.00")).paymentMode(PaymentMode.CREDIT_CARD)
        .paymentStatus(PaymentStatus.PAID).paymentDateTime(LocalDateTime.now()).build();
    }
    
    @Test   //Positive Test Case: Test createPayment with valid data
    void testCreatePayment_Success() 
    {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment); //Mock saving payment
        PaymentDTO result = paymentService.createPayment(paymentDTO);               //Call the service method
        assertNotNull(result);                                                      //Assert that the result is not null
        assertEquals(paymentDTO.getAmount(), result.getAmount());                   //Assert amount is correct
        assertEquals(paymentDTO.getPaymentMode(), result.getPaymentMode());         //Assert payment mode is correct
        assertEquals(paymentDTO.getPaymentStatus(), result.getPaymentStatus());     //Assert payment status is correct
    }
    
    @Test   //Negative Test Case: Test createPayment with null amount (validation failure)
    void testCreatePayment_Failure_AmountNull() 
    {
        paymentDTO.setAmount(null);                                         //Set amount to null

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> 
        {
            paymentService.createPayment(paymentDTO);                               //Call the service method and expect an exception
        });

        assertEquals("Amount is mandatory", exception.getMessage());    //Assert exception message
    }

    @Test   //Negative Test Case: Test createPayment with non-positive amount (validation failure)
    void testCreatePayment_Failure_AmountZero() 
    {
        paymentDTO.setAmount(BigDecimal.ZERO);                                              //Set amount to zero

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> 
        {
            paymentService.createPayment(paymentDTO);                                       //Call the service method and expect an exception
        });

        assertEquals("Amount must be greater than zero.", exception.getMessage());  //Assert exception message
    }

    @Test   //Negative Test Case: Test createPayment with null payment mode (validation failure)
    void testCreatePayment_Failure_PaymentModeNull() 
    {
        paymentDTO.setPaymentMode(null);                                //Set payment mode to null

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> 
        {
            paymentService.createPayment(paymentDTO);                               //Call the service method and expect an exception
        });

        assertEquals("Payment mode is mandatory", exception.getMessage());  //Assert exception message
    }

    @Test   //Positive Test Case: Test getPayment by ID
    void testGetPayment_Success() 
    {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));  //Mock repository to return payment
        PaymentDTO result = paymentService.getPayment(1L);                      //Call the service method
        assertNotNull(result);                                                      //Assert that the result is not null
        assertEquals(payment.getPid(), result.getPid());                            //Assert payment ID matches
        assertEquals(payment.getAmount(), result.getAmount());                      //Assert amount matches
    }

    @Test   //Negative Test Case: Test getPayment with non-existing ID (throws PaymentNotFoundException)
    void testGetPayment_Failure_NotFound() 
    {
        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());          //Mock repository to return empty

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentService.getPayment(2L);                                          //Call the service method and expect an exception
        });

        assertEquals("Payment with ID 2 not found", exception.getMessage());    //Assert exception message
    }

    @Test   //Positive Test Case: Test partialUpdatePayment with valid data
    void testPartialUpdatePayment_Success() 
    {
        PaymentDTO updatedPaymentDTO = PaymentDTO.builder().pid(1L).amount(new BigDecimal("150.00"))
        .paymentMode(PaymentMode.DEBIT_CARD).paymentStatus(PaymentStatus.PENDING).build();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));          //Mock repository to return payment
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);           //Mock saving payment
        PaymentDTO result = paymentService.partialUpdatePayment(1L, updatedPaymentDTO); //Call the service method
        assertNotNull(result);                                                              //Assert that the result is not null
        assertEquals(updatedPaymentDTO.getAmount(), result.getAmount());                    //Assert updated amount matches
        assertEquals(updatedPaymentDTO.getPaymentMode(), result.getPaymentMode());          //Assert updated payment mode matches
        assertEquals(updatedPaymentDTO.getPaymentStatus(), result.getPaymentStatus());      //Assert updated payment status matches
    }

    @Test   //Negative Test Case: Test partialUpdatePayment with non-existing ID (throws PaymentNotFoundException)
    void testPartialUpdatePayment_Failure_NotFound() 
    {
        PaymentDTO updatedPaymentDTO = PaymentDTO.builder().pid(2L).amount(new BigDecimal("150.00"))
        .paymentMode(PaymentMode.DEBIT_CARD).paymentStatus(PaymentStatus.PENDING).build();

        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());          //Mock repository to return empty

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentService.partialUpdatePayment(2L, updatedPaymentDTO);             //Call the service method and expect an exception
        });

        assertEquals("Payment with ID 2 not found", exception.getMessage());    //Assert exception message
    }

    @Test   //Positive Test Case: Test deletePayment with existing ID
    void testDeletePayment_Success() 
    {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));      //Mock repository to return payment
        paymentService.deletePayment(1L);                                           //Call the service method
        verify(paymentRepository, times(1)).delete(payment);    //Verify the delete method is called once
    }

    @Test   //Negative Test Case: Test deletePayment with non-existing ID (throws PaymentNotFoundException)
    void testDeletePayment_Failure_NotFound() 
    {
        when(paymentRepository.findById(2L)).thenReturn(Optional.empty());          //Mock repository to return empty

        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, () -> 
        {
            paymentService.deletePayment(2L);                                       //Call the service method and expect an exception
        });

        assertEquals("Payment with ID 2 not found", exception.getMessage());    //Assert exception message
    }
}