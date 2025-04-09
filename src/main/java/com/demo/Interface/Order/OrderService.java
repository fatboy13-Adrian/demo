package com.demo.Interface.Order;
import java.util.List;
import com.demo.DTO.Order.OrderDTO;

public interface OrderService 
{
    OrderDTO createOrder(OrderDTO orderDTO);                         //Create a new order
    OrderDTO getOrder(Long oid);                                     //Retrieve an order by its ID
    List<OrderDTO> getOrders();                                      //Retrieve all orders
    OrderDTO partialUpdateOrder(Long oid, OrderDTO orderDTO);        //Partially update an order (accepts an OrderDTO)
    void deleteOrder(Long oid);                                      //Delete an order by its ID
}