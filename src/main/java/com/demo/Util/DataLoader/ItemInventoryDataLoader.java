package com.demo.Util.DataLoader;
import com.demo.Entity.Item;
import com.demo.Entity.Inventory;
import com.demo.Entity.ItemInventory;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.InventoryRepository;
import com.demo.Repository.ItemInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component  //Marks this class as a Spring component to be automatically detected and registered as a bean
public class ItemInventoryDataLoader implements CommandLineRunner 
{
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final ItemInventoryRepository itemInventoryRepository;

    //Constructor for dependency injection of the repositories
    public ItemInventoryDataLoader(ItemRepository itemRepository, InventoryRepository inventoryRepository,ItemInventoryRepository itemInventoryRepository) 
    {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    @Override
    public void run(String... args) throws Exception 
    {
        //Check if ItemInventory data already exists in the database
        if (itemInventoryRepository.count() == 0) 
            linkExistingItemsWithInventories(); //If no records exist, link existing Items with Inventories
    }

    //Method to link Items with Inventories and save them as ItemInventory records
    private void linkExistingItemsWithInventories() 
    {
        //Fetch all Item and Inventory entities from the database
        List<Item> items = itemRepository.findAll();
        List<Inventory> inventories = inventoryRepository.findAll();

        //Check if either Items or Inventories are empty
        if (items.isEmpty() || inventories.isEmpty()) 
        {
            //Log a message if no Items or Inventories are found
            System.out.println("No existing Items or Inventories found. Skipping linkage.");
            return;
        }

        //Determine the minimum size between Items and Inventories list to avoid IndexOutOfBoundsException
        int minSize = Math.min(items.size(), inventories.size());

        //Loop through the lists and link each Item with an Inventory
        for (int i = 0; i < minSize; i++) 
        {
            Item item = items.get(i);                   //Get the current Item
            Inventory inventory = inventories.get(i);   //Get the current Inventory

            //Create a new ItemInventory record that links the current Item and Inventory
            ItemInventory itemInventory = ItemInventory.builder().iid(item).sid(inventory).build();

            //Save the ItemInventory entity to the database
            itemInventoryRepository.save(itemInventory);

            //Log the successful linkage
            System.out.println("Linked Item: " + item.getItemName() + " with Inventory ID: " + inventory.getSid());
        }

        //Log a warning if the number of Items does not match the number of Inventories
        if (items.size() != inventories.size()) 
            System.out.println("Warning: Items and Inventories count mismatch. Only " + minSize + " links created.");
    }
}