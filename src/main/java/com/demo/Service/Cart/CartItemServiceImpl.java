package com.demo.Service.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.demo.DTO.Cart.CartItemDTO;
import com.demo.Entity.Cart.Cart;
import com.demo.Entity.Cart.CartItem;
import com.demo.Entity.Item.Item;
import com.demo.Exception.Cart.CartItemNotFoundException;
import com.demo.Exception.Cart.CartNotFoundException;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Interface.Cart.CartItemService;
import com.demo.Repository.Cart.CartItemRepository;
import com.demo.Repository.Cart.CartRepository;
import com.demo.Repository.Item.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service        //This annotation marks this class as a Spring service, making it a candidate for dependency injection.
@Transactional  //This ensures that database operations within methods of this service are handled within a transaction.
public class CartItemServiceImpl implements CartItemService 
{
    private final CartItemRepository cartItemRepository;        //Repository for CartItem entity.
    private final CartRepository cartRepository;                //Repository for Cart entity.
    private final ItemRepository itemRepository;                //Repository for Item entity.

    //Constructor to inject the required dependencies (repositories).
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ItemRepository itemRepository) 
    {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    //Converts a CartItem entity to a CartItemDTO (Data Transfer Object).
    private CartItemDTO toDTO(CartItem cartItem) 
    {
        return CartItemDTO.builder().ciid(cartItem.getCiid()).cid(cartItem.getCid().getCid()).iid(cartItem.getIid().getIid()).build();
    }

    //Converts a CartItemDTO to a CartItem entity, performing necessary lookups in the repositories.
    private CartItem toEntity(CartItemDTO dto) 
    {
        //Fetching the Cart and Item entities based on their IDs from the DTO.
        Cart cart = cartRepository.findById(dto.getCid()).orElseThrow(() -> new CartNotFoundException(dto.getCid()));   //Throws exception if Cart not found.
        Item item = itemRepository.findById(dto.getIid()).orElseThrow(() -> new ItemNotFoundException(dto.getIid()));   //Throws exception if Item not found.

        //Building and returning a new CartItem entity.
        return CartItem.builder().ciid(dto.getCiid()).cid(cart).iid(item).build();
    }

    @Override
    public CartItemDTO createCartItem(CartItemDTO dto) 
    {
        //Converts the DTO to an entity and saves it in the repository.
        CartItem entity = toEntity(dto);
        CartItem saved = cartItemRepository.save(entity);
        return toDTO(saved);    //Converts the saved entity back to DTO and returns it.
    }

    @Override
    public CartItemDTO getCartItem(Long ciid) 
    {
        //Fetches the CartItem entity by its ID.
        CartItem cartItem = cartItemRepository.findById(ciid).orElseThrow(() -> new CartItemNotFoundException(ciid));   //Throws exception if CartItem not found.
        return toDTO(cartItem); //Converts the CartItem entity to DTO and returns it.
    }

    @Override
    public List<CartItemDTO> getCartItems() 
    {
        //Fetches all CartItems from the repository and converts each to DTO.
        return cartItemRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CartItemDTO updateCartItem(Long ciid, CartItemDTO dto) 
    {
        //Fetches the existing CartItem by its ID.
        CartItem existing = cartItemRepository.findById(ciid).orElseThrow(() -> new CartItemNotFoundException(ciid)); //Throws exception if CartItem not found.

        //Fetches the Cart and Item entities using the IDs from the DTO.
        Cart cart = cartRepository.findById(dto.getCid()).orElseThrow(() -> new CartNotFoundException(dto.getCid())); //Throws exception if Cart not found.
        Item item = itemRepository.findById(dto.getIid()).orElseThrow(() -> new ItemNotFoundException(dto.getIid())); //Throws exception if Item not found.

        //Updates the CartItem entity with the new Cart and Item entities.
        existing.setCid(cart);
        existing.setIid(item);

        //Saves the updated CartItem entity and converts it back to DTO for the response.
        CartItem saved = cartItemRepository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void deleteCartItem(Long ciid) 
    {
        //Fetches the CartItem entity by its ID.
        CartItem cartItem = cartItemRepository.findById(ciid).orElseThrow(() -> new CartItemNotFoundException(ciid)); //Throws exception if CartItem not found.
        cartItemRepository.delete(cartItem);    //Deletes the CartItem entity.
    }
}