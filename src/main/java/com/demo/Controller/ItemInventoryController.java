package com.demo.Controller;
import com.demo.DTO.ItemInventoryDTO;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Interface.ItemInventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/item-inventory")
@Slf4j  //Lombok annotation for logging
public class ItemInventoryController 
{
    private final ItemInventoryService itemInventoryService;

    //Constructor injection for the ItemInventoryService
    public ItemInventoryController(ItemInventoryService itemInventoryService) 
    {
        this.itemInventoryService = itemInventoryService;
    }

    @PostMapping    //Endpoint to create a new ItemInventory
    public ResponseEntity<ItemInventoryDTO> createItemInventory(@RequestBody ItemInventoryDTO itemInventoryDTO) {
        try 
        {
            //Log the request details for creating an ItemInventory
            log.info("Creating ItemInventory with details: {}", itemInventoryDTO);
            
            //Call service to create the item inventory and return the created DTO
            ItemInventoryDTO createdItemInventory = itemInventoryService.createItemInventory(itemInventoryDTO);
            return new ResponseEntity<>(createdItemInventory, HttpStatus.CREATED);  //Return created status
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //Log error if the item inventory could not be created
            log.error("Error creating ItemInventory: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //Return NOT_FOUND if exception is thrown
        }
    }

    @GetMapping("/{siid}")  //Endpoint to retrieve an ItemInventory by its primary key (siid)
    public ResponseEntity<ItemInventoryDTO> getItemInventory(@PathVariable Long siid) 
    {
        try 
        {
            //Log the request details for retrieving an ItemInventory
            log.info("Retrieving ItemInventory with siid: {}", siid);
            
            //Call service to get the item inventory by its siid
            ItemInventoryDTO itemInventoryDTO = itemInventoryService.getItemInventory(siid);
            return new ResponseEntity<>(itemInventoryDTO, HttpStatus.OK);   //Return OK status with the item inventory
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //Log error if item inventory is not found
            log.error("ItemInventory with siid {} not found: {}", siid, e.getMessage());
            return ResponseEntity.notFound().build();   //Return NOT_FOUND if item inventory doesn't exist
        }
    }

    @GetMapping //Endpoint to retrieve all ItemInventories
    public ResponseEntity<List<ItemInventoryDTO>> getAllItemInventories() 
    {
        //Log the request to retrieve all item inventories
        log.info("Request to retrieve all ItemInventories");

        //Call service to get all item inventories
        List<ItemInventoryDTO> itemInventoryDTOList = itemInventoryService.getItemInventories();
        return new ResponseEntity<>(itemInventoryDTOList, HttpStatus.OK);   //Return OK status with the list of inventories
    }

    @PutMapping("/{siid}")  //Endpoint to update an existing ItemInventory
    public ResponseEntity<ItemInventoryDTO> updateItemInventory(@PathVariable Long siid, @RequestBody ItemInventoryDTO itemInventoryDTO) {
        try 
        {
            //Log the request details for updating an ItemInventory
            log.info("Updating ItemInventory with siid: {} and new details: {}", siid, itemInventoryDTO);
            
            //Call service to update the item inventory and return the updated DTO
            ItemInventoryDTO updatedItemInventory = itemInventoryService.updateItemInventory(siid, itemInventoryDTO);
            return new ResponseEntity<>(updatedItemInventory, HttpStatus.OK);   //Return OK status with the updated item
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //Log error if item inventory is not found for update
            log.error("ItemInventory with siid {} not found for update: {}", siid, e.getMessage());
            return ResponseEntity.notFound().build();   //Return NOT_FOUND if item inventory doesn't exist
        }
    }

    @DeleteMapping("/{siid}")   //Endpoint to delete an ItemInventory by its primary key (siid)
    public ResponseEntity<Void> deleteItemInventory(@PathVariable Long siid) 
    {
        try 
        {
            log.info("Deleting ItemInventory with siid: {}", siid); //Log the request to delete an ItemInventory  
            itemInventoryService.deleteItemInventory(siid);                 //Call service to delete the item inventory by its siid
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);             //Return NO_CONTENT status on successful deletion
        } 
        
        catch (ItemInventoryNotFoundException e) 
        {
            //Log error if item inventory is not found for deletion
            log.error("ItemInventory with siid {} not found for deletion: {}", siid, e.getMessage());
            return ResponseEntity.notFound().build();   //Return NOT_FOUND if item inventory doesn't exist
        }
    }
}