package com.demo.Interface;
import com.demo.DTO.OrderItemDTO;

import java.util.List;

public interface OrderItemService 
{
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO getOrderItem(Long oiid);
    List<OrderItemDTO> getOrderItems();
    OrderItemDTO updateOrderItem(Long oiid, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long oiid);
}