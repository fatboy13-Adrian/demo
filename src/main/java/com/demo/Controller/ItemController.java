package com.demo.Controller;
import com.demo.DTO.ItemDTO; 
import com.demo.Exception.Item.ItemNotFoundException; 
import com.demo.Interface.ItemService; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import java.util.List; 

@RestController
@RequestMapping("/items")   //Maps HTTP requests to the "/items" endpoint
public class ItemController 
{
    private final ItemService itemService; //Injecting the item service layer for business logic

    //Constructor injection for the ItemService
    public ItemController(ItemService itemService) 
    {
        this.itemService = itemService;
    }

    //Create a new item
    @PostMapping    //Maps POST requests to create a new item
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) 
    {
        try 
        {
            
            ItemDTO createdItem = itemService.createItem(itemDTO);          //Calls service layer to create a new item and return the created item as a DTO
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);   //Responds with 201 Created status
        } 
        
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    //Handles any exception that occurs and responds with a 400 Bad Request
        }
    }

    //Get an item by its ID
    @GetMapping("/{iid}") //Maps GET requests to fetch an item by its ID
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long iid) 
    {
        try 
        {
            
            ItemDTO itemDTO = itemService.getItem(iid); //Calls service layer to retrieve item by its ID
            return ResponseEntity.ok(itemDTO);          //Responds with the item data and 200 OK status
        } 
        
        catch (ItemNotFoundException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  //Handles the case when item is not found and responds with 404 Not Found
        }
    }

    //Get all items
    @GetMapping //Maps GET requests to fetch all items
    public ResponseEntity<List<ItemDTO>> getItems() 
    {
        List<ItemDTO> items = itemService.getItems();   //Calls service layer to retrieve all items
        return ResponseEntity.ok(items);                //Responds with a list of items and 200 OK status
    }

    //Partially update an item
    @PatchMapping("/{iid}") //Maps PATCH requests to update an item by its ID (partial update)
    public ResponseEntity<ItemDTO> partialUpdateItem(@PathVariable Long iid, @RequestBody ItemDTO itemDTO) 
    {
        try 
        {
            //Calls service layer to partially update the item with provided data
            ItemDTO updatedItem = itemService.partialUpdateItem(iid, itemDTO);
            return ResponseEntity.ok(updatedItem);  //Responds with updated item data and 200 OK status
        } 
        
        catch (ItemNotFoundException e) 
        {
            //Handles the case when item is not found and responds with 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Delete an item by its ID
    @DeleteMapping("/{iid}") //Maps DELETE requests to delete an item by its ID
    public ResponseEntity<Void> deleteItem(@PathVariable Long iid) 
    {
        try 
        {
            itemService.deleteItem(iid);                //Calls service layer to delete the item
            return ResponseEntity.noContent().build();  //Responds with 204 No Content status if deletion is successful
        } 
        
        catch (ItemNotFoundException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //Handles the case when item is not found and responds with 404 Not Found
        }
    }
}