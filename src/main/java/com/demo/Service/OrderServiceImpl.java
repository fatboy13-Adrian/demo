package com.demo.Service;

import com.demo.Entity.Order;
import com.demo.Enum.OrderStatus;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Interface.OrderService;
import com.demo.Repository.OrderRepository;
import com.demo.DTO.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated // This annotation is typically used to validate method parameters in the service class
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Create a new order
    @Override
    public OrderDTO createOrder(@Valid OrderDTO orderDTO) {
        // Map OrderDTO to Order entity
        Order order = new Order(orderDTO.getOid(), orderDTO.getTotalPrice(), orderDTO.getOrderStatus(), orderDTO.getOrderDateTime());
        
        // Save the order and return the DTO
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    // Retrieve an order by its ID
    @Override
    public OrderDTO getOrder(Long oid) {
        // Fetch the order from the repository
        Optional<Order> orderOptional = orderRepository.findById(oid);

        // If the order is not found, throw a custom exception
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException(oid);
        }

        // Convert to DTO and return
        return convertToDTO(orderOptional.get());
    }

    // Get all orders
    @Override
    public List<OrderDTO> getOrders() {
        // Fetch all orders from the repository
        List<Order> orders = orderRepository.findAll();

        // Convert and return as a list of OrderDTOs
        return orders.stream()
                     .map(this::convertToDTO)
                     .collect(Collectors.toList());
    }

    // Partial update of an existing order
    @Override
    public OrderDTO partialUpdateOrder(Long oid, BigDecimal totalPrice, OrderStatus orderStatus) {
        // Fetch the existing order
        Optional<Order> existingOrderOptional = orderRepository.findById(oid);

        // If not found, throw exception
        if (existingOrderOptional.isEmpty()) {
            throw new OrderNotFoundException(oid);
        }

        // Get the existing order
        Order existingOrder = existingOrderOptional.get();

        // Update the fields if new values are provided
        if (totalPrice != null) {
            existingOrder.setTotalPrice(totalPrice);
        }
        if (orderStatus != null) {
            existingOrder.setOrderStatus(orderStatus);
        }

        // Save the updated order
        existingOrder = orderRepository.save(existingOrder);

        // Return the updated DTO
        return convertToDTO(existingOrder);
    }

    // Delete an existing order
    @Override
    public void deleteOrder(Long oid) {
        // Fetch the order from the repository
        Optional<Order> orderOptional = orderRepository.findById(oid);

        // If the order is not found, throw a custom exception
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException(oid);
        }

        // Proceed to delete the order
        orderRepository.delete(orderOptional.get());
    }

    // Helper method to convert an Order entity to an OrderDTO
    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                       .oid(order.getOid())
                       .totalPrice(order.getTotalPrice())
                       .orderStatus(order.getOrderStatus())
                       .orderDateTime(order.getOrderDateTime())
                       .build();
    }
}