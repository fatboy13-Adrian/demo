package com.demo.Service;
import com.demo.DTO.ItemDTO;
import com.demo.Entity.Item;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest 
{
    @Mock
    private ItemRepository itemRepository; //Mocked ItemRepository

    @InjectMocks
    private ItemServiceImpl itemService; //The service we are testing

    private ItemDTO itemDTO;

    @BeforeEach
    public void setUp() 
    {
        //Initialize a sample item DTO for testing
        itemDTO = new ItemDTO(1L, "Laptop", new BigDecimal("1200.00"));
    }

    @Test   //Positive Test Case for createItem
    public void testCreateItem() 
    {
        //Given
        Item item = new Item(1L, "Laptop", new BigDecimal("1200.00"));
        when(itemRepository.save(any(Item.class))).thenReturn(item);                    //Mocking the save operation

        //When
        ItemDTO createdItem = itemService.createItem(itemDTO);

        //Then
        assertNotNull(createdItem);
        assertEquals("Laptop", createdItem.getItemName());
        assertEquals(new BigDecimal("1200.00"), createdItem.getUnitPrice());
        verify(itemRepository, times(1)).save(any(Item.class)); //Verifying that save was called once
    }

    @Test   //Negative Test Case for getItem (Item Not Found)
    public void testGetItemNotFound() 
    {
        //Given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());         //Mocking not found scenario

        //When & Then
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> 
        {
            itemService.getItem(1L);
        });

        assertEquals("Item with ID 1 not found", exception.getMessage());   //Verifying the exception message
    }

    @Test   //Positive Test Case for getItem
    public void testGetItem() 
    {
        //Given
        Item item = new Item(1L, "Laptop", new BigDecimal("1200.00"));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));    //Mocking findById

        //When
        ItemDTO foundItem = itemService.getItem(1L);

        //Then
        assertNotNull(foundItem);
        assertEquals("Laptop", foundItem.getItemName());
        assertEquals(new BigDecimal("1200.00"), foundItem.getUnitPrice());
    }

    @Test   //Positive Test Case for partialUpdateItem
    public void testPartialUpdateItem() 
    {
        //Given
        Item existingItem = new Item(1L, "Laptop", new BigDecimal("1200.00"));
        ItemDTO updatedItemDTO = new ItemDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(existingItem);

        //When
        ItemDTO updatedItem = itemService.partialUpdateItem(1L, updatedItemDTO);

        //Then
        assertNotNull(updatedItem);
        assertEquals("Gaming Laptop", updatedItem.getItemName());           //Item name should be updated
        assertEquals(new BigDecimal("1500.00"), updatedItem.getUnitPrice());    //Unit price should be updated
    }

    @Test   //Negative Test Case for partialUpdateItem (Item Not Found)
    public void testPartialUpdateItemNotFound() 
    {
        //Given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty()); //Mocking not found scenario

        //When & Then
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> 
        {
            itemService.partialUpdateItem(1L, itemDTO);
        });

        assertEquals("Item with ID 1 not found", exception.getMessage()); //Verifying the exception message
    }

    @Test   //Positive Test Case for deleteItem
    public void testDeleteItem() 
    {
        //Given
        Item item = new Item(1L, "Laptop", new BigDecimal("1200.00"));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        //When
        itemService.deleteItem(1L);

        //Then
        verify(itemRepository, times(1)).delete(item); //Verifying that delete was called once
    }

    @Test   //Negative Test Case for deleteItem (Item Not Found)
    public void testDeleteItemNotFound() 
    {
        //Given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty()); //Mocking not found scenario

        //When & Then
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> 
        {
            itemService.deleteItem(1L);
        });

        assertEquals("Item with ID 1 not found", exception.getMessage()); //Verifying the exception message
    }
}