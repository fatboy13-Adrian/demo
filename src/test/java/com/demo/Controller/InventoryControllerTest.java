package com.demo.Controller;
import com.demo.DTO.InventoryDTO;
import com.demo.Interface.InventoryService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Enable Mockito support for JUnit tests
public class InventoryControllerTest 
{
    @Mock //Mocking the InventoryService to simulate its behavior without actually calling the real service
    private InventoryService inventoryService;

    @InjectMocks //Automatically injects the mocked inventoryService into the InventoryController
    private InventoryController inventoryController;

    private InventoryDTO inventoryDTO;  //Declaring an InventoryDTO for use in test cases

    @BeforeEach //Before each test, initialize the InventoryDTO instance
    void setUp() 
    {
        inventoryDTO = new InventoryDTO(1L, 10);    //Setup test inventory with sid=1L and stockQty=10
    }
    
    @Test   //Positive test case: Creating inventory with valid data
    void testCreateInventory_Success() 
    {
        //Mock the service method to return the same DTO when a valid InventoryDTO is passed
        when(inventoryService.createInventory(any(InventoryDTO.class))).thenReturn(inventoryDTO);
        
        //Call the controller method to create the inventory
        ResponseEntity<?> response = inventoryController.createInventory(inventoryDTO);
        
        //Assert that the response status is HTTP CREATED (201) indicating successful creation
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Assert that the response body is not null
        assertNotNull(response.getBody());
    }

    @Test   //Negative test case: Creating inventory with zero stock (invalid case)
    void testCreateInventory_Fail_ZeroStock() 
    {
        InventoryDTO invalidDTO = new InventoryDTO(1L, 0); //Invalid DTO with zero stock quantity
        
        //Call the controller method to create the inventory
        ResponseEntity<?> response = inventoryController.createInventory(invalidDTO);
        
        //Assert that the response status is HTTP BAD REQUEST (400) indicating invalid input
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        //Assert that the response body contains the appropriate error message
        assertEquals("Stock inventory cannot be null or zero", response.getBody());
    }

    @Test   //Positive test case: Getting inventory by ID
    void testGetInventory_Success() 
    {
        //Mock the service method to return the inventoryDTO when searching by id=1
        when(inventoryService.getInventory(1L)).thenReturn(inventoryDTO);
        
        //Call the controller method to get the inventory by id
        ResponseEntity<InventoryDTO> response = inventoryController.getInventory(1L);
        
        //Assert that the response status is HTTP OK (200) indicating success
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assert that the response body is not null (inventory found)
        assertNotNull(response.getBody());
    }

    @Test   //Negative test case: Trying to get a non-existing inventory (ID 2)
    void testGetInventory_NotFound() 
    {
        //Mock the service method to return null when searching for a non-existing inventory
        when(inventoryService.getInventory(2L)).thenReturn(null);
        
        //Call the controller method to get the inventory by id
        ResponseEntity<InventoryDTO> response = inventoryController.getInventory(2L);
        
        //Assert that the response status is HTTP NOT FOUND (404) indicating inventory does not exist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive test case: Getting all inventories
    void testGetInventories_Success() 
    {
        //Prepare a list of inventories (for now, just one inventoryDTO)
        List<InventoryDTO> inventories = Arrays.asList(inventoryDTO);
        
        //Mock the service method to return the list of inventories
        when(inventoryService.getInventories()).thenReturn(inventories);
        
        //Call the controller method to get all inventories
        ResponseEntity<List<InventoryDTO>> response = inventoryController.getInventories();
        
        //Assert that the response status is HTTP OK (200) indicating success
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assert that the response body (list of inventories) is not empty
        assertFalse(response.getBody().isEmpty());
    }

    @Test   //Positive test case: Updating an existing inventory
    void testUpdateInventory_Success() 
    {
        InventoryDTO updatedDTO = new InventoryDTO(1L, 20); //DTO with updated stock quantity
        
        //Mock the service method to return the updated inventoryDTO
        when(inventoryService.updateInventory(eq(1L), any(InventoryDTO.class))).thenReturn(updatedDTO);
        
        //Call the controller method to update the inventory by id
        ResponseEntity<InventoryDTO> response = inventoryController.updateInventory(1L, updatedDTO);
        
        //Assert that the response status is HTTP OK (200) indicating success
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Assert that the updated stock quantity in the response is 20
        assertEquals(20, response.getBody().getStockQty());
    }

    @Test   //Negative test case: Updating a non-existing inventory (ID 2)
    void testUpdateInventory_NotFound() 
    {
        //Mock the service method to return null when trying to update a non-existing inventory
        when(inventoryService.updateInventory(eq(2L), any(InventoryDTO.class))).thenReturn(null);
        
        //Call the controller method to update the inventory by id
        ResponseEntity<InventoryDTO> response = inventoryController.updateInventory(2L, inventoryDTO);
        
        //Assert that the response status is HTTP NOT FOUND (404) indicating inventory does not exist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive test case: Deleting an inventory
    void testDeleteInventory_Success() 
    {
        //Mock the service method to simulate successful deletion (no exception)
        doNothing().when(inventoryService).deleteInventory(1L);
        
        //Call the controller method to delete the inventory by id
        ResponseEntity<Void> response = inventoryController.deleteInventory(1L);
        
        //Assert that the response status is HTTP NO CONTENT (204) indicating successful deletion
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Negative test case: Deleting a non-existing inventory (ID 2)
    void testDeleteInventory_NotFound() 
    {
        //Mock the service method to throw an exception when trying to delete a non-existing inventory
        doThrow(new IllegalArgumentException()).when(inventoryService).deleteInventory(2L);
        
        //Call the controller method to delete the inventory by id
        ResponseEntity<Void> response = inventoryController.deleteInventory(2L);
        
        //Assert that the response status is HTTP NOT FOUND (404) indicating inventory does not exist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}