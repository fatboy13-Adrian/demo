package com.demo.Controller.Order;

import com.demo.DTO.Order.OrderProductDTO;
import com.demo.Exception.Order.OrderProductNotFoundException;
import com.demo.Interface.Order.OrderProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductControllerTest {

    @Mock
    private OrderProductService orderProductService;

    @InjectMocks
    private OrderProductController orderProductController;

    private OrderProductDTO orderProductDTO;

    @BeforeEach
    void setUp() {
        orderProductDTO = OrderProductDTO.builder().opid(1L).oid(1L).pid(2L).build();
    }

    @Test
    void testCreateOrderProduct_success() {
        when(orderProductService.createOrderProduct(any(OrderProductDTO.class))).thenReturn(orderProductDTO);

        ResponseEntity<OrderProductDTO> response = orderProductController.createOrderProduct(orderProductDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderProductDTO, response.getBody());
    }

    @Test
    void testCreateOrderProduct_badRequest() {
        when(orderProductService.createOrderProduct(any(OrderProductDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<OrderProductDTO> response = orderProductController.createOrderProduct(orderProductDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetOrderProduct_success() {
        when(orderProductService.getOrderProduct(1L)).thenReturn(orderProductDTO);

        ResponseEntity<OrderProductDTO> response = orderProductController.getOrderProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderProductDTO, response.getBody());
    }

    @Test
    void testGetOrderProduct_notFound() {
        when(orderProductService.getOrderProduct(1L)).thenThrow(new OrderProductNotFoundException(1L));

        ResponseEntity<OrderProductDTO> response = orderProductController.getOrderProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllOrderProducts_success() {
        when(orderProductService.getOrderProducts()).thenReturn(Arrays.asList(orderProductDTO));

        ResponseEntity<List<OrderProductDTO>> response = orderProductController.getAllOrderProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllOrderProducts_empty() {
        when(orderProductService.getOrderProducts()).thenReturn(List.of());

        ResponseEntity<List<OrderProductDTO>> response = orderProductController.getAllOrderProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testUpdateOrderProduct_success() {
        OrderProductDTO updatedDTO = OrderProductDTO.builder().opid(1L).oid(2L).pid(3L).build();
        when(orderProductService.updateOrderProduct(eq(1L), any(OrderProductDTO.class))).thenReturn(updatedDTO);

        ResponseEntity<OrderProductDTO> response = orderProductController.updateOrderProduct(1L, updatedDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDTO, response.getBody());
    }

    @Test
    void testUpdateOrderProduct_notFound() {
        when(orderProductService.updateOrderProduct(eq(1L), any(OrderProductDTO.class)))
                .thenThrow(new OrderProductNotFoundException(1L));

        ResponseEntity<OrderProductDTO> response = orderProductController.updateOrderProduct(1L, orderProductDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateOrderProduct_badRequest() {
        when(orderProductService.updateOrderProduct(eq(1L), any(OrderProductDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<OrderProductDTO> response = orderProductController.updateOrderProduct(1L, orderProductDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteOrderProduct_success() {
        doNothing().when(orderProductService).deleteOrderProduct(1L);

        ResponseEntity<Void> response = orderProductController.deleteOrderProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteOrderProduct_notFound() {
        doThrow(new OrderProductNotFoundException(1L)).when(orderProductService).deleteOrderProduct(1L);

        ResponseEntity<Void> response = orderProductController.deleteOrderProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
