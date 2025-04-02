package com.demo.DTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.demo.Enum.PaymentStatus;
import com.demo.Enum.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             //Automatically generates getter methods for all fields
@Setter             //Automatically generates setter methods for all fields
@NoArgsConstructor  //Generates a no-argument constructor (required for frameworks like JPA)
@AllArgsConstructor //Generates a constructor with arguments for all fields, providing an easy way to create instances with values
@Builder            //Enables the builder pattern for constructing instances of the class in a more readable way
public class PaymentDTO 
{
    private Long pid;
    private BigDecimal amount;
    private PaymentMode paymentMode;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDateTime;
}