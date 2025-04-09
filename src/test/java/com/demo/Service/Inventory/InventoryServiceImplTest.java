package com.demo.Service.Inventory;
import com.demo.DTO.Inventory.InventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Repository.Inventory.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Enables Mockito support for JUnit 5 tests
public class InventoryServiceImplTest 
{
    @Mock //Creates a mock instance of InventoryRepository
    private InventoryRepository inventoryRepository;

    @InjectMocks //Injects mocks into InventoryServiceImpl
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private InventoryDTO inventoryDTO;

    @BeforeEach //Runs before each test case to initialize test data
    void setUp() 
    {
        inventory = new Inventory(1L, 10);          //Sample inventory entity
        inventoryDTO = new InventoryDTO(1L, 10);    //Corresponding DTO
    }

    @Test   //Positive test case: Creating inventory
    void testCreateInventory_Success() 
    {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory); //Mock save operation
        InventoryDTO result = inventoryService.createInventory(inventoryDTO);           //Call service method
        assertNotNull(result);                                                          //Ensure result is not null
        assertEquals(inventoryDTO.getSid(), result.getSid());                           //Validate ID
        assertEquals(inventoryDTO.getStockQty(), result.getStockQty());                 //Validate stock quantity
    }

    @Test   //Positive test case: Getting inventory by ID
    void testGetInventory_Success() 
    {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));  //Mock findById operation
        InventoryDTO result = inventoryService.getInventory(1L);                    //Call service method
        assertNotNull(result);                                                          //Ensure result is not null
        assertEquals(inventory.getSid(), result.getSid());                              //Validate ID
        assertEquals(inventory.getStockQty(), result.getStockQty());                    //Validate stock quantity
    }

    @Test   //Negative test case: Getting inventory with non-existing ID
    void testGetInventory_NotFound() 
    {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());                                                //Mock empty result
        Exception exception = assertThrows(RuntimeException.class, () -> inventoryService.getInventory(2L));    //Expect exception
        assertEquals("Inventory not found", exception.getMessage());                                                    //Validate exception message
    }
    
    @Test   //Positive test case: Getting all inventories
    void testGetInventories_Success() 
    {
        List<Inventory> inventoryList = Arrays.asList(inventory);       //Sample inventory list
        when(inventoryRepository.findAll()).thenReturn(inventoryList);  //Mock findAll operation
        List<InventoryDTO> result = inventoryService.getInventories();  //Call service method
        assertNotNull(result);                                          //Ensure result is not null
        assertEquals(1, result.size());                         //Validate list size
    }

    @Test   //Positive test case: Updating inventory
    void testUpdateInventory_Success() 
    {
        InventoryDTO updatedDTO = new InventoryDTO(1L, 20);                         //Updated DTO
        Inventory updatedInventory = new Inventory(1L, 20);                         //Updated entity
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));          //Mock findById
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(updatedInventory);  //Mock save
        InventoryDTO result = inventoryService.updateInventory(1L, updatedDTO);             //Call service method
        assertNotNull(result); //Ensure result is not null
        assertEquals(20, result.getStockQty()); //Validate updated stock quantity
    }
    
    @Test   //Negative test case: Updating non-existing inventory
    void testUpdateInventory_NotFound() 
    {
        InventoryDTO updatedDTO = new InventoryDTO(2L, 20);                                                                 //Updated DTO
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());                                                            //Mock empty result
        Exception exception = assertThrows(RuntimeException.class, () -> inventoryService.updateInventory(2L, updatedDTO)); //Expect exception
        assertEquals("Inventory not found", exception.getMessage());                                                                //Validate exception message
    }

    @Test   //Positive test case: Deleting inventory
    void testDeleteInventory_Success() 
    {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));      //Mock findById
        doNothing().when(inventoryRepository).delete(inventory);                            //Mock delete operation
        assertDoesNotThrow(() -> inventoryService.deleteInventory(1L));                 //Ensure no exception is thrown
        verify(inventoryRepository, times(1)).delete(inventory);    //Verify delete was called once
    }
    
    @Test   //Negative test case: Deleting non-existing inventory
    void testDeleteInventory_NotFound() 
    {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());                                                //Mock empty result
        Exception exception = assertThrows(RuntimeException.class, () -> inventoryService.deleteInventory(2L)); //Expect exception
        assertEquals("Inventory not found", exception.getMessage());                                                    //Validate exception message
    }
}