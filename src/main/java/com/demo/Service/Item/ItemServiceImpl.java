package com.demo.Service.Item;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.demo.DTO.Item.ItemDTO;
import com.demo.Entity.Item.Item;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Interface.Item.ItemService;
import com.demo.Repository.Item.ItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service //Marks this class as a service that contains business logic for item management
public class ItemServiceImpl implements ItemService 
{
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);    //Logger for logging information
    private final ItemRepository itemRepository;                                                //Repository to handle database interactions for Item entity

    //Constructor injection for the ItemRepository, a common Spring approach for dependency injection
    public ItemServiceImpl(ItemRepository itemRepository) 
    {
        this.itemRepository = itemRepository;
    }

    @Override   //Creates a new item using the provided DTO and saves it to the database
    public ItemDTO createItem(ItemDTO itemDTO) 
    {
        Item newItem = convertToEntity(itemDTO);                                    //Convert DTO to Entity
        Item savedItem = itemRepository.save(newItem);                              //Save the new item to the database
        logger.info("Created new item with ID: {}", savedItem.getIid());    //Log item creation
        return convertToDTO(savedItem);                                             //Return the saved item as a DTO
    }

    @Override   //Retrieves an item by its ID
    public ItemDTO getItem(Long iid) 
    {
        Item item = findById(iid);  //Fetch the item by its ID
        return convertToDTO(item);  //Convert the entity to DTO and return
    }

    @Override   //Retrieves a list of all items from the database
    public List<ItemDTO> getItems() 
    {
        //Convert the list of items from the database to DTOs and return
        return itemRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList()); //Conert each item to a DTO and collect into a list
    }

    @Override   //Partially updates an item by its ID using the provided DTO
    public ItemDTO partialUpdateItem(Long iid, ItemDTO itemDTO) 
    {
        Item existingItem = findById(iid);                      //Fetch the existing item by ID
        updateItemFields(existingItem, itemDTO);                //Update fields based on the provided DTO
        Item updatedItem = itemRepository.save(existingItem);   //Save the updated item
        logger.info("Updated item with ID: {}", iid);   //Log the update
        return convertToDTO(updatedItem);                       //Return the updated item as a DTO
    }

    @Override   //Deletes an item by its ID
    public void deleteItem(Long iid) 
    {
        Item item = findById(iid);                              //Find the item by ID
        itemRepository.delete(item);                            //Delete the item from the database
        logger.info("Deleted item with ID: {}", iid);   //Log the deletion
    }

    //Helper method to find an item by its ID and throw an exception if not found
    private Item findById(Long iid) 
    {
        //If item is not found, throw an ItemNotFoundException with a log message
        return itemRepository.findById(iid).orElseThrow(() -> 
        {
            logger.error("Item with ID {} not found", iid); //Log error if item not found
            return new ItemNotFoundException(iid);                  //Throw exception
        });
    }

    //Helper method to update fields of the item entity from the DTO
    private void updateItemFields(Item existingItem, ItemDTO itemDTO) 
    {
        //Only update if the field is not null and the value is valid
        if (itemDTO.getItemName() != null)
            existingItem.setItemName(itemDTO.getItemName());    //Update item name
        
        if (itemDTO.getUnitPrice() != null && itemDTO.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) 
            existingItem.setUnitPrice(itemDTO.getUnitPrice());  //Update unit price
    }

    //Helper method to convert an Item entity to an ItemDTO
    private ItemDTO convertToDTO(Item item) 
    {
        return new ItemDTO(item.getIid(), item.getItemName(), item.getUnitPrice());         //Return a DTO with item details
    }

    //Helper method to convert an ItemDTO to an Item entity
    private Item convertToEntity(ItemDTO itemDTO) 
    {
        return new Item(itemDTO.getIid(), itemDTO.getItemName(), itemDTO.getUnitPrice());   //Convert DTO to Entity
    }
}