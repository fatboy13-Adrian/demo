package com.demo.Service.Inventory;
import com.demo.DTO.Inventory.InventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Interface.Inventory.InventoryService;
import com.demo.Repository.Inventory.InventoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service //Marks this class as a Spring service component
public class InventoryServiceImpl implements InventoryService 
{
    private final InventoryRepository inventoryRepository;

    //Constructor-based dependency injection for InventoryRepository
    public InventoryServiceImpl(InventoryRepository inventoryRepository) 
    {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) 
    {
        //Converts DTO to entity, saves it in the repository, then converts it back to DTO
        return convertToDTO(inventoryRepository.save(convertToEntity(inventoryDTO)));
    }

    @Override
    public InventoryDTO getInventory(Long sid) 
    {
        //Retrieves inventory by ID and converts the entity to DTO
        Inventory inventory = findById(sid);
        return convertToDTO(inventory);
    }

    @Override
    public List<InventoryDTO> getInventories() 
    {
        //Retrieves all inventory records, converts them to DTOs, and returns as a list
        return inventoryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public InventoryDTO updateInventory(Long sid, InventoryDTO inventoryDTO) 
    {
        //Finds inventory by ID, updates stock quantity, saves, and returns updated DTO
        Inventory inventory = findById(sid);
        inventory.setStockQty(inventoryDTO.getStockQty());
        return convertToDTO(inventoryRepository.save(inventory));
    }

    @Override
    public void deleteInventory(Long sid) 
    {
        //Finds inventory by ID and deletes it from the repository
        Inventory inventory = findById(sid);
        inventoryRepository.delete(inventory);
    }

    //Helper method to retrieve an inventory entity by ID, throwing an exception if not found
    public Inventory findById(Long sid) 
    {
        return inventoryRepository.findById(sid).orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    //Converts Inventory entity to InventoryDTO
    public InventoryDTO convertToDTO(Inventory inventory) 
    {
        return new InventoryDTO(inventory.getSid(), inventory.getStockQty());
    }

    //Converts InventoryDTO to Inventory entity
    public Inventory convertToEntity(InventoryDTO inventoryDTO) 
    {
        return new Inventory(inventoryDTO.getSid(), inventoryDTO.getStockQty());
    }
}