package com.demo.Exception.Order;

//Custom exception to handle the scenario where an OrderPayment is not found.
public class OrderPaymentNotFoundException extends RuntimeException 
{
    //Constructor takes the POID (Order Payment ID) and creates a detailed message
    public OrderPaymentNotFoundException(Long poid) 
    {
        super("Order payment with ID " + poid + " not found");  //Pass the error message to the superclass (RuntimeException) constructor
    }
}