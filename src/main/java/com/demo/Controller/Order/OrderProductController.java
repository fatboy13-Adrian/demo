package com.demo.Controller.Order;

import com.demo.DTO.Order.OrderProductDTO;
import com.demo.Exception.Order.OrderProductNotFoundException;
import com.demo.Interface.Order.OrderProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order_product")
public class OrderProductController {

    private final OrderProductService orderProductService;

    // Constructor injection of OrderProductService
    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    /**
     * Create a new OrderProduct
     */
    @PostMapping
    public ResponseEntity<OrderProductDTO> createOrderProduct(@Valid @RequestBody OrderProductDTO orderProductDTO) {
        try {
            OrderProductDTO created = orderProductService.createOrderProduct(orderProductDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            // Return bad request if there is invalid input
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get OrderProduct by ID
     */
    @GetMapping("/{opid}")
    public ResponseEntity<OrderProductDTO> getOrderProduct(@PathVariable Long opid) {
        try {
            OrderProductDTO found = orderProductService.getOrderProduct(opid);
            return ResponseEntity.ok(found);
        } catch (OrderProductNotFoundException e) {
            // Return 404 if OrderProduct is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all OrderProducts
     */
    @GetMapping
    public ResponseEntity<List<OrderProductDTO>> getAllOrderProducts() {
        List<OrderProductDTO> list = orderProductService.getOrderProducts();
        return ResponseEntity.ok(list);
    }

    /**
     * Update an existing OrderProduct
     */
    @PutMapping("/{opid}")
    public ResponseEntity<OrderProductDTO> updateOrderProduct(@PathVariable Long opid, @Valid @RequestBody OrderProductDTO orderProductDTO) {
        try {
            OrderProductDTO updated = orderProductService.updateOrderProduct(opid, orderProductDTO);
            return ResponseEntity.ok(updated);
        } catch (OrderProductNotFoundException e) {
            // Return 404 if OrderProduct with given ID is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            // Return 400 if input is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete an OrderProduct by ID
     */
    @DeleteMapping("/{opid}")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable Long opid) {
        try {
            orderProductService.deleteOrderProduct(opid);
            return ResponseEntity.noContent().build(); // 204 No Content for successful delete
        } catch (OrderProductNotFoundException e) {
            // Return 404 if OrderProduct is not found for deletion
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
