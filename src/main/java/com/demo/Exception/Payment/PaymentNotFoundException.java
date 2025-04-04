package com.demo.Exception.Payment;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)                           //Automatically maps to a 404 response when this exception is thrown
public class PaymentNotFoundException extends RuntimeException 
{
    private final Long pid;                                     //Store the payment ID for which the exception was thrown

    //Constructor with a message indicating the payment was not found by its ID
    public PaymentNotFoundException(Long pid) 
    {
        super("Payment with ID " + pid + " not found");         //Set the exception message
        this.pid = pid;                                         //Initialize the payment ID
    }

    //Constructor with a message and a cause (another throwable) for the exception
    public PaymentNotFoundException(Long pid, Throwable cause) 
    {
        super("Payment with ID " + pid + " not found", cause);  //Set the exception message and cause
        this.pid = pid;                                         //Initialize the payment ID
    }
}