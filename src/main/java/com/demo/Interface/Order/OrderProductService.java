package com.demo.Interface.Order;
import java.util.List;              

import com.demo.DTO.Order.OrderProductDTO;

//Interface for defining the service layer methods related to OrderProducts
public interface OrderProductService 
{
    OrderProductDTO createOrderProduct(OrderProductDTO dto);             //Method to create a new OrderProduct
    OrderProductDTO getOrderProduct(Long opid);                           //Method to retrieve a specific OrderProduct by its ID
    List<OrderProductDTO> getOrderProducts();                             //Method to retrieve a list of all OrderProducts
    OrderProductDTO updateOrderProduct(Long opid, OrderProductDTO dto);  //Method to update an existing OrderProduct
    void deleteOrderProduct(Long opid);                                  //Method to delete an OrderProduct by its ID
}
