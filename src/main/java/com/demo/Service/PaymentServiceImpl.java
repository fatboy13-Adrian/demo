package com.demo.Service;

import com.demo.Interface.PaymentService;
import com.demo.Repository.PaymentRepository;
import com.demo.DTO.PaymentDTO;
import com.demo.Entity.Payment;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import com.demo.Exception.CustomValidationException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        validatePaymentDTO(paymentDTO);
        Payment payment = convertToEntity(paymentDTO);
        return convertToDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO getPayment(Long pid) {
        return convertToDTO(findPaymentById(pid));
    }

    @Override
    public List<PaymentDTO> getPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDTO partialUpdatePayment(Long pid, BigDecimal amount, PaymentMode paymentMode, PaymentStatus paymentStatus, PaymentDTO paymentDTO) {
        Payment existingPayment = findPaymentById(pid);

        if (amount != null) {
            validateAmount(amount);
            existingPayment.setAmount(amount);
        }
        if (paymentMode != null) {
            existingPayment.setPaymentMode(paymentMode);
        }
        if (paymentStatus != null) {
            existingPayment.setPaymentStatus(paymentStatus);
        }
        
        return convertToDTO(paymentRepository.save(existingPayment));
    }

    @Override
    public void deletePayment(Long pid) {
        Payment payment = findPaymentById(pid);
        paymentRepository.delete(payment);
    }

    private void validatePaymentDTO(PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() == null) {
            throw new CustomValidationException("Amount is mandatory");
        }
        validateAmount(paymentDTO.getAmount());
        
        if (paymentDTO.getPaymentMode() == null) {
            throw new CustomValidationException("Payment mode is mandatory");
        }
        if (paymentDTO.getPaymentStatus() == null) {
            throw new CustomValidationException("Payment status is mandatory");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomValidationException("Amount must be greater than zero.");
        }
    }

    private Payment findPaymentById(Long pid) {
        return paymentRepository.findById(pid)
                .orElseThrow(() -> new CustomValidationException("Payment with ID " + pid + " not found."));
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return PaymentDTO.builder()
                .pid(payment.getPid())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDateTime(payment.getPaymentDateTime())
                .build();
    }

    private Payment convertToEntity(PaymentDTO paymentDTO) {
        return Payment.builder()
                .amount(paymentDTO.getAmount())
                .paymentMode(paymentDTO.getPaymentMode())
                .paymentStatus(paymentDTO.getPaymentStatus())
                .paymentDateTime(paymentDTO.getPaymentDateTime() != null ? paymentDTO.getPaymentDateTime() : LocalDateTime.now())
                .build();
    }
}