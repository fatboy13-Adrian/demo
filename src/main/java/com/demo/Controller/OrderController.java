package com.demo.Controller;
import com.demo.DTO.OrderDTO;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Interface.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController             //Marks this class as a RESTful controller
@RequestMapping("/orders")  //Specifies the base URL for this controller
public class OrderController 
{
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);  //Logger instance for logging events

    private final OrderService orderService;                                                    //Instance of OrderService to handle business logic

    @Autowired  //Constructor-based dependency injection for OrderService
    public OrderController(OrderService orderService) 
    {
        this.orderService = orderService;   //Initializes the order service to interact with the service layer
    }

    //Create a new order
    @PostMapping  //Maps POST requests to this method to create a new order
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) 
    {
        logger.info("Creating order: {}", orderDTO);            //Log the incoming order request
        OrderDTO createdOrder = orderService.createOrder(orderDTO);     //Call service method to create the order
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);  //Return the created order with HTTP 201 Created
    }

    @GetMapping("/{oid}")  //Maps GET requests with an order ID to this method
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long oid) 
    {
        try 
        {
            OrderDTO orderDTO = orderService.getOrder(oid);         //Fetch the order from the service layer
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);   //Return the order with HTTP 200 OK
        } 
        
        catch (OrderNotFoundException ex) 
        { 
            logger.error("Order not found: {}", oid);       //Log the error
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);      //Return HTTP 404 Not Found
        }
    }

    @GetMapping  //Maps GET requests to this method for fetching all orders
    public ResponseEntity<List<OrderDTO>> getOrders() 
    {
        List<OrderDTO> orderDTOs = orderService.getOrders();    //Fetch the list of orders from the service layer
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);  //Return the list of orders with HTTP 200 OK
    }

    @PatchMapping("/{oid}")  //Maps PATCH requests with an order ID to this method for partial updates
    public ResponseEntity<OrderDTO> partialUpdateOrder(@PathVariable Long oid, @RequestBody OrderDTO orderDTO) 
    { 
        try 
        {
            logger.info("Updating order with ID {}: {}", oid, orderDTO);    //Log the update request
            OrderDTO updatedOrder = orderService.partialUpdateOrder(oid, orderDTO); //Call the service method to update the order
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);               //Return the updated order with HTTP 200 OK
        } 
        
        catch (OrderNotFoundException ex) 
        { 
            logger.error("Order not found for update: {}", oid);    //Log the error
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);              //Return HTTP 404 Not Found
        }
    }

    @DeleteMapping("/{oid}")  //Maps DELETE requests with an order ID to this method for deletion
    public ResponseEntity<Void> deleteOrder(@PathVariable Long oid) 
    {
        try 
        {
            logger.info("Deleting order with ID: {}", oid); //Log the delete request
            orderService.deleteOrder(oid);                          //Call the service method to delete the order
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);     //Return HTTP 204 No Content after successful deletion
        } 
        
        catch (OrderNotFoundException ex)
        {  
            logger.error("Order not found for deletion: {}", oid);  //Log the error
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);      //Return HTTP 404 Not Found
        }
    }
}