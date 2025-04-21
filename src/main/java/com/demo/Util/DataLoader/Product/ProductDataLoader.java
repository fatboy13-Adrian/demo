package com.demo.Util.DataLoader.Product;
import com.demo.Entity.Product.Product;
import com.demo.Repository.Product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component  //Marks this class as a Spring component to be automatically detected and registered as a bean
public class ProductDataLoader implements CommandLineRunner 
{
    private final ProductRepository productRepository;    //Repository to interact with the Product database

    //Constructor injection to inject the ProductRepository
    public ProductDataLoader(ProductRepository productRepository) 
    {
        this.productRepository = productRepository;
    }

    @Override   //This method will run after the Spring Boot application starts
    public void run(String... args) throws Exception 
    {
        //Check if there are existing products in the database before loading new ones
        if (productRepository.count() == 0) 
            loadInitialData();  //Load initial data if no products exist
    }

    //Helper method to load initial products into the database
    private void loadInitialData() 
    {
        //Create sample products with some initial data
        Product product1 = Product.builder().productName("Laptop").unitPrice(new BigDecimal("1200.00")).build();
        Product product2 = Product.builder().productName("Smartphone").unitPrice(new BigDecimal("800.00")).build();
        Product product3 = Product.builder().productName("Headphones").unitPrice(new BigDecimal("150.00")).build();

        //Save the products to the database using the repository
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        System.out.println("Sample products have been loaded into the database."); //Log message to indicate completion
    }
}
