package com.demo.Interface;
import java.math.BigDecimal;
import java.util.List;
import com.demo.DTO.OrderDTO;
import com.demo.Enum.OrderStatus;

public interface OrderService 
{
    OrderDTO createOrder(OrderDTO orderDTO);    //Create a new order
    OrderDTO getOrder(Long oid);                //Retrieve an order by its ID
    List<OrderDTO> getOrders();                 //Retrieve all orders
    OrderDTO partialUpdateOrder(Long oid, BigDecimal totalPrice, OrderStatus orderStatus);  //Partially update an order (allows updating specific fields without modifying the whole object)
    void deleteOrder(Long oid);                 //Delete an order by its ID
}