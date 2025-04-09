package com.demo.Util.DataLoader.Item;
import com.demo.Entity.Item;
import com.demo.Repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component  //Marks this class as a Spring component to be automatically detected and registered as a bean
public class ItemDataLoader implements CommandLineRunner 
{
    private final ItemRepository itemRepository;    //Repository to interact with the Item database

    //Constructor injection to inject the ItemRepository
    public ItemDataLoader(ItemRepository itemRepository) 
    {
        this.itemRepository = itemRepository;
    }

    @Override   //This method will run after the Spring Boot application starts
    public void run(String... args) throws Exception 
    {
        //Check if there are existing items in the database before loading new ones
        if (itemRepository.count() == 0) 
            loadInitialData();  //Load initial data if no items exist

    }

    //Helper method to load initial items into the database
    private void loadInitialData() 
    {
        //Create sample items with some initial data
        Item item1 = Item.builder().itemName("Laptop").unitPrice(new BigDecimal("1200.00")).build();
        Item item2 = Item.builder().itemName("Smartphone").unitPrice(new BigDecimal("800.00")).build();
        Item item3 = Item.builder().itemName("Headphones").unitPrice(new BigDecimal("150.00")).build();

        //Save the items to the database using the repository
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        System.out.println("Sample items have been loaded into the database."); //Log message to indicate completion
    }
}