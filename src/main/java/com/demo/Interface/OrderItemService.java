package com.demo.Interface;
import com.demo.DTO.OrderItemDTO;   //Importing the OrderItemDTO class, which is used for transferring data
import java.util.List;              //Importing the List interface for returning a list of items

//Interface for defining the service layer methods related to OrderItems
public interface OrderItemService 
{
    OrderItemDTO createOrderItem(OrderItemDTO dto);             //Method to create a new OrderItem
    OrderItemDTO getOrderItem(Long oiid);                       //Method to retrieve a specific OrderItem by its ID
    List<OrderItemDTO> getOrderItems();                         //Method to retrieve a list of all OrderItems
    OrderItemDTO updateOrderItem(Long oiid, OrderItemDTO dto);  //Method to update an existing OrderItem
    void deleteOrderItem(Long oiid);                            //Method to delete an OrderItem by its ID
}