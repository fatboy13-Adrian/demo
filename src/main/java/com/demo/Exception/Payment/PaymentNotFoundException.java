package com.demo.Exception.Payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND) //Automatically maps to a 404 response
public class PaymentNotFoundException extends RuntimeException 
{
    private final Long pid;

    public PaymentNotFoundException(Long pid) 
    {
        super("Payment with ID " + pid + " not found");
        this.pid = pid;
    }

    public PaymentNotFoundException(Long pid, Throwable cause) 
    {
        super("Payment with ID " + pid + " not found", cause);
        this.pid = pid;
    }
}