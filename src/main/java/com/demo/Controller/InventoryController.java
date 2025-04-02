package com.demo.Controller;
import jakarta.validation.Valid;  
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.demo.DTO.InventoryDTO;  
import com.demo.Interface.InventoryService;  
import java.util.List;

@RestController                                         //Marks this class as a REST controller, allowing it to handle HTTP requests
@RequestMapping("/inventories")                         //Base URL path for all endpoints in this controller
public class InventoryController 
{
    private final InventoryService inventoryService;    //Injecting the InventoryService

    //Constructor-based dependency injection of InventoryService
    public InventoryController(InventoryService inventoryService) 
    {
        this.inventoryService = inventoryService;
    }

    //POST method for creating a new inventory
    @PostMapping  //Marks this method as handling POST requests to /inventories
    public ResponseEntity<?> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) 
    {
        //Check if stock quantity is null or zero, return bad request if so
        if (inventoryDTO.getStockQty() == null || inventoryDTO.getStockQty() == 0) 
            return ResponseEntity.badRequest().body("Stock inventory cannot be null or zero");
        
        //Create and return the new inventory, with HTTP status 201 (Created)
        return new ResponseEntity<>(inventoryService.createInventory(inventoryDTO), HttpStatus.CREATED);
    }

    //GET method for retrieving a specific inventory by its sid (id)
    @GetMapping("/{sid}")  //Marks this method as handling GET requests with a path variable 'sid'
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable Long sid) 
    {
        //Retrieve the inventory by sid, return 200 OK if found, else return 404 Not Found
        InventoryDTO inventoryDTO = inventoryService.getInventory(sid);
        return (inventoryDTO != null) ? ResponseEntity.ok(inventoryDTO) : ResponseEntity.notFound().build();
    }

    //GET method for retrieving all inventories
    @GetMapping  //Marks this method as handling GET requests to /inventories
    public ResponseEntity<List<InventoryDTO>> getInventories() 
    {
        List<InventoryDTO> inventories = inventoryService.getInventories(); //Retrieve the list of inventories
        return ResponseEntity.ok(inventories);  //Return the list with HTTP status 200 (OK)
    }

    //PUT method for updating an existing inventory by its sid
    @PutMapping("/{sid}")  //Marks this method as handling PUT requests with a path variable 'sid'
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long sid, @RequestBody InventoryDTO inventoryDTO) 
    {
        //Update the inventory and return the updated inventory with HTTP status 200 (OK), or 404 if not found
        InventoryDTO updatedInventory = inventoryService.updateInventory(sid, inventoryDTO);
        return (updatedInventory != null) ? ResponseEntity.ok(updatedInventory) : ResponseEntity.notFound().build();
    }

    //DELETE method for deleting an inventory by its sid
    @DeleteMapping("/{sid}")  //Marks this method as handling DELETE requests with a path variable 'sid'
    public ResponseEntity<Void> deleteInventory(@PathVariable Long sid) 
    {
        try 
        {
            //Try deleting the inventory and return 204 No Content if successful
            inventoryService.deleteInventory(sid);
            return ResponseEntity.noContent().build();
        } 
        
        catch (IllegalArgumentException e) 
        {
            return ResponseEntity.notFound().build();   //Return 404 Not Found if the inventory does not exist
        }
    }
}