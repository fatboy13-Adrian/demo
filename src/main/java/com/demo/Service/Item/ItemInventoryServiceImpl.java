package com.demo.Service.Item;
import com.demo.DTO.Item.ItemInventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Entity.Item.Item;
import com.demo.Entity.Item.ItemInventory;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Interface.Item.ItemInventoryService;
import com.demo.Repository.Inventory.InventoryRepository;
import com.demo.Repository.Item.ItemInventoryRepository;
import com.demo.Repository.Item.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional //Ensures the service method runs within a transaction context
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

    //Converts an ItemInventory entity to a DTO (Data Transfer Object)
    private ItemInventoryDTO toDTO(ItemInventory itemInventory) 
    {
        return ItemInventoryDTO.builder().siid(itemInventory.getSiid()).iid(itemInventory.getIid().getIid()).sid(itemInventory.getSid().getSid())
        .build();
    }

    //Converts a DTO to an ItemInventory entity
    private ItemInventory toEntity(ItemInventoryDTO dto) 
    {
        //Find the item using its ID from the repository or throw an exception if not found
        Item item = itemRepository.findById(dto.getIid()).orElseThrow(() -> new ItemInventoryNotFoundException(dto.getIid()));
        
        //Find the inventory using its ID from the repository or throw an exception if not found
        Inventory inventory = inventoryRepository.findById(dto.getSid()).orElseThrow(() -> new ItemInventoryNotFoundException(dto.getSid()));

        //Return a new ItemInventory entity built using the DTO values
        return ItemInventory.builder().siid(dto.getSiid()).iid(item) .sid(inventory).build();
    }

    @Override
    public ItemInventoryDTO createItemInventory(ItemInventoryDTO dto) 
    {
        ItemInventory entity = toEntity(dto);                           //Convert DTO to entity for saving
        ItemInventory saved = itemInventoryRepository.save(entity);     //Save the entity using the repository
        return toDTO(saved);                                            //Convert the saved entity back to DTO and return it
    }

    @Override
    public ItemInventoryDTO getItemInventory(Long siid) 
    {
        //Retrieve the item inventory by siid from the repository or throw exception if not found
        ItemInventory itemInventory = itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));
        return toDTO(itemInventory);    //Convert the entity to DTO and return it
    }

    @Override
    public List<ItemInventoryDTO> getItemInventories() 
    {
        //Retrieve all item inventories from the repository
        return itemInventoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList()); 
    }

    @Override
    public ItemInventoryDTO updateItemInventory(Long siid, ItemInventoryDTO dto) 
    {
        //Check if the item inventory exists by its siid, if not throw an exception
        itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));

        //Update the DTO to ensure siid is included for the update
        dto = ItemInventoryDTO.builder().siid(siid).iid(dto.getIid()).sid(dto.getSid()).build();

        ItemInventory updated = toEntity(dto);                          //Convert the updated DTO back to an entity for saving
        ItemInventory saved = itemInventoryRepository.save(updated);    //Save the updated entity using the repository
        return toDTO(saved);                                            //Convert the saved entity back to DTO and return it
    }

    @Override
    public void deleteItemInventory(Long siid) 
    {
        //Retrieve the item inventory by siid, or throw an exception if not found
        ItemInventory itemInventory = itemInventoryRepository.findById(siid).orElseThrow(() -> new ItemInventoryNotFoundException(siid));
        
        //Delete the item inventory from the repository
        itemInventoryRepository.delete(itemInventory);
    }
}