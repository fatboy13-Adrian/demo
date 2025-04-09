package com.demo.Interface.Inventory;
import java.util.List;
import com.demo.DTO.Inventory.InventoryDTO;

public interface InventoryService 
{
    //Methods that create inventory, retrieve inventory by ID, retrieve all inventory, update existing inventory, and delete inventory by ID
    InventoryDTO createInventory(InventoryDTO inventoryDTO);                //Takes an InventoryDTO object and returns the created InventoryDTO
    InventoryDTO getInventory(Long sid);                                    //Takes an InventoryDTO (sid) and returns the corresponding InventoryDTO
    List<InventoryDTO> getInventories();                                    //Returns a list of all InventoryDTO objects
    InventoryDTO updateInventory(Long sid, InventoryDTO inventoryDTO);      //Takes the sid and an InventoryDTO with updated information and returns the updated InventoryDTO
    void deleteInventory(Long sid);                                         //Takes the inventory ID (inventory) and performs the deletion, no return value
}
