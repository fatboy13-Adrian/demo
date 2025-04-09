package com.demo.Controller;
import com.demo.DTO.OrderItemDTO;
import com.demo.Interface.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order_item")  //Base URL for OrderItem related endpoints
public class OrderItemController 
{
    private final OrderItemService orderItemService;

    //Constructor injection of the OrderItemService to handle the business logic
    public OrderItemController(OrderItemService orderItemService) 
    {
        this.orderItemService = orderItemService;
    }

    @PostMapping    //Endpoint to create a new OrderItem
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItemDTO) 
    {
        OrderItemDTO created = orderItemService.createOrderItem(orderItemDTO);  //Create the order item using the service
        return ResponseEntity.ok(created);                                      //Return the created order item with HTTP status 200 OK
    }

    @GetMapping("/{oiid}")  //Endpoint to retrieve a specific OrderItem by its ID
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable Long oiid) 
    {
        //Retrieve the order item by ID
        OrderItemDTO found = orderItemService.getOrderItem(oiid);

        if (found == null) 
            return ResponseEntity.notFound().build();   //If not found, return HTTP status 404 Not Found
        
        return ResponseEntity.ok(found);                //If found, return HTTP status 200 OK with the order item
    }

    @GetMapping //Endpoint to retrieve all OrderItems
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() 
    {
        List<OrderItemDTO> list = orderItemService.getOrderItems(); //Retrieve all order items from the service
        return ResponseEntity.ok(list);                             //Return the list of order items with HTTP status 200 OK
    }

    @PutMapping("/{oiid}")  //Endpoint to update an existing OrderItem by its ID
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long oiid, @RequestBody OrderItemDTO orderItemDTO) 
    {
        //Update the order item using the service
        OrderItemDTO updated = orderItemService.updateOrderItem(oiid, orderItemDTO);

        if (updated == null)
            return ResponseEntity.notFound().build();    //If the order item is not found, return HTTP status 404 Not Found

        return ResponseEntity.ok(updated);              //If successfully updated, return the updated order item with HTTP status 200 OK
    }

    @DeleteMapping("/{oiid}")   //Endpoint to delete an OrderItem by its ID
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long oiid) 
    {
        orderItemService.deleteOrderItem(oiid);     //Delete the order item using the service
        return ResponseEntity.noContent().build();  //Return HTTP status 204 No Content indicating successful deletion
    }
}