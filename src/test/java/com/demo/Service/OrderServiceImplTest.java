package com.demo.Service;
import com.demo.DTO.OrderDTO;
import com.demo.Entity.Order;
import com.demo.Enum.OrderStatus;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Sample order and orderDTO setup
        order = new Order(1L, new BigDecimal("100.00"), OrderStatus.PENDING, LocalDateTime.now());
        orderDTO = new OrderDTO(1L, new BigDecimal("100.00"), OrderStatus.PENDING, LocalDateTime.now());
    }

    @Test
    void testCreateOrder_ValidDTO() {
        // Arrange: Mock the repository to return a saved order when save is called
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act: Call the createOrder method
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO);

        // Assert: Verify the result
        assertNotNull(createdOrderDTO);
        assertEquals(order.getOid(), createdOrderDTO.getOid());
        assertEquals(order.getTotalPrice(), createdOrderDTO.getTotalPrice());
        assertEquals(order.getOrderStatus(), createdOrderDTO.getOrderStatus());
        assertEquals(order.getOrderDateTime(), createdOrderDTO.getOrderDateTime());

        // Verify that the save method was called once
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_InvalidDTO() {
        // Arrange: Pass an invalid OrderDTO (e.g., missing required fields)
        OrderDTO invalidOrderDTO = new OrderDTO(); // Invalid DTO with no fields

        // Act & Assert: Expect a validation exception to be thrown
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            orderService.createOrder(invalidOrderDTO);
        });
    }

    @Test
    void testGetOrder_OrderFound() {
        // Arrange: Mock the repository to return an existing order when findById is called
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act: Call the getOrder method
        OrderDTO foundOrderDTO = orderService.getOrder(1L);

        // Assert: Verify the result
        assertNotNull(foundOrderDTO);
        assertEquals(order.getOid(), foundOrderDTO.getOid());
        assertEquals(order.getTotalPrice(), foundOrderDTO.getTotalPrice());
        assertEquals(order.getOrderStatus(), foundOrderDTO.getOrderStatus());
        assertEquals(order.getOrderDateTime(), foundOrderDTO.getOrderDateTime());

        // Verify that the findById method was called once
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrder_OrderNotFound() {
        // Arrange: Mock the repository to return empty when findById is called
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Ensure that the OrderNotFoundException is thrown when the order is not found
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(1L));

        // Verify that findById was called once
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrders() {
        // Arrange: Mock the repository to return a list of orders
        when(orderRepository.findAll()).thenReturn(List.of(order));

        // Act: Call the getOrders method
        List<OrderDTO> orders = orderService.getOrders();

        // Assert: Verify that the list is not empty and contains the expected orderDTO
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(order.getOid(), orders.get(0).getOid());

        // Verify that the findAll method was called once
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testPartialUpdateOrder_Valid() {
        // Arrange: Mock the repository to return an existing order
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act: Call the partialUpdateOrder method
        OrderDTO updatedOrderDTO = orderService.partialUpdateOrder(1L, new BigDecimal("150.00"), OrderStatus.COMPLETED);

        // Assert: Verify the updated fields
        assertNotNull(updatedOrderDTO);
        assertEquals(new BigDecimal("150.00"), updatedOrderDTO.getTotalPrice());
        assertEquals(OrderStatus.COMPLETED, updatedOrderDTO.getOrderStatus());

        // Verify that the findById and save methods were called once
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testPartialUpdateOrder_NotFound() {
        // Arrange: Mock the repository to return empty for findById
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Ensure that the OrderNotFoundException is thrown when the order is not found
        assertThrows(OrderNotFoundException.class, () -> orderService.partialUpdateOrder(1L, new BigDecimal("150.00"), OrderStatus.COMPLETED));

        // Verify that findById was called once
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteOrder_Valid() {
        // Arrange: Mock the repository to return an existing order when findById is called
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act: Call the deleteOrder method
        orderService.deleteOrder(1L);

        // Assert: Verify that the delete method was called exactly once
        verify(orderRepository, times(1)).delete(any(Order.class));
    }

    @Test
    void testDeleteOrder_NotFound() {
        // Arrange: Mock the repository to return empty when findById is called
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Ensure that the OrderNotFoundException is thrown when the order is not found
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));

        // Verify that findById was called once
        verify(orderRepository, times(1)).findById(1L);
        // Verify that delete was not called since the order was not found
        verify(orderRepository, times(0)).delete(any(Order.class));
    }
}
