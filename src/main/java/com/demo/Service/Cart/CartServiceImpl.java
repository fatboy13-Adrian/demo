package com.demo.Service.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.demo.DTO.Cart.CartDTO;
import com.demo.Entity.Cart.Cart;
import com.demo.Entity.User.User;
import com.demo.Exception.Cart.CartNotFoundException;
import com.demo.Exception.User.UserNotFoundException;
import com.demo.Interface.Cart.CartService;
import com.demo.Repository.Cart.CartRepository;
import com.demo.Repository.User.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service        //Indicates that this class is a service layer component
@Transactional  //Ensures that transactions are managed for the methods in this service
public class CartServiceImpl implements CartService 
{
    private final CartRepository cartRepository;    //Repository for interacting with Cart entities
    private final UserRepository userRepository;    //Repository for interacting with User entities

    //Constructor-based dependency injection
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository) 
    {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override   //Create a new cart
    public CartDTO createCart(CartDTO cartDTO) 
    {
        Cart cart = convertToEntity(cartDTO);           //Convert the CartDTO to a Cart entity
        return convertToDTO(cartRepository.save(cart)); //Save the cart entity and convert it back to a DTO
    }

    @Override   //Retrieve a specific cart by its ID
    public CartDTO getCart(Long cid) 
    {
        return convertToDTO(findById(cid)); //Find the cart by ID and return it as a DTO
    }

    @Override   //Retrieve all carts
    public List<CartDTO> getCarts() 
    {
        //Find all carts, convert each to a CartDTO, and return the list
        return cartRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override   //Update an existing cart
    public CartDTO updateCart(Long cid, CartDTO cartDTO) 
    {
        Cart cart = findById(cid);                      //Find the cart by its ID
        User user = findUserById(cartDTO.getUid());     //Find the user by the UID from the CartDTO
        cart.setUid(user);                              //Update the cart's user association
        return convertToDTO(cartRepository.save(cart)); //Save the updated cart and return it as a DTO
    }

    @Override   //Delete a cart by its ID
    public void deleteCart(Long cid) 
    {
        Cart cart = findById(cid);      //Find the cart by its ID
        cartRepository.delete(cart);    //Delete the cart entity
    }

    //Helper method to find a Cart by its ID, throws an exception if not found
    private Cart findById(Long cid) 
    {
        return cartRepository.findById(cid).orElseThrow(() -> new CartNotFoundException(cid));  //Throw exception if cart is not found
    }

    //Helper method to find a User by their ID, throws an exception if not found
    private User findUserById(Long uid) 
    {
        return userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));  //Throw exception if user is not found
    }

    //Convert a Cart entity to a CartDTO
    private CartDTO convertToDTO(Cart cart) 
    {
        return new CartDTO(cart.getCid(), cart.getUid().getUid());  //Create and return a new CartDTO from Cart entity
    }

    //Convert a CartDTO to a Cart entity
    private Cart convertToEntity(CartDTO cartDTO) 
    {
        User user = findUserById(cartDTO.getUid()); //Find the user associated with the CartDTO
        Cart cart = new Cart();                     //Create a new Cart entity
        cart.setCid(cartDTO.getCid());              //Set the cart ID from the DTO
        cart.setUid(user);                          //Set the user for the cart
        return cart;                                //Return the Cart entity
    }
}