package com.demo.Controller;
import com.demo.DTO.ItemInventoryDTO;
import com.demo.Service.ItemInventoryServiceImpl;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/item-inventory")      //Base URL for this controller's endpoints
public class ItemInventoryController 
{
    private final ItemInventoryServiceImpl itemInventoryService;

    //Constructor injection for ItemInventoryServiceImpl
    public ItemInventoryController(ItemInventoryServiceImpl itemInventoryService) 
    {
        this.itemInventoryService = itemInventoryService;
    }

    
    @PostMapping    //Endpoint to create a new ItemInventory
    public ResponseEntity<ItemInventoryDTO> createItemInventory(@RequestBody ItemInventoryDTO itemInventoryDTO) 
    {
        //Call the service to create the ItemInventory
        ItemInventoryDTO createdItemInventory = itemInventoryService.createItemInventory(itemInventoryDTO);

        //Return the created ItemInventory with HTTP status 201 (Created)
        return new ResponseEntity<>(createdItemInventory, HttpStatus.CREATED);
    }

    @GetMapping("/{siid}")  //Endpoint to get a specific ItemInventory by its ID
    public ResponseEntity<ItemInventoryDTO> getItemInventory(@PathVariable Long siid) 
    {
        try 
        {
            //Fetch the ItemInventory details from the service using the provided ID
            ItemInventoryDTO itemInventoryDTO = itemInventoryService.getItemInventory(siid);
            
            //Return the found ItemInventory with HTTP status 200 (OK)
            return new ResponseEntity<>(itemInventoryDTO, HttpStatus.OK);
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //If the ItemInventory is not found, return HTTP status 404 (Not Found)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping //Endpoint to get a list of all ItemInventories
    public ResponseEntity<List<ItemInventoryDTO>> getItemInventories() 
    {
        //Fetch the list of all ItemInventories from the service
        List<ItemInventoryDTO> itemInventories = itemInventoryService.getItemInventories();

        //Return the list with HTTP status 200 (OK)
        return new ResponseEntity<>(itemInventories, HttpStatus.OK);
    }

    @PutMapping("/{siid}")  //Endpoint to update an existing ItemInventory
    public ResponseEntity<ItemInventoryDTO> updateItemInventory(@PathVariable Long siid,@RequestBody ItemInventoryDTO itemInventoryDTO) 
    {
        try 
        {
            //Call the service to update the ItemInventory
            ItemInventoryDTO updatedItemInventory = itemInventoryService.updateItemInventory(siid, itemInventoryDTO);
            
            //Return the updated ItemInventory with HTTP status 200 (OK)
            return new ResponseEntity<>(updatedItemInventory, HttpStatus.OK);
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //If the ItemInventory is not found, return HTTP status 404 (Not Found)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);    
        }
    }

    @DeleteMapping("/{siid}")   //Endpoint to delete an ItemInventory by its ID
    public ResponseEntity<Void> deleteItemInventory(@PathVariable Long siid) 
    {
        try 
        { 
            itemInventoryService.deleteItemInventory(siid);     //Call the service to delete the ItemInventory by ID
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //Return HTTP status 204 (No Content) after successful deletion
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //If the ItemInventory is not found, return HTTP status 404 (Not Found)
        }
    }
}