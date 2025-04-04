package com.demo.Service;
import com.demo.DTO.ItemInventoryDTO;
import com.demo.Entity.Inventory;
import com.demo.Entity.Item;
import com.demo.Entity.ItemInventory;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Interface.ItemInventoryService;
import com.demo.Repository.ItemInventoryRepository;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemInventoryServiceImpl implements ItemInventoryService 
{
    private final ItemInventoryRepository itemInventoryRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    //Constructor for dependency injection of repositories
    public ItemInventoryServiceImpl(ItemInventoryRepository itemInventoryRepository, ItemRepository itemRepository,InventoryRepository inventoryRepository) 
    {
        this.itemInventoryRepository = itemInventoryRepository;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    //Converts an ItemInventory entity to ItemInventoryDTO
    private ItemInventoryDTO toDTO(ItemInventory itemInventory) 
    {
        return ItemInventoryDTO.builder().iid(itemInventory.getIid().getIid()).sid(itemInventory.getSid().getSid()).build();
    }

    //Converts ItemInventoryDTO to ItemInventory entity
    private ItemInventory toEntity(ItemInventoryDTO itemInventoryDTO) 
    {
        //Find the Item and Inventory by their respective IDs
        Item item = itemRepository.findById(itemInventoryDTO.getIid()).orElseThrow(() -> new ItemInventoryNotFoundException(itemInventoryDTO.getIid()));
        Inventory inventory = inventoryRepository.findById(itemInventoryDTO.getSid()).orElseThrow(() -> new ItemInventoryNotFoundException(itemInventoryDTO.getSid()));

        //Build and return ItemInventory entity
        return ItemInventory.builder().iid(item).sid(inventory).build();
    }

    @Override   //Creates a new ItemInventory and returns the DTO of the created entity
    public ItemInventoryDTO createItemInventory(ItemInventoryDTO itemInventoryDTO) 
    {
        //Convert DTO to entity and save it to the repository
        ItemInventory itemInventory = toEntity(itemInventoryDTO);

        //Save entity and map saved entity to DTO
        ItemInventory savedEntity = itemInventoryRepository.save(itemInventory);
        return toDTO(savedEntity); //Return saved entity as DTO
    }
    
    @Override   //Retrieves a specific ItemInventory by its ID and returns the DTO
    public ItemInventoryDTO getItemInventory(Long siid) 
    {
        //Fetch ItemInventory entity from the repository
        ItemInventory itemInventory = itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));
        return toDTO(itemInventory);    //Return the ItemInventory as a DTO
    }

    @Override   //Retrieves all ItemInventories and returns them as a list of DTOs
    public List<ItemInventoryDTO> getItemInventories() 
    {
        //Retrieve all ItemInventory entities, map them to DTOs, and return
        return itemInventoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override   //Updates an existing ItemInventory and returns the updated DTO
    public ItemInventoryDTO updateItemInventory(Long siid, ItemInventoryDTO itemInventoryDTO) 
    {
        //Find existing ItemInventory by ID
        ItemInventory existingItemInventory = itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));

        //Convert DTO to entity and update the existing entity
        ItemInventory updatedEntity = toEntity(itemInventoryDTO);
        existingItemInventory.setIid(updatedEntity.getIid());
        existingItemInventory.setSid(updatedEntity.getSid());

        //Save updated entity and return it as DTO
        ItemInventory savedEntity = itemInventoryRepository.save(existingItemInventory);
        return toDTO(savedEntity);
    }

    @Override   //Deletes an ItemInventory by its ID
    public void deleteItemInventory(Long siid) 
    {
        //Find the existing ItemInventory by ID
        ItemInventory existingItemInventory = itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));

        //Delete the ItemInventory entity
        itemInventoryRepository.delete(existingItemInventory);  //Cascade delete on related entities if configured
    }
}