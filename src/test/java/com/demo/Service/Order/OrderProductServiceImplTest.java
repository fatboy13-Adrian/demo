package com.demo.Service.Order;

import com.demo.DTO.Order.OrderProductDTO;
import com.demo.Entity.Order.Order;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Order.OrderProduct;
import com.demo.Exception.Order.OrderProductNotFoundException;
import com.demo.Repository.Order.OrderProductRepository;
import com.demo.Repository.Order.OrderRepository;
import com.demo.Repository.Product.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceImplTest {

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderProductServiceImpl orderProductService;

    private final Long opid = 1L;
    private final Long oid = 2L;
    private final Long pid = 3L;

    private final Order order = Order.builder().oid(oid).build();
    private final Product product = Product.builder().pid(pid).build();
    private final OrderProduct orderProduct = OrderProduct.builder()
            .opid(opid)
            .oid(order)
            .pid(product)
            .build();
    private final OrderProductDTO orderProductDTO = OrderProductDTO.builder()
            .opid(opid)
            .oid(oid)
            .pid(pid)
            .build();

    @Test
    void testCreateOrderProduct_Success() {
        when(orderRepository.findById(oid)).thenReturn(Optional.of(order));
        when(productRepository.findById(pid)).thenReturn(Optional.of(product));
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(orderProduct);

        OrderProductDTO result = orderProductService.createOrderProduct(orderProductDTO);

        assertEquals(opid, result.getOpid());
        assertEquals(oid, result.getOid());
        assertEquals(pid, result.getPid());
        verify(orderProductRepository, times(1)).save(any(OrderProduct.class));
    }

    @Test
    void testGetOrderProduct_Success() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.of(orderProduct));

        OrderProductDTO result = orderProductService.getOrderProduct(opid);

        assertEquals(opid, result.getOpid());
        assertEquals(oid, result.getOid());
        assertEquals(pid, result.getPid());
    }

    @Test
    void testGetOrderProducts_Success() {
        when(orderProductRepository.findAll()).thenReturn(Arrays.asList(orderProduct));

        List<OrderProductDTO> result = orderProductService.getOrderProducts();

        assertEquals(1, result.size());
        assertEquals(opid, result.get(0).getOpid());
    }

    @Test
    void testUpdateOrderProduct_Success() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.of(orderProduct));
        when(orderRepository.findById(oid)).thenReturn(Optional.of(order));
        when(productRepository.findById(pid)).thenReturn(Optional.of(product));
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(orderProduct);

        OrderProductDTO result = orderProductService.updateOrderProduct(opid, orderProductDTO);

        assertEquals(opid, result.getOpid());
        assertEquals(oid, result.getOid());
        assertEquals(pid, result.getPid());
        verify(orderProductRepository, times(1)).save(any(OrderProduct.class));
    }

    @Test
    void testDeleteOrderProduct_Success() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.of(orderProduct));

        assertDoesNotThrow(() -> orderProductService.deleteOrderProduct(opid));
        verify(orderProductRepository, times(1)).delete(orderProduct);
    }

    @Test
    void testCreateOrderProduct_OrderNotFound() {
        when(orderRepository.findById(oid)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class, () -> orderProductService.createOrderProduct(orderProductDTO));
    }

    @Test
    void testCreateOrderProduct_ProductNotFound() {
        when(orderRepository.findById(oid)).thenReturn(Optional.of(order));
        when(productRepository.findById(pid)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class, () -> orderProductService.createOrderProduct(orderProductDTO));
    }

    @Test
    void testGetOrderProduct_NotFound() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class, () -> orderProductService.getOrderProduct(opid));
    }

    @Test
    void testUpdateOrderProduct_NotFound() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class, () -> orderProductService.updateOrderProduct(opid, orderProductDTO));
    }

    @Test
    void testDeleteOrderProduct_NotFound() {
        when(orderProductRepository.findById(opid)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class, () -> orderProductService.deleteOrderProduct(opid));
    }
}
