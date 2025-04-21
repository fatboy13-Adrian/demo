package com.demo.Controller.Product;

import com.demo.DTO.Product.ProductInventoryDTO;
import com.demo.Exception.Product.ProductInventoryNotFoundException;
import com.demo.Interface.Product.ProductInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductInventoryControllerTest 
{
    @InjectMocks
    private ProductInventoryController productInventoryController;    //Injecting the controller and its dependencies

    @Mock
    private ProductInventoryService productInventoryService;          //Mocking the service layer

    private ProductInventoryDTO productInventoryDTO;                  //DTO instance for testing

    @BeforeEach
    public void setUp() 
    {
        //Initializing the DTO with sample data before each test
        productInventoryDTO = new ProductInventoryDTO(1L, 101L, 202L);
    }

    @Test   //Positive Test Case - Create ProductInventory
    public void testCreateProductInventory() 
    {
        //Mocking the service call to return the same DTO when create is invoked
        when(productInventoryService.createProductInventory(productInventoryDTO)).thenReturn(productInventoryDTO);

        //Invoking the controller's create method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.createProductInventory(productInventoryDTO);

        //Asserting the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Create ProductInventory (ProductInventoryNotFoundException)
    public void testCreateProductInventory_NotFound() 
    {
        //Mocking the service to throw an exception
        when(productInventoryService.createProductInventory(productInventoryDTO)).thenThrow(new ProductInventoryNotFoundException(1L));

        //Invoking the controller's create method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.createProductInventory(productInventoryDTO);

        //Asserting the response status is NOT_FOUND when exception is thrown
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive Test Case - Get ProductInventory by siid
    public void testGetProductInventory() 
    {
        //Mocking the service call to return the DTO for a given siid
        when(productInventoryService.getProductInventory(1L)).thenReturn(productInventoryDTO);

        //Invoking the controller's get method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.getProductInventory(1L);

        //Asserting the response status and the body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Get ProductInventory by siid (ProductInventoryNotFoundException)
    public void testGetProductInventory_NotFound() 
    {
        //Mocking the service to throw an exception when the product inventory is not found
        when(productInventoryService.getProductInventory(1L)).thenThrow(new ProductInventoryNotFoundException(1L));

        //Invoking the controller's get method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.getProductInventory(1L);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test   //Positive Test Case - Get All ProductInventories
    public void testGetAllProductInventories() 
    {
        //Mocking the service call to return a list of product inventories
        List<ProductInventoryDTO> productInventoryDTOList = Arrays.asList(productInventoryDTO);
        when(productInventoryService.getProductInventories()).thenReturn(productInventoryDTOList);

        //Invoking the controller's getAll method
        ResponseEntity<List<ProductInventoryDTO>> response = productInventoryController.getAllProductInventories();

        //Asserting the response status and the returned list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productInventoryDTOList, response.getBody());
    }

    @Test   //Positive Test Case - Update ProductInventory
    public void testUpdateProductInventory() 
    {
        //Mocking the service to return the updated DTO
        when(productInventoryService.updateProductInventory(1L, productInventoryDTO)).thenReturn(productInventoryDTO);

        //Invoking the controller's update method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.updateProductInventory(1L, productInventoryDTO);

        //Asserting the response status and updated DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Update ProductInventory (ProductInventoryNotFoundException)
    public void testUpdateProductInventory_NotFound() 
    {
        //Mocking the service to throw an exception when the product inventory is not found
        when(productInventoryService.updateProductInventory(1L, productInventoryDTO)).thenThrow(new ProductInventoryNotFoundException(1L));

        //Invoking the controller's update method
        ResponseEntity<ProductInventoryDTO> response = productInventoryController.updateProductInventory(1L, productInventoryDTO);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive Test Case - Delete ProductInventory
    public void testDeleteProductInventory() 
    {
        //Mocking the service to do nothing when the product inventory is deleted
        doNothing().when(productInventoryService).deleteProductInventory(1L);

        //Invoking the controller's delete method
        ResponseEntity<Void> response = productInventoryController.deleteProductInventory(1L);

        //Asserting the response status is NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Negative Test Case - Delete ProductInventory (ProductInventoryNotFoundException)
    public void testDeleteProductInventory_NotFound() 
    {
        //Mocking the service to throw an exception when trying to delete a non-existent product inventory
        doThrow(new ProductInventoryNotFoundException(1L)).when(productInventoryService).deleteProductInventory(1L);

        //Invoking the controller's delete method
        ResponseEntity<Void> response = productInventoryController.deleteProductInventory(1L);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
