package com.demo.Interface;
import java.util.List;  
import com.demo.DTO.OrderPaymentDTO;  

//Interface defining the contract for OrderPaymentService
public interface OrderPaymentService 
{
    OrderPaymentDTO createOrderPayment(OrderPaymentDTO dto);            //Method to create a new OrderPayment.
    OrderPaymentDTO getOrderPayment(Long poid);                         //Method to fetch a single OrderPayment by its POID. 
    List<OrderPaymentDTO> getOrderPayments();                           //Method to fetch all OrderPayments.
    OrderPaymentDTO updateOrderPayment(Long poid, OrderPaymentDTO dto); //Method to update an existing OrderPayment by POID.
    void deleteOrderPayment(Long poid);                                 //Method to delete an OrderPayment by its POID.
}