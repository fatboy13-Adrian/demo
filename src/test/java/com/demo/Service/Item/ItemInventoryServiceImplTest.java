package com.demo.Service.Item;
import com.demo.DTO.Item.ItemInventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Entity.Item.Item;
import com.demo.Entity.Item.ItemInventory;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Repository.Inventory.InventoryRepository;
import com.demo.Repository.Item.ItemInventoryRepository;
import com.demo.Repository.Item.ItemRepository;
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
public class ItemInventoryServiceImplTest 
{
    @InjectMocks
    private ItemInventoryServiceImpl itemInventoryService;      //Injects mock dependencies into the service

    @Mock
    private ItemInventoryRepository itemInventoryRepository;    //Mocks the ItemInventory repository

    @Mock
    private ItemRepository itemRepository;                      //Mocks the Item repository

    @Mock
    private InventoryRepository inventoryRepository;            //Mocks the Inventory repository

    private Item item;                                          //Test object for Item
    private Inventory inventory;                                //Test object for Inventory
    private ItemInventory itemInventory;                        //Test object for ItemInventory
    private ItemInventoryDTO itemInventoryDTO;                  //Test object for ItemInventoryDTO

    @BeforeEach
    void setUp() 
    {
        //Setting up test data before each test method is executed.
        item = new Item();
        item.setIid(1L);            //Setting Item ID

        inventory = new Inventory();
        inventory.setSid(1L);       //Setting Inventory ID

        //Building the ItemInventory object with associations
        itemInventory = ItemInventory.builder().siid(100L).iid(item).sid(inventory).build();

        //Building the DTO object corresponding to ItemInventory
        itemInventoryDTO = ItemInventoryDTO.builder().siid(100L).iid(item.getIid())
        .sid(inventory.getSid()).build();
    }

