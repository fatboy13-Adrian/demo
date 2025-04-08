package com.demo.Service;
import com.demo.DTO.OrderItemDTO;
import com.demo.Entity.Item;
import com.demo.Entity.OrderItem;
import com.demo.Exception.Order.OrderItemNotFoundException;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) //Extends the Mockito framework for unit tests
class OrderItemServiceImplTest 
{
    @Mock           //Mock the OrderItemRepository to simulate the database interaction
    private OrderItemRepository orderItemRepository;

    @Mock           //Mock the ItemRepository to simulate the database interaction
    private ItemRepository itemRepository;

    @InjectMocks    //Inject the mocks into the OrderItemServiceImpl instance
    private OrderItemServiceImpl orderItemService;

    //Sample data for testing
    private final Long oiid = 1L;
    private final Long oid = 2L;
    private final Long iid = 3L;

    private final Item order = Item.builder().iid(oid).build();                                             //Example Item for order
    private final Item item = Item.builder().iid(iid).build();                                              //Example Item for order item
    private final OrderItem orderItem = OrderItem.builder().oiid(oiid).oid(order).iid(item).build();        //Example OrderItem
    private final OrderItemDTO orderItemDTO = OrderItemDTO.builder().oiid(oiid).oid(oid).iid(iid).build();  //Example DTO

    //Positive test cases
    @Test
    void testCreateOrderItem_Success() 
    {
        //Mock behavior for repository interactions
        when(itemRepository.findById(oid)).thenReturn(Optional.of(order));
        when(itemRepository.findById(iid)).thenReturn(Optional.of(item));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        //Call the method under test
        OrderItemDTO result = orderItemService.createOrderItem(orderItemDTO);

        //Assert the results
        assertEquals(oiid, result.getOiid());   //Ensure the correct OrderItemDTO is returned
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));   //Verify save method was called once
    }

    @Test
    void testGetOrderItem_Success() 
    {
        //Mock behavior for repository interaction
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.of(orderItem));

        //Call the method under test
        OrderItemDTO result = orderItemService.getOrderItem(oiid);

        //Assert the results
        assertEquals(oiid, result.getOiid());
        assertEquals(oid, result.getOid());
        assertEquals(iid, result.getIid());
    }

    @Test
    void testGetOrderItems_Success() 
    {
        //Mock behavior for repository interaction
        when(orderItemRepository.findAll()).thenReturn(Arrays.asList(orderItem));

        //Call the method under test
        List<OrderItemDTO> result = orderItemService.getOrderItems();

        //Assert the results
        assertEquals(1, result.size());             //List should contain one element
        assertEquals(oiid, result.get(0).getOiid());    //Verify the first item in the list
    }

    @Test
    void testUpdateOrderItem_Success() 
    {
        //Mock behavior for repository interactions
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.of(orderItem));
        when(itemRepository.findById(oid)).thenReturn(Optional.of(order));
        when(itemRepository.findById(iid)).thenReturn(Optional.of(item));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        //Call the method under test
        OrderItemDTO result = orderItemService.updateOrderItem(oiid, orderItemDTO);

        //Assert the results
        assertEquals(oiid, result.getOiid());   //Ensure correct OrderItemDTO is returned
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));   //Verify save method was called once
    }

    @Test
    void testDeleteOrderItem_Success() 
    {
        //Mock behavior for repository interaction
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.of(orderItem));

        //Call the method under test and ensure no exceptions are thrown
        assertDoesNotThrow(() -> orderItemService.deleteOrderItem(oiid));
        verify(orderItemRepository, times(1)).delete(orderItem);    //Verify delete method was called once
    }

    //Negative test cases
    @Test
    void testCreateOrderItem_ItemNotFound() 
    {
        //Mock behavior: Item with given ID not found
        when(itemRepository.findById(oid)).thenReturn(Optional.empty());

        //Assert that the exception is thrown when trying to create an order item
        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.createOrderItem(orderItemDTO));
    }

    @Test
    void testGetOrderItem_NotFound() 
    {
        //Mock behavior: OrderItem with given ID not found
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.empty());

        //Assert that the exception is thrown when trying to get an order item
        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.getOrderItem(oiid));
    }

    @Test
    void testUpdateOrderItem_OrderItemNotFound() 
    {
        //Mock behavior: OrderItem with given ID not found
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.empty());

        //Assert that the exception is thrown when trying to update a non-existing order item
        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.updateOrderItem(oiid, orderItemDTO));
    }

    @Test
    void testDeleteOrderItem_NotFound() 
    {
        //Mock behavior: OrderItem with given ID not found
        when(orderItemRepository.findById(oiid)).thenReturn(Optional.empty());

        //Assert that the exception is thrown when trying to delete a non-existing order item
        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.deleteOrderItem(oiid));
    }
}