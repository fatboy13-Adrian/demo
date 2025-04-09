package com.demo.Service;
import com.demo.DTO.OrderPaymentDTO;
import com.demo.Entity.OrderPayment;
import com.demo.Entity.Payment;
import com.demo.Exception.Order.OrderPaymentNotFoundException;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Repository.OrderPaymentRepository;
import com.demo.Repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //This automatically handles mock initialization
class OrderPaymentServiceImplTest 
{
    @Mock
    private OrderPaymentRepository orderPaymentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderPaymentServiceImpl service;

    @BeforeEach
    void setUp() 
    {
        //No need for MockitoAnnotations.openMocks(this) anymore with @ExtendWith
    }

    @Test
    void testCreateOrderPayment_Success() 
    {
        //Preparing mock data for payment and order
        Payment order = new Payment();
        order.setPid(1L);

        Payment payment = new Payment();
        payment.setPid(2L);

        OrderPaymentDTO dto = OrderPaymentDTO.builder().poid(10L).oid(1L).pid(2L).build();

        //Creating OrderPayment entity and mocking repository calls
        OrderPayment entity = OrderPayment.builder().poid(10L).pid(order).pid(payment).build();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findById(2L)).thenReturn(Optional.of(payment));
        when(orderPaymentRepository.save(any(OrderPayment.class))).thenReturn(entity);

        //Calling the service method and validating the result
        OrderPaymentDTO result = service.createOrderPayment(dto);
        assertNotNull(result);
        assertEquals(10L, result.getPoid());
        assertEquals(1L, result.getOid());
        assertEquals(2L, result.getPid());
    }

    @Test
    void testCreateOrderPayment_OrderPaymentNotFound() 
    {
        //Preparing mock DTO and simulating an empty response for paymentRepository
        OrderPaymentDTO dto = OrderPaymentDTO.builder().poid(50L).oid(10L).pid(20L).build();
        when(paymentRepository.findById(10L)).thenReturn(Optional.empty());

        //Expecting exception to be thrown when payment is not found
        assertThrows(PaymentNotFoundException.class, () -> service.createOrderPayment(dto));
    }

    @Test
    void testGetOrderPayment_Success() 
    {
        //Creating mock payment data
        Payment order = new Payment();
        order.setPid(1L);

        Payment payment = new Payment();
        payment.setPid(2L);

        //Creating OrderPayment entity
        OrderPayment entity = OrderPayment.builder().poid(20L).pid(order).pid(payment).build();

        //Mocking repository call to find OrderPayment by ID
        when(orderPaymentRepository.findById(20L)).thenReturn(Optional.of(entity));

        //Calling the service method and validating the result
        OrderPaymentDTO result = service.getOrderPayment(20L);
        assertNotNull(result);
        assertEquals(1L, result.getOid());
        assertEquals(2L, result.getPid());
    }

    @Test
    void testGetOrderPayment_NotFound() 
    {
        //Simulating not found scenario for order payment
        when(orderPaymentRepository.findById(99L)).thenReturn(Optional.empty());

        //Expecting exception when OrderPayment is not found
        assertThrows(OrderPaymentNotFoundException.class, () -> service.getOrderPayment(99L));
    }

    @Test
    void testGetOrderPayments_ReturnsList() 
    {
        //Mocking payment data and creating an OrderPayment entity
        Payment order = new Payment();
        order.setPid(1L);

        Payment payment = new Payment();
        payment.setPid(2L);

        OrderPayment entity = OrderPayment.builder().poid(100L).pid(order).pid(payment).build();

        //Mocking repository to return a list with a single OrderPayment entity
        when(orderPaymentRepository.findAll()).thenReturn(List.of(entity));

        //Calling the service method and validating the result
        List<OrderPaymentDTO> result = service.getOrderPayments();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetOrderPayments_EmptyList() 
    {
        //Mocking repository to return an empty list
        when(orderPaymentRepository.findAll()).thenReturn(Collections.emptyList());

        //Calling the service method and validating that the result is empty
        List<OrderPaymentDTO> result = service.getOrderPayments();
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateOrderPayment_Success() 
    {
        //Creating mock payment data for old and new payments
        Payment oldOrder = new Payment();
        oldOrder.setPid(1L);

        Payment oldPayment = new Payment();
        oldPayment.setPid(2L);

        //Creating existing OrderPayment entity
        OrderPayment existing = OrderPayment.builder().poid(30L).pid(oldOrder).pid(oldPayment).build();

        Payment newOrder = new Payment();
        newOrder.setPid(3L);

        Payment newPayment = new Payment();
        newPayment.setPid(4L);

        //Mocking DTO and repository calls for update
        OrderPaymentDTO dto = OrderPaymentDTO.builder().poid(30L).oid(3L).pid(4L).build();
        when(orderPaymentRepository.findById(30L)).thenReturn(Optional.of(existing));
        when(paymentRepository.findById(3L)).thenReturn(Optional.of(newOrder));
        when(paymentRepository.findById(4L)).thenReturn(Optional.of(newPayment));
        when(orderPaymentRepository.save(any(OrderPayment.class))).thenReturn(OrderPayment.builder().poid(30L).pid(newOrder).pid(newPayment).build());

        //Calling the service method and validating the updated values
        OrderPaymentDTO result = service.updateOrderPayment(30L, dto);
        assertEquals(3L, result.getOid());
        assertEquals(4L, result.getPid());
    }

    @Test
    void testUpdateOrderPayment_NotFound() 
    {
        //Simulating scenario where OrderPayment is not found
        when(orderPaymentRepository.findById(30L)).thenReturn(Optional.empty());

        //Preparing mock DTO for the update
        OrderPaymentDTO dto = OrderPaymentDTO.builder().oid(1L).pid(2L).build();

        //Expecting exception when OrderPayment is not found
        assertThrows(OrderPaymentNotFoundException.class, () -> service.updateOrderPayment(30L, dto));
    }

    @Test
    void testUpdateOrderPayment_PaymentNotFound() 
    {
        //Creating existing OrderPayment and DTO for the update
        Payment order = new Payment();
        order.setPid(1L);

        Payment payment = new Payment();
        payment.setPid(2L);

        OrderPayment existing = OrderPayment.builder().poid(30L).pid(order).pid(payment).build();
        OrderPaymentDTO dto = OrderPaymentDTO.builder().poid(30L).oid(3L).pid(4L).build();

        //Mocking repository calls for OrderPayment and Payment not found scenario
        when(orderPaymentRepository.findById(30L)).thenReturn(Optional.of(existing));
        when(paymentRepository.findById(3L)).thenReturn(Optional.empty());

        //Expecting exception when payment is not found
        assertThrows(PaymentNotFoundException.class, () -> service.updateOrderPayment(30L, dto));
    }

    @Test
    void testDeleteOrderPayment_Success() 
    {
        //Creating an OrderPayment entity and mocking repository calls
        OrderPayment orderPayment = OrderPayment.builder().poid(40L).pid(new Payment()).pid(new Payment()).build();
        when(orderPaymentRepository.findById(40L)).thenReturn(Optional.of(orderPayment));

        //Calling the service method and verifying the delete operation
        service.deleteOrderPayment(40L);
        verify(orderPaymentRepository).delete(orderPayment);
    }

    @Test
    void testDeleteOrderPayment_NotFound() 
    {
        //Simulating not found scenario for delete
        when(orderPaymentRepository.findById(40L)).thenReturn(Optional.empty());

        //Expecting exception when OrderPayment is not found for deletion
        assertThrows(OrderPaymentNotFoundException.class, () -> service.deleteOrderPayment(40L));
    }
}