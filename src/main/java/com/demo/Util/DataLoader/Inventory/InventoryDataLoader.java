package com.demo.Util.DataLoader.Inventory;
import com.demo.Entity.Inventory;
import com.demo.Repository.InventoryRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component  //Marks this class as a Spring bean to be managed by the Spring container
public class InventoryDataLoader 
{
    private final InventoryRepository inventoryRepository;

    public InventoryDataLoader(InventoryRepository inventoryRepository) 
    {
        this.inventoryRepository = inventoryRepository;
    }

    @PostConstruct  //This method is called automatically after the bean is initialized
    public void loadData() 
    {
        //Check if the inventory is already populated, to avoid reloading data on every application start
        if (inventoryRepository.count() == 0)
            loadSampleData();   //Load some sample inventory records
    }

    private void loadSampleData() 
    {
        //Create sample inventory records using the builder pattern
        Inventory inventory1 = Inventory.builder().stockQty(100).build();
        Inventory inventory2 = Inventory.builder().stockQty(200).build();
        Inventory inventory3 = Inventory.builder().stockQty(150).build();

        //Save sample inventory records to the database
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        inventoryRepository.save(inventory3);

        System.out.println("Sample inventory data loaded successfully!");
    }
}