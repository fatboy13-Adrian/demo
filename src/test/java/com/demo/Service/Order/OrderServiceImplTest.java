package com.demo.Service.Order;
import com.demo.DTO.Order.OrderDTO;
import com.demo.Entity.Order.Order;
import com.demo.Enum.Order.OrderStatus;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Repository.Order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Extension to enable Mockito in JUnit tests
public class OrderServiceImplTest 
{
    @Mock
    private OrderRepository orderRepository; //Mocking the OrderRepository dependency

    @InjectMocks
    private OrderServiceImpl orderService;  //Injecting the mocks into the OrderServiceImpl

    private Order order;                    //Order entity to be used in tests
    private OrderDTO orderDTO;              //OrderDTO to be used in tests

    @BeforeEach
    void setUp() 
    {
        //Setup a sample order and orderDTO to be used across tests
        order = new Order(1L, BigDecimal.valueOf(100.00), OrderStatus.PENDING, LocalDateTime.now());
        orderDTO = new OrderDTO(1L, BigDecimal.valueOf(100.00), OrderStatus.PENDING, LocalDateTime.now());
    }

    @Test   //Test Case: Create Order (Positive Test Case)
    void testCreateOrder_Success() 
    {
        //Mocking orderRepository.save() to return the order when createOrder() is called
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDTO createdOrder = orderService.createOrder(orderDTO); //Calling the method to test
        assertNotNull(createdOrder);                                //Checking if the createdOrder is not null
        assertEquals(order.getOid(), createdOrder.getOid());        //Ensuring the order's ID matches
    }

    @Test   //Test Case: Get Order by ID (Positive Test Case)
    void testGetOrder_Success() 
    {
        //Mocking orderRepository.findById() to return the order when the ID 1L is searched
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderDTO retrievedOrder = orderService.getOrder(1L);    //Calling the method to test
        assertNotNull(retrievedOrder);                              //Ensuring the retrievedOrder is not null
        assertEquals(order.getOid(), retrievedOrder.getOid());      //Ensuring the order's ID matches
    }

    @Test   //Test Case: Get All Orders (Positive Test Case)
    void testGetOrders_Success() 
    {
        //Mocking orderRepository.findAll() to return a list with one order
        when(orderRepository.findAll()).thenReturn(List.of(order));
        List<OrderDTO> orders = orderService.getOrders();   //Calling the method to test
        assertFalse(orders.isEmpty());                      //Ensuring the list of orders is not empty
        assertEquals(1, orders.size());             //Ensuring the size of the list is correct
    }

    @Test   //Test Case: Partial Update Order (Positive Test Case)
    void testPartialUpdateOrder_Success() 
    {
        //Mocking the behavior when an order is found and saved with new details
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDTO updatedOrder = orderService.partialUpdateOrder(1L, orderDTO);  //Calling the method to test
        assertNotNull(updatedOrder);                                                //Ensuring the updatedOrder is not null
        assertEquals(OrderStatus.PENDING, updatedOrder.getOrderStatus());           //Ensuring the status is updated correctly
    }

    @Test   //Test Case: Delete Order (Positive Test Case)
    void testDeleteOrder_Success() 
    {
        //Mocking orderRepository.findById() to return the order for deletion
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //Verifying that delete() is called once on the repository
        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository, times(1)).delete(order);    //Ensuring delete is called once
    }

    @Test   //Test Case: Get Order by ID (Negative Test Case)
    void testGetOrder_NotFound() 
    {
        //Mocking orderRepository.findById() to return empty (order not found)
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        //Asserting that OrderNotFoundException is thrown
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(2L));
    }

    @Test   //Test Case: Partial Update Order (Negative Test Case)
    void testPartialUpdateOrder_NotFound() 
    {
        //Mocking orderRepository.findById() to return empty (order not found)
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        //Asserting that OrderNotFoundException is thrown
        assertThrows(OrderNotFoundException.class, () -> orderService.partialUpdateOrder(2L, orderDTO));
    }

    @Test   //Test Case: Delete Order (Negative Test Case)
    void testDeleteOrder_NotFound() 
    {
        //Mocking orderRepository.findById() to return empty (order not found)
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        //Asserting that OrderNotFoundException is thrown
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(2L));
    }
}