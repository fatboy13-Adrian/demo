package com.demo.Service.Cart;
import com.demo.DTO.Cart.CartItemDTO;
import com.demo.Entity.Cart.Cart;
import com.demo.Entity.Cart.CartItem;
import com.demo.Entity.Item.Item;
import com.demo.Exception.Cart.CartItemNotFoundException;
import com.demo.Exception.Cart.CartNotFoundException;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Repository.Cart.CartItemRepository;
import com.demo.Repository.Cart.CartRepository;
import com.demo.Repository.Item.ItemRepository;
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

@ExtendWith(MockitoExtension.class)                 //Enable Mockito support in JUnit 5
class CartItemServiceImplTest 
{
    @Mock
    private CartItemRepository cartItemRepository;  //Mock for CartItem DB operations

    @Mock
    private CartRepository cartRepository;          //Mock for Cart DB operations

    @Mock
    private ItemRepository itemRepository;          //Mock for Item DB operations

    @InjectMocks
    private CartItemServiceImpl cartItemService;    //Class under test with mocks injected

    private Cart cart;
    private Item item;
    private CartItem cartItem;
    private CartItemDTO cartItemDTO;

    @BeforeEach
    void initData() 
    {
        //Sample mock data used across multiple tests
        cart = Cart.builder().cid(1L).build();
        item = Item.builder().iid(10L).build();
        cartItem = CartItem.builder().ciid(100L).cid(cart).iid(item).build();
        cartItemDTO = CartItemDTO.builder().ciid(100L).cid(1L).iid(10L).build();
    }

    @Test
    void createCartItem_Positive() 
    {
        //Simulate Cart and Item found
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        //Simulate saving a cart item
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        //Act
        CartItemDTO result = cartItemService.createCartItem(cartItemDTO);

        //Assert
        assertNotNull(result);
        assertEquals(100L, result.getCiid());
    }

    @Test
    void createCartItem_CartNotFound_Negative() 
    {
        //Simulate Cart not found
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(CartNotFoundException.class, () -> 
        {
            cartItemService.createCartItem(cartItemDTO);
        });
    }

    @Test
    void createCartItem_ItemNotFound_Negative() 
    {
        //Simulate Cart found but Item not found
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(ItemNotFoundException.class, () -> 
        {
            cartItemService.createCartItem(cartItemDTO);
        });
    }

    @Test
    void getCartItem_Positive() 
    {
        //Simulate CartItem found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.of(cartItem));

        //Act and Assert
        CartItemDTO result = cartItemService.getCartItem(100L);
        assertEquals(100L, result.getCiid());
    }

    @Test
    void getCartItem_NotFound_Negative() 
    {
        //Simulate CartItem not found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(CartItemNotFoundException.class, () -> 
        {
            cartItemService.getCartItem(100L);
        });
    }

    @Test
    void getCartItems_Positive() 
    {
        //Simulate a list with one cart item
        when(cartItemRepository.findAll()).thenReturn(Arrays.asList(cartItem));

        //Act
        List<CartItemDTO> result = cartItemService.getCartItems();

        //Assert list size and content
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getCiid());
    }

    @Test
    void updateCartItem_Positive() 
    {
        //Simulate all related entities found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        //Act
        CartItemDTO result = cartItemService.updateCartItem(100L, cartItemDTO);

        //Assert update was successful
        assertEquals(100L, result.getCiid());
    }

    @Test
    void updateCartItem_CartItemNotFound_Negative() 
    {
        //Simulate CartItem not found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(CartItemNotFoundException.class, () -> 
        {
            cartItemService.updateCartItem(100L, cartItemDTO);
        });
    }

    @Test
    void updateCartItem_CartNotFound_Negative() 
    {
        //Simulate CartItem found but Cart not found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(CartNotFoundException.class, () -> 
        {
            cartItemService.updateCartItem(100L, cartItemDTO);
        });
    }

    @Test
    void updateCartItem_ItemNotFound_Negative() 
    {
        //Simulate CartItem and Cart found, but Item not found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(ItemNotFoundException.class, () -> 
        {
            cartItemService.updateCartItem(100L, cartItemDTO);
        });
    }

    @Test
    void deleteCartItem_Positive() 
    {
        //Simulate CartItem found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(cartItem);

        //Assert no exceptions thrown on delete
        assertDoesNotThrow(() -> cartItemService.deleteCartItem(100L));
    }

    @Test
    void deleteCartItem_NotFound_Negative() 
    {
        //Simulate CartItem not found
        when(cartItemRepository.findById(100L)).thenReturn(Optional.empty());

        //Assert exception is thrown
        assertThrows(CartItemNotFoundException.class, () -> 
        {
            cartItemService.deleteCartItem(100L);
        });
    }
}