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

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    private CartItemDTO toDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .ciid(cartItem.getCiid())
                .cid(cartItem.getCid().getCid())
                .iid(cartItem.getIid().getIid())
                .build();
    }

    private CartItem toEntity(CartItemDTO dto) {
        Cart cart = cartRepository.findById(dto.getCid())
                .orElseThrow(() -> new CartNotFoundException(dto.getCid()));
        Item item = itemRepository.findById(dto.getIid())
                .orElseThrow(() -> new ItemNotFoundException(dto.getIid()));

        return CartItem.builder()
                .ciid(dto.getCiid())
                .cid(cart)
                .iid(item)
                .build();
    }

    @Override
    public CartItemDTO createCartItem(CartItemDTO dto) {
        CartItem entity = toEntity(dto);
        CartItem saved = cartItemRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public CartItemDTO getCartItem(Long ciid) {
        CartItem cartItem = cartItemRepository.findById(ciid)
                .orElseThrow(() -> new CartItemNotFoundException(ciid));
        return toDTO(cartItem);
    }

    @Override
    public List<CartItemDTO> getCartItems() {
        return cartItemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDTO updateCartItem(Long ciid, CartItemDTO dto) {
        CartItem existing = cartItemRepository.findById(ciid)
                .orElseThrow(() -> new CartItemNotFoundException(ciid));

        Cart cart = cartRepository.findById(dto.getCid())
                .orElseThrow(() -> new CartNotFoundException(dto.getCid()));
        Item item = itemRepository.findById(dto.getIid())
                .orElseThrow(() -> new ItemNotFoundException(dto.getIid()));

        existing.setCid(cart);
        existing.setIid(item);

        CartItem saved = cartItemRepository.save(existing);
        return toDTO(saved);
    }

    @Override
    public void deleteCartItem(Long ciid) {
        CartItem cartItem = cartItemRepository.findById(ciid)
                .orElseThrow(() -> new CartItemNotFoundException(ciid));
        cartItemRepository.delete(cartItem);
    }
}