    @Test
    void testCreateItemInventory() 
    {
        //Mocking repository calls to simulate behavior for successful creation
        when(itemRepository.findById(item.getIid())).thenReturn(Optional.of(item));                 //Item exists
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));  //Inventory exists
        when(itemInventoryRepository.save(any(ItemInventory.class))).thenReturn(itemInventory); //Saving ItemInventory

        //Calling the service method to create ItemInventory
        ItemInventoryDTO createdDTO = itemInventoryService.createItemInventory(itemInventoryDTO);

        //Assertions to verify that the ItemInventory was created successfully
        assertNotNull(createdDTO);                              //Ensure that the result is not null
        assertEquals(100L, createdDTO.getSiid());       //Check siid
        assertEquals(item.getIid(), createdDTO.getIid());       //Check Item ID
        assertEquals(inventory.getSid(), createdDTO.getSid());  //Check Inventory ID

        //Verifying that the correct methods were called on the mock repositories
        verify(itemRepository).findById(item.getIid());
        verify(inventoryRepository).findById(inventory.getSid());
        verify(itemInventoryRepository).save(any(ItemInventory.class));
    }

    @Test
    void testCreateItemInventory_ItemNotFound() 
    {
        //Mocking repository to return an empty Optional for Item, simulating an Item not found scenario
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Creating a DTO with non-existent Item ID
        ItemInventoryDTO nonExistentDTO = ItemInventoryDTO.builder().siid(101L).iid(999L)
        .sid(1L).build();

        //Asserting that the method throws ItemInventoryNotFoundException when trying to create with a non-existent Item
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.createItemInventory(nonExistentDTO));

        //Verifying that ItemRepository's findById method was called but others were not called
        verify(itemRepository).findById(anyLong());
        verify(inventoryRepository, never()).findById(anyLong());                       //Inventory shouldn't be called
        verify(itemInventoryRepository, never()).save(any(ItemInventory.class));    //Save shouldn't happen
    }

    @Test
    void testGetItemInventory() 
    {
        //Mocking repository to return an ItemInventory
        when(itemInventoryRepository.findById(100L)).thenReturn(Optional.of(itemInventory));

        //Calling the service method to retrieve ItemInventory
        ItemInventoryDTO found = itemInventoryService.getItemInventory(100L);

        //Verifying the correct ItemInventory DTO was returned
        assertNotNull(found);                               //Ensure result is not null
        assertEquals(100L, found.getSiid());        //Check siid
        assertEquals(item.getIid(), found.getIid());        //Check Item ID
        assertEquals(inventory.getSid(), found.getSid());   //Check Inventory ID

        //Verifying that the repository's findById method was called
        verify(itemInventoryRepository).findById(100L);
    }

    @Test
    void testGetItemInventory_ItemInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ItemInventory
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ItemInventoryNotFoundException when itemInventory is not found
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.getItemInventory(999L));

        //Verifying that the repository's findById method was called
        verify(itemInventoryRepository).findById(anyLong());
    }

    @Test
    void testGetItemInventories() 
    {
        //Mocking repository to return a list containing the itemInventory
        when(itemInventoryRepository.findAll()).thenReturn(List.of(itemInventory));

        //Calling the service method to retrieve all ItemInventories
        List<ItemInventoryDTO> list = itemInventoryService.getItemInventories();

        //Verifying the result is not empty and contains the expected itemInventory
        assertNotNull(list);
        assertFalse(list.isEmpty());                                //List shouldn't be empty
        assertEquals(1, list.size());                   //Only one itemInventory should be in the list
        assertEquals(100L, list.get(0).getSiid());  //Check siid

        //Verifying that the repository's findAll method was called
        verify(itemInventoryRepository).findAll();
    }

    @Test
    void testUpdateItemInventory() 
    {
        //Mocking repository calls for Item, Inventory, and ItemInventory
        when(itemInventoryRepository.findById(100L)).thenReturn(Optional.of(itemInventory));
        when(itemRepository.findById(item.getIid())).thenReturn(Optional.of(item));                 //Item exists
        when(inventoryRepository.findById(inventory.getSid())).thenReturn(Optional.of(inventory));  //Inventory exists
        when(itemInventoryRepository.save(any(ItemInventory.class))).thenReturn(itemInventory);

        //Calling the service method to update ItemInventory
        ItemInventoryDTO updated = itemInventoryService.updateItemInventory(100L, itemInventoryDTO);

        //Verifying the updated values in the result
        assertNotNull(updated);
        assertEquals(100L, updated.getSiid());      //Check siid
        assertEquals(item.getIid(), updated.getIid());      //Check Item ID
        assertEquals(inventory.getSid(), updated.getSid()); //Check Inventory ID

        //Verifying the mock repository methods were called correctly
        verify(itemInventoryRepository).findById(100L);
        verify(itemRepository).findById(item.getIid());
        verify(inventoryRepository).findById(inventory.getSid());
        verify(itemInventoryRepository).save(any(ItemInventory.class));
    }

    @Test
    void testUpdateItemInventory_ItemInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ItemInventory
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ItemInventoryNotFoundException when itemInventory is not found
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.updateItemInventory(999L, itemInventoryDTO));

        //Verifying that the repository's findById method was called but others were not called
        verify(itemInventoryRepository).findById(anyLong());
        verify(itemRepository, never()).findById(anyLong());        //Item shouldn't be looked up
        verify(inventoryRepository, never()).findById(anyLong());   //Inventory shouldn't be looked up
    }

    @Test
    void testDeleteItemInventory() 
    {
        //Mocking repository to return an ItemInventory
        when(itemInventoryRepository.findById(100L)).thenReturn(Optional.of(itemInventory));

        //Calling the service method to delete ItemInventory
        itemInventoryService.deleteItemInventory(100L);

        //Verifying that the repository's delete method was called
        verify(itemInventoryRepository).delete(any(ItemInventory.class));
    }

    @Test
    void testDeleteItemInventory_ItemInventoryNotFound() 
    {
        //Mocking repository to return empty for non-existent ItemInventory
        when(itemInventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Asserting that the method throws ItemInventoryNotFoundException when itemInventory is not found
        assertThrows(ItemInventoryNotFoundException.class, () -> itemInventoryService.deleteItemInventory(999L));

        //Verifying that the repository's findById method was called and delete was never called
        verify(itemInventoryRepository).findById(anyLong());
        verify(itemInventoryRepository, never()).delete(any(ItemInventory.class));  //Deletion shouldn't happen
    }
}