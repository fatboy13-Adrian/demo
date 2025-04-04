package com.demo.Entity;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import jakarta.persistence.*; 
import jakarta.validation.constraints.NotNull; 
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity                     //Marks the class as a JPA entity, meaning it will be mapped to a database table
@Table(name = "payments")   //Specifies the table name in the database
@Getter                     //Automatically generates getter methods for all fields
@Setter                     //Automatically generates setter methods for all fields
@NoArgsConstructor          //Generates a no-argument constructor (required for frameworks like JPA and Hibernate)
@AllArgsConstructor         //Generates a constructor with arguments for all fields
@Builder                    //Enables the builder pattern for constructing instances of the class
public class Payment 
{
    @Id                                                     //Marks this field as the primary key in the database
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //Auto-incrementing ID generation
    @Column(name = "pid")                                   //Specifies the column name in the database
    private Long pid;                                       //Unique identifier for the payment

    @Column(name = "amount", nullable = false)              //Specifies the column name and makes it non-nullable
    @NotNull(message = "Amount is mandatory")               //Validation to ensure the amount is provided
    private BigDecimal amount;                              //Payment amount, using BigDecimal for precision in financial calculations

    @Enumerated(EnumType.STRING)                            //Stores the enum as a String in the database instead of an ordinal value
    @Column(name = "payment_mode", nullable = false)        //Maps this field to a database column
    @NotNull(message = "Payment mode needs to be defined")  //Ensures payment mode is mandatory
    private PaymentMode paymentMode;                        //Defines the payment mode (e.g., CASH, CREDIT_CARD, etc.)

    @Enumerated(EnumType.STRING)                            //Stores the enum as a String in the database instead of an ordinal value
    @Column(name = "payment_status", nullable = false)      //Maps this field to a database column
    @NotNull(message = "Payment status is mandatory")       //Ensures payment status is mandatory
    private PaymentStatus paymentStatus;                    //Defines the payment status (e.g., PENDING, COMPLETED)

    @Column(name = "paymentDateTime", nullable = false)     //Specifies the column name and makes it non-nullable
    @NotNull(message = "Date time is mandatory")            //Ensures the payment date and time are mandatory
    private LocalDateTime paymentDateTime;                  //Stores the date and time when the payment was made
}