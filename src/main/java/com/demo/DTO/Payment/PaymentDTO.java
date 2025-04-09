package com.demo.DTO.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.demo.Enum.Payment.PaymentMode;
import com.demo.Enum.Payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             //Automatically generates getter methods for all fields
@Setter             //Automatically generates setter methods for all fields
@NoArgsConstructor  //Generates a no-argument constructor (required for frameworks like JPA and model binding)
@AllArgsConstructor //Generates a constructor with arguments for all fields, providing an easy way to create instances with values
@Builder            //Enables the builder pattern for constructing instances of the class in a more readable way
public class PaymentDTO 
{
    private Long pid;                                //Unique identifier for the payment
    private BigDecimal amount;                       //Payment amount (uses BigDecimal for precision)
    private PaymentMode paymentMode;                 //Mode of payment (e.g., Cash, Credit Card)
    private PaymentStatus paymentStatus;             //Status of the payment (e.g., Pending, Completed)
    private LocalDateTime paymentDateTime;           //Date and time of the payment
}