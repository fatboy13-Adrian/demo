package com.demo.Controller.Order;
import com.demo.DTO.Order.OrderPaymentDTO;
import com.demo.Interface.Order.OrderPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController                     //Specifies this class as a REST controller
@RequestMapping("/order_payment")   //Sets the base URL for the controller
public class OrderPaymentController 
{
    private final OrderPaymentService orderPaymentService;

    //Constructor injection for OrderPaymentService
    public OrderPaymentController(OrderPaymentService orderPaymentService) 
    {
        this.orderPaymentService = orderPaymentService; //Initializes the service
    }

    @PostMapping  //Maps POST requests to this method
    public ResponseEntity<OrderPaymentDTO> createOrderPayment(@RequestBody OrderPaymentDTO orderPaymentDTO) 
    {
        OrderPaymentDTO createdOrderPayment = orderPaymentService.createOrderPayment(orderPaymentDTO);  //Calls the service to create a new order payment
        return new ResponseEntity<>(createdOrderPayment, HttpStatus.CREATED);                           //Returns the created order payment with HTTP status 201 (Created)
    }

    @GetMapping("/{poid}")  //Maps GET requests with a path parameter for POID
    public ResponseEntity<OrderPaymentDTO> getOrderPayment(@PathVariable Long poid) 
    {
        OrderPaymentDTO orderPayment = orderPaymentService.getOrderPayment(poid);   //Fetches the order payment using the service
        return new ResponseEntity<>(orderPayment, HttpStatus.OK);                   //Returns the found order payment with HTTP status 200 (OK)
    }

    @GetMapping  //Maps GET requests to retrieve all order payments
    public ResponseEntity<List<OrderPaymentDTO>> getAllOrderPayments() 
    {
        List<OrderPaymentDTO> orderPayments = orderPaymentService.getOrderPayments();   //Fetches the list of all order payments using the service
        return new ResponseEntity<>(orderPayments, HttpStatus.OK);                      //Returns the list of order payments with HTTP status 200 (OK)
    }

    @PutMapping("/{poid}")  //Maps PUT requests to update an order payment with a specific POID
    public ResponseEntity<OrderPaymentDTO> updateOrderPayment(@PathVariable Long poid, @RequestBody OrderPaymentDTO orderPaymentDTO) 
    {
        //Calls the service to update the order payment
        OrderPaymentDTO updatedOrderPayment = orderPaymentService.updateOrderPayment(poid, orderPaymentDTO);
        return new ResponseEntity<>(updatedOrderPayment, HttpStatus.OK);    //Returns the updated order payment with HTTP status 200 (OK)
    }

    @DeleteMapping("/{poid}")  //Maps DELETE requests to remove an order payment with a specific POID
    public ResponseEntity<Void> deleteOrderPayment(@PathVariable Long poid) 
    {
        orderPaymentService.deleteOrderPayment(poid);       //Calls the service to delete the order payment
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //Returns HTTP status 204 (No Content) indicating successful deletion
    }
}