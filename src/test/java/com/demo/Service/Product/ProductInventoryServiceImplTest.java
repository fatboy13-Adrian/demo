package com.demo.Service.Product;
import com.demo.DTO.Product.ProductInventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Product.ProductInventory;
import com.demo.Exception.Product.ProductInventoryNotFoundException;
import com.demo.Repository.Inventory.InventoryRepository;
import com.demo.Repository.Product.ProductInventoryRepository;
import com.demo.Repository.Product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //This annotation tells JUnit to use Mockito for testing.
public class ProductInventoryServiceImplTest 
{
    @InjectMocks
    private ProductInventoryServiceImpl productInventoryService;      //Injects mock dependencies into the service

    @Mock
    private ProductInventoryRepository productInventoryRepository;    //Mocks the ProductInventory repository

    @Mock
    private ProductRepository productRepository;                      //Mocks the Product repository

    @Mock
    private InventoryRepository inventoryRepository;            //Mocks the Inventory repository

    private Product product;                                          //Test object for Product
    private Inventory inventory;                                //Test object for Inventory
    private ProductInventory productInventory;                        //Test object for ProductInventory
    private ProductInventoryDTO productInventoryDTO;                  //Test object for ProductInventoryDTO

    @BeforeEach
    void setUp() 
    {
        //Setting up test data before each test method is executed.
        product = new Product();
        product.setPid(1L);            //Setting Product ID

        inventory = new Inventory();
        inventory.setSid(1L);       //Setting Inventory ID

        //Building the ProductInventory object with associations
        productInventory = ProductInventory.builder().psid(100L).pid(product).sid(inventory).build();

        //Building the DTO object corresponding to ProductInventory
        productInventoryDTO = ProductInventoryDTO.builder().psid(100L).pid(product.getPid())
        .sid(inventory.getSid()).build();
    }

