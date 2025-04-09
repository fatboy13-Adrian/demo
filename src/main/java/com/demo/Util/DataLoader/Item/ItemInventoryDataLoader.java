package com.demo.Util.DataLoader.Item;

import com.demo.Entity.Item;
import com.demo.Entity.Inventory;
import com.demo.Entity.ItemInventory;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.InventoryRepository;
import com.demo.Repository.ItemInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class ItemInventoryDataLoader implements CommandLineRunner 
{
    private final ItemRepository itemRepository;                    //Repository to access Item data
    private final InventoryRepository inventoryRepository;          //Repository to access Inventory data
    private final ItemInventoryRepository itemInventoryRepository;  //Repository to access ItemInventory data

    //Constructor for dependency injection of the repositories
    public ItemInventoryDataLoader(ItemRepository itemRepository, InventoryRepository inventoryRepository, ItemInventoryRepository itemInventoryRepository) 
    {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    @Override
    public void run(String... args) 
    {
        //Proceed only if no ItemInventory records exist in the database
        if(itemInventoryRepository.count() == 0)
            linkExistingItemsWithInventories();     //Trigger the method to link items with inventories
    }

    @Transactional //Ensures that all operations in this method are executed as a single transaction
    private void linkExistingItemsWithInventories() 
    {
        List<Item> items = itemRepository.findAll();                    //Fetch all items from the database
        List<Inventory> inventories = inventoryRepository.findAll();    //Fetch all inventories from the database

        //Early exit if either Items or Inventories are missing
        if(items.isEmpty() || inventories.isEmpty()) 
        {
            //Log message if no items or inventories are found
            System.out.println("No existing Items or Inventories found. Skipping linkage."); 
            return; //Exit the method if there are no items or inventories
        }

        //Link Items to Inventories based on the desired pattern
        int inventoryIndex = 0; //Start from the first inventory (index 0)

        //Iterate through all items and associate them with inventories
        for (Item item : items) 
        {
            //Get the current inventory using the inventoryIndex, linking the item to the inventory
            Inventory inventory = inventories.get(inventoryIndex); 

            //Create and save the association between item and inventory
            createAndSaveItemInventoryLink(item, inventory);

            //Increment inventoryIndex and cycle back to the first inventory if needed
            inventoryIndex = (inventoryIndex + 1) % inventories.size(); //Cycle through inventories
        }

        //Log the linkage status after processing all items
        System.out.println("Linked " + items.size() + " items with inventories.");
    }

    //Helper method to create and save ItemInventory link
    private void createAndSaveItemInventoryLink(Item item, Inventory inventory) 
    {
        //Create a new ItemInventory instance that links the current Item and Inventory
        ItemInventory itemInventory = ItemInventory.builder().iid(item).sid(inventory).build();

        //Save the newly created ItemInventory instance to the database
        itemInventoryRepository.save(itemInventory);

        //Log the successful linkage between the Item and Inventory
        System.out.println("Linked Item: " + item.getItemName() + " with Inventory ID: " + inventory.getSid());
    }
}