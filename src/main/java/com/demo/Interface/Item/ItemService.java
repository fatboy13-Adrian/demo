package com.demo.Interface.Item;
import java.util.List;
import com.demo.DTO.Item.ItemDTO;

public interface ItemService 
{
    //Methods that create item, retrieve item by ID, retrieve all items, update existing item, and delete item by ID
    ItemDTO createItem(ItemDTO itemDTO);                    //Takes an ItemDTO object and returns the created ItemDTO
    ItemDTO getItem(Long iid);                              //Takes an item ID (iid) and returns the corresponding ItemDTO
    List<ItemDTO> getItems();                               //Returns a list of all ItemDTO objects
    ItemDTO partialUpdateItem(Long iid, ItemDTO itemDTO);   //Takes the iid and an ItemDTO with updated information and returns the updated ItemDTO
    void deleteItem(Long iid);                              //Takes the item ID (item) and performs the deletion, no return value
}