    @Test
    void testCreateProductInventory() 
    {
        //Mocking repository calls to simulate behavior for successful creation
        when(productRepository.findById(product.getPid())).thenReturn(Optional.of(product));                 //Product exists
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));  //Inventory exists
        when(productInventoryRepository.save(any(ProductInventory.class))).thenReturn(productInventory); //Saving ProductInventory

        //Calling the service method to create ProductInventory
        ProductInventoryDTO createdDTO = productInventoryService.createProductInventory(productInventoryDTO);

        //Assertions to verify that the ProductInventory was created successfully
        assertNotNull(createdDTO);                              //Ensure that the result is not null
        assertEquals(100L, createdDTO.getPsid());       //Check siid
        assertEquals(product.getPid(), createdDTO.getPid());       //Check Product ID
        assertEquals(inventory.getSid(), createdDTO.getSid());  //Check Inventory ID

        //Verifying that the correct methods were called on the mock repositories
        verify(productRepository).findById(product.getPid());
        verify(inventoryRepository).findById(inventory.getSid());
        verify(productInventoryRepository).save(any(ProductInventory.class));
    }

    @Test
    void testCreateProductInventory_ProductNotFound() 
    {
        //Mocking repository to return an empty Optional for Product, simulating a Product not found scenario
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Creating a DTO with non-existent Product ID
        ProductInventoryDTO nonExistentDTO = ProductInventoryDTO.builder().psid(101L).pid(999L)
        .sid(1L).build();

        //Asserting that the method throws ProductInventoryNotFoundException when trying to create with a non-existent Product
        assertThrows(ProductInventoryNotFoundException.class, () -> productInventoryService.createProductInventory(nonExistentDTO));

        //Verifying that ProductRepository's findById method was called but others were not called
        verify(productRepository).findById(anyLong());
        verify(inventoryRepository, never()).findById(anyLong());                       //Inventory shouldn't be called
        verify(productInventoryRepository, never()).save(any(ProductInventory.class));    //Save shouldn't happen
    }

    @Test
    void testGetProductInventory() 
    {
        //Mocking repository to return a ProductInventory
        when(productInventoryRepository.findById(100L)).thenReturn(Optional.of(productInventory));

        //Calling the service method to retrieve ProductInventory
        ProductInventoryDTO found = productInventoryService.getProductInventory(100L);

        //Verifying the correct ProductInventory DTO was returned
        assertNotNull(found);                               //Ensure result is not null
        assertEquals(100L, found.getPsid());        //Check siid
        assertEquals(product.getPid(), found.getPid());        //Check Product ID
        assertEquals(inventory.getSid(), found.getSid());   //Check Inventory ID

        //Verifying that the repository's findById method was called
        verify(productInventoryRepository).findById(100L);
    }

    @Test
    void testGetProductInventory_ProductInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ProductInventory
        when(productInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ProductInventoryNotFoundException when productInventory is not found
        assertThrows(ProductInventoryNotFoundException.class, () -> productInventoryService.getProductInventory(999L));

        //Verifying that the repository's findById method was called
        verify(productInventoryRepository).findById(anyLong());
    }

    @Test
    void testGetProductInventories() 
    {
        //Mocking repository to return a list containing the productInventory
        when(productInventoryRepository.findAll()).thenReturn(List.of(productInventory));

        //Calling the service method to retrieve all ProductInventories
        List<ProductInventoryDTO> list = productInventoryService.getProductInventories();

        //Verifying the result is not empty and contains the expected productInventory
        assertNotNull(list);
        assertFalse(list.isEmpty());                                //List shouldn't be empty
        assertEquals(1, list.size());                   //Only one productInventory should be in the list
        assertEquals(100L, list.get(0).getPsid());  //Check siid

        //Verifying that the repository's findAll method was called
        verify(productInventoryRepository).findAll();
    }

    @Test
    void testUpdateProductInventory() 
    {
        //Mocking repository calls for Product, Inventory, and ProductInventory
        when(productInventoryRepository.findById(100L)).thenReturn(Optional.of(productInventory));
        when(productRepository.findById(product.getPid())).thenReturn(Optional.of(product));                 //Product exists
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));  //Inventory exists
        when(productInventoryRepository.save(any(ProductInventory.class))).thenReturn(productInventory);

        //Calling the service method to update ProductInventory
        ProductInventoryDTO updated = productInventoryService.updateProductInventory(100L, productInventoryDTO);

        //Verifying the updated values in the result
        assertNotNull(updated);
        assertEquals(100L, updated.getPsid());      //Check siid
        assertEquals(product.getPid(), updated.getPid());      //Check Product ID
        assertEquals(inventory.getSid(), updated.getSid()); //Check Inventory ID

        //Verifying the mock repository methods were called correctly
        verify(productInventoryRepository).findById(100L);
        verify(productRepository).findById(product.getPid());
        verify(inventoryRepository).findById(inventory.getSid());
        verify(productInventoryRepository).save(any(ProductInventory.class));
    }

    @Test
    void testUpdateProductInventory_ProductInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ProductInventory
        when(productInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ProductInventoryNotFoundException when productInventory is not found
        assertThrows(ProductInventoryNotFoundException.class, () -> productInventoryService.updateProductInventory(999L, productInventoryDTO));

        //Verifying that the repository's findById method was called but others were not called
        verify(productInventoryRepository).findById(anyLong());
        verify(productRepository, never()).findById(anyLong());        //Product shouldn't be looked up
        verify(inventoryRepository, never()).findById(anyLong());   //Inventory shouldn't be looked up
    }

    @Test
    void testDeleteProductInventory() 
    {
        //Mocking repository to return a ProductInventory
        when(productInventoryRepository.findById(100L)).thenReturn(Optional.of(productInventory));

        //Calling the service method to delete ProductInventory
        productInventoryService.deleteProductInventory(100L);

        //Verifying that the repository's delete method was called
        verify(productInventoryRepository).delete(any(ProductInventory.class));
    }

    @Test
    void testDeleteProductInventory_ProductInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ProductInventory
        when(productInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ProductInventoryNotFoundException when productInventory is not found
        assertThrows(ProductInventoryNotFoundException.class, () -> productInventoryService.deleteProductInventory(999L));

        //Verifying that the repository's findById method was called and delete was never called
        verify(productInventoryRepository).findById(anyLong());
        verify(productInventoryRepository, never()).delete(any(ProductInventory.class));  //Deletion shouldn't happen
    }
}
