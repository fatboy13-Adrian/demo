package com.demo.Service;
import com.demo.DTO.ItemInventoryDTO;
import com.demo.Entity.Inventory;
import com.demo.Entity.Item;
import com.demo.Entity.ItemInventory;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Repository.ItemInventoryRepository;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.InventoryRepository;
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

@ExtendWith(MockitoExtension.class)
public class ItemInventoryServiceImplTest 
{
    @InjectMocks
    private ItemInventoryServiceImpl itemInventoryService;      //Injecting the service class to test

    @Mock
    private ItemInventoryRepository itemInventoryRepository;    //Mocking the ItemInventory repository

    @Mock
    private ItemRepository itemRepository;                      //Mocking the Item repository

    @Mock
    private InventoryRepository inventoryRepository;            //Mocking the Inventory repository

    private Item item;                                          //Item entity for testing
    private Inventory inventory;                                //Inventory entity for testing
    private ItemInventory itemInventory;                        //ItemInventory entity for testing
    private ItemInventoryDTO itemInventoryDTO;                  //DTO to be used in test methods

    @BeforeEach
    void setUp() 
    {
        item = new Item();
        item.setIid(1L);            //Assigning an ID to the item

        inventory = new Inventory();
        inventory.setSid(1L);       //Assigning an ID to the inventory

        //Creating an ItemInventory entity using builder pattern
        itemInventory = ItemInventory.builder().iid(item).sid(inventory).build();

        //Creating ItemInventoryDTO to match the ItemInventory entity
        itemInventoryDTO = ItemInventoryDTO.builder().iid(item.getIid()).sid(inventory.getSid()).build();
    }

