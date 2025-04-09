package com.demo.DTO.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             //Generates getters for all fields
@Setter             //Generates setters for all fields
@NoArgsConstructor  //Generates a no-argument constructor
@AllArgsConstructor //Generates a constructor with all arguments
@Builder            //Generates a builder pattern for creating instances
public class OrderPaymentDTO 
{
    private Long poid, oid, pid; //Fields for OrderPaymentDTO
}