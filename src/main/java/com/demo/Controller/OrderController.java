package com.demo.Controller;

import com.demo.DTO.OrderDTO;
import com.demo.Enum.OrderStatus;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Get an order by its ID
    @GetMapping("/{oid}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long oid) {
        try {
            OrderDTO orderDTO = orderService.getOrder(oid);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<OrderDTO> orderDTOs = orderService.getOrders();
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    // Partial update of an existing order
    @PatchMapping("/{oid}")
    public ResponseEntity<OrderDTO> partialUpdateOrder(
            @PathVariable Long oid,
            @RequestParam(required = false) BigDecimal totalPrice,
            @RequestParam(required = false) OrderStatus orderStatus) {

        try {
            OrderDTO updatedOrder = orderService.partialUpdateOrder(oid, totalPrice, orderStatus);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Delete an order
    @DeleteMapping("/{oid}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long oid) {
        try {
            orderService.deleteOrder(oid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
