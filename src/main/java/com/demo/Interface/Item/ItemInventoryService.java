package com.demo.Interface.Item;
import java.util.List;
import com.demo.DTO.Item.ItemInventoryDTO;

public interface ItemInventoryService 
{
    ItemInventoryDTO createItemInventory(ItemInventoryDTO itemInventoryDTO);            //Method to create a new ItemInventory entry from the provided DTO.
    ItemInventoryDTO getItemInventory(Long siid);                                       //Method to get a specific ItemInventory by its ID (siid).
    List<ItemInventoryDTO> getItemInventories();                                        //Method to get a list of all ItemInventory entries.
    ItemInventoryDTO updateItemInventory(Long siid, ItemInventoryDTO itemInventoryDTO); //Method to update an existing ItemInventory entry using the provided DTO.
    void deleteItemInventory(Long siid);                                                //Method to delete an ItemInventory entry by its ID (siid).
}