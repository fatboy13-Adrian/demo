package com.demo.Interface;
import java.util.List;
import com.demo.DTO.PaymentDTO;

public interface PaymentService 
{
    PaymentDTO createPayment(PaymentDTO paymentDTO);                    //Method to create a new payment.
    PaymentDTO getPayment(Long pid);                                    //Method to get a specific payment by its ID (pid). 
    List<PaymentDTO> getPayments();                                     //Method to get all payments. 
    PaymentDTO partialUpdatePayment(Long pid, PaymentDTO paymentDTO);   //Method to partially update an existing payment. 
    void deletePayment(Long pid);                                       //Method to delete a payment by its ID (pid).
}