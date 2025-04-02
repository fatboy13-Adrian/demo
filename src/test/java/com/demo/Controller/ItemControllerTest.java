package com.demo.Controller;
import com.demo.DTO.ItemDTO;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Interface.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest 
{
    @Mock
    private ItemService itemService;        //Mocked ItemService

    @InjectMocks
    private ItemController itemController;  //Controller under test

    private ItemDTO itemDTO;

    @BeforeEach
    public void setUp() 
    {
        itemDTO = new ItemDTO(1L, "Laptop", new BigDecimal("1200.00"));
    }

    @Test   //Positive Test Case for createItem
    public void testCreateItem() 
    {
        //Given
        when(itemService.createItem(any(ItemDTO.class))).thenReturn(itemDTO);       //Mocking the createItem method

        //When
        ResponseEntity<ItemDTO> response = itemController.createItem(itemDTO);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());                     //Verify the status is 201 Created
        assertNotNull(response.getBody());                                              //Verify the response body is not null
        assertEquals("Laptop", response.getBody().getItemName());               //Verify the item name
        assertEquals(new BigDecimal("1200.00"), response.getBody().getUnitPrice()); //Verify the item price
    }

    @Test   //Negative Test Case for createItem (Bad Request)
    public void testCreateItemBadRequest() 
    {
        //Given
        when(itemService.createItem(any(ItemDTO.class))).thenThrow(new RuntimeException("Error creating item"));

        //When
        ResponseEntity<ItemDTO> response = itemController.createItem(itemDTO);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); //Verify the status is 400 Bad Request
        assertNull(response.getBody());                                 //Verify the response body is null
    }

    @Test   //Positive Test Case for getItem
    public void testGetItem() 
    {
        //Given
        when(itemService.getItem(1L)).thenReturn(itemDTO);  //Mocking the getItem method

        //When
        ResponseEntity<ItemDTO> response = itemController.getItem(1L);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());              //Verify the status is 200 OK
        assertNotNull(response.getBody());                                  //Verify the response body is not null
        assertEquals("Laptop", response.getBody().getItemName());   //Verify the item name
    }

    @Test   //Negative Test Case for getItem (Item Not Found)
    public void testGetItemNotFound() 
    {
        //Given
        when(itemService.getItem(1L)).thenThrow(new ItemNotFoundException(1L)); //Pass Long parameter

        //When
        ResponseEntity<ItemDTO> response = itemController.getItem(1L);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());   //Verify the status is 404 Not Found
        assertNull(response.getBody());                                 //Verify the response body is null
    }

    @Test   //Positive Test Case for getItems
    public void testGetItems() 
    {
        //Given
        List<ItemDTO> items = List.of(itemDTO);
        when(itemService.getItems()).thenReturn(items);         //Mocking the getItems method

        //When
        ResponseEntity<List<ItemDTO>> response = itemController.getItems();

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());  //Verify the status is 200 OK
        assertEquals(1, response.getBody().size());     //Verify the size of the item list
    }

    @Test   //Positive Test Case for partialUpdateItem
    public void testPartialUpdateItem() 
    {
        //Given
        ItemDTO updatedItemDTO = new ItemDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(itemService.partialUpdateItem(1L, updatedItemDTO)).thenReturn(updatedItemDTO); //Mocking the partial update

        //When
        ResponseEntity<ItemDTO> response = itemController.partialUpdateItem(1L, updatedItemDTO);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());                          //Verify the status is 200 OK
        assertNotNull(response.getBody());                                              //Verify the response body is not null
        assertEquals("Gaming Laptop", response.getBody().getItemName());        //Verify the updated item name
        assertEquals(new BigDecimal("1500.00"), response.getBody().getUnitPrice()); //Verify the updated item price
    }

    @Test   //Negative Test Case for partialUpdateItem (Item Not Found)
    public void testPartialUpdateItemNotFound() 
    {
        //Given
        ItemDTO updatedItemDTO = new ItemDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(itemService.partialUpdateItem(1L, updatedItemDTO)).thenThrow(new ItemNotFoundException(1L));  //Pass Long parameter

        //When
        ResponseEntity<ItemDTO> response = itemController.partialUpdateItem(1L, updatedItemDTO);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());   //Verify the status is 404 Not Found
        assertNull(response.getBody());                                 //Verify the response body is null
    }

    @Test   //Positive Test Case for deleteItem
    public void testDeleteItem() 
    {
        //Given
        doNothing().when(itemService).deleteItem(1L); //Mocking successful deletion

        //When
        ResponseEntity<Void> response = itemController.deleteItem(1L);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); //Verify the status is 204 No Content
    }

    @Test   //Negative Test Case for deleteItem (Item Not Found)
    public void testDeleteItemNotFound() 
    {
        //Given
        doThrow(new ItemNotFoundException(1L)).when(itemService).deleteItem(1L);    //Pass Long parameter

        //When
        ResponseEntity<Void> response = itemController.deleteItem(1L);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());                       //Verify the status is 404 Not Found
    }
}