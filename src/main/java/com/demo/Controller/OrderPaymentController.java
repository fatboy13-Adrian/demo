package com.demo.Controller;
import com.demo.DTO.OrderPaymentDTO;
import com.demo.Interface.OrderPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order_payment")   //Map all requests to /order_payment URL
public class OrderPaymentController 
{
    private final OrderPaymentService orderPaymentService;

    //Constructor injection for OrderPaymentService
    public OrderPaymentController(OrderPaymentService orderPaymentService) 
    {
        this.orderPaymentService = orderPaymentService; //Initialize the service with the injected service
    }

    @PostMapping  //This method handles POST requests for creating a new OrderPayment
    public ResponseEntity<OrderPaymentDTO> createOrderPayment(@RequestBody OrderPaymentDTO orderPaymentDTO) 
    {
        //Call the service to create the order payment and return the created OrderPaymentDTO
        OrderPaymentDTO createdOrderPayment = orderPaymentService.createOrderPayment(orderPaymentDTO);
        return new ResponseEntity<>(createdOrderPayment, HttpStatus.CREATED);   //Return the created order payment with a 201 CREATED status
    }

    @GetMapping("/{poid}")  //This method handles GET requests for fetching a specific OrderPayment by POID
    public ResponseEntity<OrderPaymentDTO> getOrderPayment(@PathVariable Long poid) 
    {
        //Call the service to fetch the order payment by POID and return it
        OrderPaymentDTO orderPayment = orderPaymentService.getOrderPayment(poid);
        return new ResponseEntity<>(orderPayment, HttpStatus.OK);   //Return the fetched order payment with a 200 OK status
    }

    @GetMapping  //This method handles GET requests to fetch all OrderPayments
    public ResponseEntity<List<OrderPaymentDTO>> getAllOrderPayments() 
    {
        //Call the service to fetch all order payments
        List<OrderPaymentDTO> orderPayments = orderPaymentService.getOrderPayments();
        return new ResponseEntity<>(orderPayments, HttpStatus.OK);  //Return the list of order payments with a 200 OK status
    }

    @PutMapping("/{poid}")  //This method handles PUT requests to update an existing OrderPayment
    public ResponseEntity<OrderPaymentDTO> updateOrderPayment(@PathVariable Long poid, @RequestBody OrderPaymentDTO orderPaymentDTO) 
    {
        //Call the service to update the order payment and return the updated OrderPaymentDTO
        OrderPaymentDTO updatedOrderPayment = orderPaymentService.updateOrderPayment(poid, orderPaymentDTO);
        return new ResponseEntity<>(updatedOrderPayment, HttpStatus.OK);    //Return the updated order payment with a 200 OK status
    }

    @DeleteMapping("/{poid}")  //This method handles DELETE requests to remove an OrderPayment by POID
    public ResponseEntity<Void> deleteOrderPayment(@PathVariable Long poid) 
    {
        orderPaymentService.deleteOrderPayment(poid);       //Call the service to delete the order payment
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //Return a 204 NO CONTENT status indicating successful deletion
    }
}