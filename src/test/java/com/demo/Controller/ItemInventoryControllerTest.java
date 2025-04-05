package com.demo.Controller;
import com.demo.DTO.ItemInventoryDTO;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Interface.ItemInventoryService;
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
public class ItemInventoryControllerTest 
{
    @InjectMocks
    private ItemInventoryController itemInventoryController;    //Injecting the controller and its dependencies

    @Mock
    private ItemInventoryService itemInventoryService;          //Mocking the service layer

    private ItemInventoryDTO itemInventoryDTO;                  //DTO instance for testing

    @BeforeEach
    public void setUp() 
    {
        //Initializing the DTO with sample data before each test
        itemInventoryDTO = new ItemInventoryDTO(1L, 101L, 202L);
    }

    @Test   //Positive Test Case - Create ItemInventory
    public void testCreateItemInventory() 
    {
        //Mocking the service call to return the same DTO when create is invoked
        when(itemInventoryService.createItemInventory(itemInventoryDTO)).thenReturn(itemInventoryDTO);

        //Invoking the controller's create method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.createItemInventory(itemInventoryDTO);

        //Asserting the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(itemInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Create ItemInventory (ItemInventoryNotFoundException)
    public void testCreateItemInventory_NotFound() 
    {
        //Mocking the service to throw an exception
        when(itemInventoryService.createItemInventory(itemInventoryDTO)).thenThrow(new ItemInventoryNotFoundException(1L));

        //Invoking the controller's create method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.createItemInventory(itemInventoryDTO);

        //Asserting the response status is NOT_FOUND when exception is thrown
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive Test Case - Get ItemInventory by siid
    public void testGetItemInventory() 
    {
        //Mocking the service call to return the DTO for a given siid
        when(itemInventoryService.getItemInventory(1L)).thenReturn(itemInventoryDTO);

        //Invoking the controller's get method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.getItemInventory(1L);

        //Asserting the response status and the body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Get ItemInventory by siid (ItemInventoryNotFoundException)
    public void testGetItemInventory_NotFound() 
    {
        //Mocking the service to throw an exception when the item inventory is not found
        when(itemInventoryService.getItemInventory(1L)).thenThrow(new ItemInventoryNotFoundException(1L));

        //Invoking the controller's get method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.getItemInventory(1L);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test   //Positive Test Case - Get All ItemInventories
    public void testGetAllItemInventories() 
    {
        //Mocking the service call to return a list of item inventories
        List<ItemInventoryDTO> itemInventoryDTOList = Arrays.asList(itemInventoryDTO);
        when(itemInventoryService.getItemInventories()).thenReturn(itemInventoryDTOList);

        //Invoking the controller's getAll method
        ResponseEntity<List<ItemInventoryDTO>> response = itemInventoryController.getAllItemInventories();

        //Asserting the response status and the returned list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemInventoryDTOList, response.getBody());
    }

    @Test   //Positive Test Case - Update ItemInventory
    public void testUpdateItemInventory() 
    {
        //Mocking the service to return the updated DTO
        when(itemInventoryService.updateItemInventory(1L, itemInventoryDTO)).thenReturn(itemInventoryDTO);

        //Invoking the controller's update method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.updateItemInventory(1L, itemInventoryDTO);

        //Asserting the response status and updated DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemInventoryDTO, response.getBody());
    }

    @Test   //Negative Test Case - Update ItemInventory (ItemInventoryNotFoundException)
    public void testUpdateItemInventory_NotFound() 
    {
        //Mocking the service to throw an exception when the item inventory is not found
        when(itemInventoryService.updateItemInventory(1L, itemInventoryDTO)).thenThrow(new ItemInventoryNotFoundException(1L));

        //Invoking the controller's update method
        ResponseEntity<ItemInventoryDTO> response = itemInventoryController.updateItemInventory(1L, itemInventoryDTO);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Positive Test Case - Delete ItemInventory
    public void testDeleteItemInventory() 
    {
        //Mocking the service to do nothing when the item inventory is deleted
        doNothing().when(itemInventoryService).deleteItemInventory(1L);

        //Invoking the controller's delete method
        ResponseEntity<Void> response = itemInventoryController.deleteItemInventory(1L);

        //Asserting the response status is NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Negative Test Case - Delete ItemInventory (ItemInventoryNotFoundException)
    public void testDeleteItemInventory_NotFound() 
    {
        //Mocking the service to throw an exception when trying to delete a non-existent item inventory
        doThrow(new ItemInventoryNotFoundException(1L)).when(itemInventoryService).deleteItemInventory(1L);

        //Invoking the controller's delete method
        ResponseEntity<Void> response = itemInventoryController.deleteItemInventory(1L);

        //Asserting the response status is NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}