    @Test   //Test case to validate the creation of ItemInventory
    void testCreateItemInventory() 
    {
        //Mocking the findById behavior for both Item and Inventory repositories
        when(itemRepository.findById(item.getIid())).thenReturn(Optional.of(item));
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));

        //Mocking the save behavior to return the created ItemInventory
        when(itemInventoryRepository.save(any(ItemInventory.class))).thenReturn(itemInventory);

        //Calling the service method and verifying the result
        ItemInventoryDTO createdItemInventoryDTO = itemInventoryService.createItemInventory(itemInventoryDTO);

        assertNotNull(createdItemInventoryDTO);                             //Ensure that the returned DTO is not null
        assertEquals(item.getIid(), createdItemInventoryDTO.getIid());      //Verify the item ID is correct
        assertEquals(inventory.getSid(), createdItemInventoryDTO.getSid()); //Verify the inventory ID is correct

        //Verifying the interaction with the repositories
        verify(itemRepository, times(1)).findById(item.getIid());
        verify(inventoryRepository, times(1)).findById(inventory.getSid());
        verify(itemInventoryRepository, times(1)).save(any(ItemInventory.class));
    }

    @Test   //Negative Test Case when Item is not found during creation
    void testCreateItemInventory_ItemNotFound() 
    {
        //Mocking Item repository to return empty for any ID
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Creating a DTO for a non-existing item
        ItemInventoryDTO nonExistentItemInventoryDTO = ItemInventoryDTO.builder().iid(999L).sid(inventory.getSid()).build();

        //Asserting that the exception is thrown when creating the ItemInventory
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.createItemInventory(nonExistentItemInventoryDTO));

        //Verifying that the repositories are called the expected number of times
        verify(itemRepository, times(1)).findById(anyLong());                           
        verify(inventoryRepository, times(0)).findById(anyLong());                      //No need to check inventory if item is not found
        verify(itemInventoryRepository, times(0)).save(any(ItemInventory.class));   //Save should not be attempted
    }

    @Test   //Positive Test Case for retrieving an existing ItemInventory by ID
    void testGetItemInventory() 
    {
        //Mocking the repository to return the itemInventory entity
        when(itemInventoryRepository.findById(1L)).thenReturn(Optional.of(itemInventory));

        //Calling the service method to fetch ItemInventory
        ItemInventoryDTO foundItemInventoryDTO = itemInventoryService.getItemInventory(1L);

        assertNotNull(foundItemInventoryDTO);                               //Verify result is not null
        assertEquals(item.getIid(), foundItemInventoryDTO.getIid());        //Check item ID matches
        assertEquals(inventory.getSid(), foundItemInventoryDTO.getSid());   //Check inventory ID matches

        //Verifying the repository interaction
        verify(itemInventoryRepository, times(1)).findById(1L);
    }

    @Test   //Negative Test Case for ItemInventory not found
    void testGetItemInventory_ItemInventoryNotFound() 
    {
        //Mocking the repository to return empty for a non-existent ID
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Assert that exception is thrown when trying to get a non-existent ItemInventory
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.getItemInventory(999L));

        //Verifying the repository interaction
        verify(itemInventoryRepository, times(1)).findById(anyLong());
    }

    @Test   //Test Case for retrieving all ItemInventories
    void testGetItemInventories() 
    {
        //Mocking the repository to return a list with one ItemInventory
        when(itemInventoryRepository.findAll()).thenReturn(List.of(itemInventory));

        //Calling the service to fetch all ItemInventories
        List<ItemInventoryDTO> itemInventoryDTOList = itemInventoryService.getItemInventories();

        assertNotNull(itemInventoryDTOList);                    //Ensure list is not null
        assertFalse(itemInventoryDTOList.isEmpty());            //Ensure list is not empty
        assertEquals(1, itemInventoryDTOList.size());   //Verify list has exactly one item

        //Verifying repository interaction
        verify(itemInventoryRepository, times(1)).findAll();
    }

    @Test   //Test case for updating an existing ItemInventory
    void testUpdateItemInventory() 
    {
        //Mocking findById for the itemInventory and its associated entities
        when(itemInventoryRepository.findById(1L)).thenReturn(Optional.of(itemInventory));
        when(itemRepository.findById(item.getIid())).thenReturn(Optional.of(item));
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));

        //Mocking save to return the updated itemInventory
        when(itemInventoryRepository.save(any(ItemInventory.class))).thenReturn(itemInventory);

        //Calling the update service method and asserting result
        ItemInventoryDTO updatedItemInventoryDTO = itemInventoryService.updateItemInventory(1L, itemInventoryDTO);

        assertNotNull(updatedItemInventoryDTO);
        assertEquals(item.getIid(), updatedItemInventoryDTO.getIid());
        assertEquals(inventory.getSid(), updatedItemInventoryDTO.getSid());

        //Verifying repository interactions
        verify(itemInventoryRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(item.getIid());
        verify(inventoryRepository, times(1)).findById(inventory.getSid());
        verify(itemInventoryRepository, times(1)).save(any(ItemInventory.class));
    }

    @Test   //Negative Test Case for updating a non-existent ItemInventory
    void testUpdateItemInventory_ItemInventoryNotFound() 
    {
        //Mocking the repository to return empty for non-existent itemInventory ID
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Assert exception is thrown for non-existent ItemInventory update
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.updateItemInventory(999L, itemInventoryDTO));

        verify(itemInventoryRepository, times(1)).findById(anyLong());  //Verifying repository interactions
        verify(itemRepository, times(0)).findById(anyLong());           //No need to check ItemRepository if not found
        verify(inventoryRepository, times(0)).findById(anyLong());      //No need to check InventoryRepository if not found
    }

    @Test   //Positive Test Case for deleting an ItemInventory
    void testDeleteItemInventory() 
    {
        //Mocking repository to return an existing ItemInventory for deletion
        when(itemInventoryRepository.findById(1L)).thenReturn(Optional.of(itemInventory));

        //Calling delete service method
        itemInventoryService.deleteItemInventory(1L);

        //Verifying the delete method was called once
        verify(itemInventoryRepository, times(1)).delete(any(ItemInventory.class));
    }

    @Test   //Negative Test Case for attempting to delete a non-existent ItemInventory
    void testDeleteItemInventory_ItemInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent itemInventory ID
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Assert exception is thrown when trying to delete a non-existent ItemInventory
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.deleteItemInventory(999L));

        verify(itemInventoryRepository, times(1)).findById(anyLong());  //Verifying repository interactions             
        verify(itemInventoryRepository, times(0)).delete(any(ItemInventory.class)); //No deletion attempt
    }
}