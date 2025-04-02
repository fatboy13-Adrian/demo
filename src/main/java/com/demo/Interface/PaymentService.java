package com.demo.Interface;

import java.math.BigDecimal;
import java.util.List;

import com.demo.DTO.PaymentDTO;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;

public interface PaymentService 
{
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPayment(Long pid);
    List<PaymentDTO> getPayments();
    PaymentDTO partialUpdatePayment(Long pid, BigDecimal amount, PaymentMode paymentMode, PaymentStatus paymentStatus, PaymentDTO paymentDTO);
    void deletePayment(Long pid);
}