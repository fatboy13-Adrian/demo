package com.demo.Util.DataLoader.Payment;
import com.demo.Entity.Payment;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import com.demo.Repository.PaymentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component  //Marks this class as a Spring component, making it eligible for auto-detection and dependency injection
public class PaymentDataLoader implements CommandLineRunner 
{
    private final PaymentRepository paymentRepository;  //Repository for performing database operations on the Payment entity

    //Constructor-based dependency injection to inject the PaymentRepository
    public PaymentDataLoader(PaymentRepository paymentRepository) 
    {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void run(String... args) throws Exception 
    {
        //Check if payment records already exist in the database to avoid reloading sample data on each application startup
        if (paymentRepository.count() == 0) 
        {
            //Create a sample payment record using the builder pattern
            Payment payment1 = Payment.builder().amount(new BigDecimal("100.50")).paymentMode(PaymentMode.CREDIT_CARD)
            .paymentStatus(PaymentStatus.PENDING).paymentDateTime(LocalDateTime.now().minusDays(1))
            .build();

            //Create another sample payment record
            Payment payment2 = Payment.builder().amount(new BigDecimal("200.75")).paymentMode(PaymentMode.DEBIT_CARD)
            .paymentStatus(PaymentStatus.PAID).paymentDateTime(LocalDateTime.now().minusDays(2))
            .build();

            //Create another sample payment record with a different status
            Payment payment3 = Payment.builder().amount(new BigDecimal("50.00")).paymentMode(PaymentMode.PAYNOW)
            .paymentStatus(PaymentStatus.UNPAID).paymentDateTime(LocalDateTime.now().minusDays(3))
            .build();

            //Save the sample payment data to the database using the PaymentRepository
            paymentRepository.save(payment1);
            paymentRepository.save(payment2);
            paymentRepository.save(payment3);

            System.out.println("Sample payment data loaded successfully."); //Log a message to indicate that the sample payment data has been successfully loaded
        } 
        
        else    //If payment records already exist, skip the data loading process    
            System.out.println("Payment data already exists, skipping data loading.");
    }
}