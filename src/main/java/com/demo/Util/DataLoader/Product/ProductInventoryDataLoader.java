package com.demo.Util.DataLoader.Product;

import com.demo.Entity.Inventory.Inventory;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Product.ProductInventory;
import com.demo.Repository.Inventory.InventoryRepository;
import com.demo.Repository.Product.ProductInventoryRepository;
import com.demo.Repository.Product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class ProductInventoryDataLoader implements CommandLineRunner 
{
    private final ProductRepository productRepository;                    //Repository to access Product data
    private final InventoryRepository inventoryRepository;                //Repository to access Inventory data
    private final ProductInventoryRepository productInventoryRepository;  //Repository to access ProductInventory data

    //Constructor for dependency injection of the repositories
    public ProductInventoryDataLoader(ProductRepository productRepository, InventoryRepository inventoryRepository, ProductInventoryRepository productInventoryRepository) 
    {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.productInventoryRepository = productInventoryRepository;
    }

    @Override
    public void run(String... args) 
    {
        //Proceed only if no ProductInventory records exist in the database
        if(productInventoryRepository.count() == 0)
            linkExistingProductsWithInventories();     //Trigger the method to link products with inventories
    }

    @Transactional //Ensures that all operations in this method are executed as a single transaction
    private void linkExistingProductsWithInventories() 
    {
        List<Product> products = productRepository.findAll();              //Fetch all products from the database
        List<Inventory> inventories = inventoryRepository.findAll();      //Fetch all inventories from the database

        //Early exit if either Products or Inventories are missing
        if(products.isEmpty() || inventories.isEmpty()) 
        {
            //Log message if no products or inventories are found
            System.out.println("No existing Products or Inventories found. Skipping linkage."); 
            return; //Exit the method if there are no products or inventories
        }

        //Link Products to Inventories based on the desired pattern
        int inventoryIndex = 0; //Start from the first inventory (index 0)

        //Iterate through all products and associate them with inventories
        for (Product product : products) 
        {
            //Get the current inventory using the inventoryIndex, linking the product to the inventory
            Inventory inventory = inventories.get(inventoryIndex); 

            //Create and save the association between product and inventory
            createAndSaveProductInventoryLink(product, inventory);

            //Increment inventoryIndex and cycle back to the first inventory if needed
            inventoryIndex = (inventoryIndex + 1) % inventories.size(); //Cycle through inventories
        }

        //Log the linkage status after processing all products
        System.out.println("Linked " + products.size() + " products with inventories.");
    }

    //Helper method to create and save ProductInventory link
    private void createAndSaveProductInventoryLink(Product product, Inventory inventory) 
    {
        //Create a new ProductInventory instance that links the current Product and Inventory
        ProductInventory productInventory = ProductInventory.builder().pid(product).sid(inventory).build();

        //Save the newly created ProductInventory instance to the database
        productInventoryRepository.save(productInventory);

        //Log the successful linkage between the Product and Inventory
        System.out.println("Linked Product: " + product.getProductName() + " with Inventory ID: " + inventory.getSid());
    }
}
