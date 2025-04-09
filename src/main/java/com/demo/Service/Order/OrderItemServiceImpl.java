package com.demo.Service.Order;
import com.demo.DTO.Order.OrderItemDTO;
import com.demo.Entity.Item.Item;
import com.demo.Entity.Order.OrderItem;
import com.demo.Exception.Order.OrderItemNotFoundException;
import com.demo.Interface.Order.OrderItemService;
import com.demo.Repository.Item.ItemRepository;
import com.demo.Repository.Order.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service        //Marks this class as a service component in Spring, enabling automatic bean creation
@Transactional  //Ensures that all methods within the class are executed in a transactional context, so changes are rolled back if something fails
public class OrderItemServiceImpl implements OrderItemService 
{
    private final OrderItemRepository orderItemRepository;  //Repository to interact with the OrderItem entity
    private final ItemRepository itemRepository;            //Repository to interact with the Item entity

    //Constructor-based dependency injection. Spring automatically injects the repositories here.
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ItemRepository itemRepository) 
    {
        this.orderItemRepository = orderItemRepository; //Initializes orderItemRepository
        this.itemRepository = itemRepository;           //Initializes itemRepository
    }

    //Helper method to convert OrderItem entity to OrderItemDTO for returning data
    private OrderItemDTO toDTO(OrderItem orderItem) 
    {
        //Builds an OrderItemDTO object using the builder pattern, Maps OrderItem to DTO
        //Maps related Item's iid as oid for DTTO and for DTO and retuns created DTO
        return OrderItemDTO.builder().oiid(orderItem.getOiid()).oid(orderItem.getOid().getIid()) .iid(orderItem.getIid().getIid()).build();  
    }

    //Helper method to convert OrderItemDTO to OrderItem entity for saving/updating
    private OrderItem toEntity(OrderItemDTO dto) 
    {
        //Lookup Item for 'oid' (which is an order reference) and throw exception if not found
        Item orderItem = itemRepository.findById(dto.getOid()).orElseThrow(() -> new OrderItemNotFoundException(dto.getOid()));

        //Lookup Item for 'iid' (which is an item reference) and throw exception if not found
        Item item = itemRepository.findById(dto.getIid()).orElseThrow(() -> new OrderItemNotFoundException(dto.getIid()));

        //Converts the OrderItemDTO to an OrderItem entity using the builder pattern
        return OrderItem.builder().oiid(dto.getOiid()).oid(orderItem).iid(item).build(); 
    }

    @Override   //Creates a new OrderItem using the provided DTO and saves it in the repository
    public OrderItemDTO createOrderItem(OrderItemDTO dto) 
    {
        OrderItem entity = toEntity(dto);                   //Converts the DTO to an OrderItem entity
        OrderItem saved = orderItemRepository.save(entity); //Saves the entity into the database
        return toDTO(saved);                                //Converts the saved OrderItem entity back to DTO and returns it
    }

    @Override   //Fetches an OrderItem by its ID and converts it into a DTO
    public OrderItemDTO getOrderItem(Long oiid) 
    {
        //Looks for the OrderItem in the repository by oiid, throws an exception if not found
        OrderItem orderItem = orderItemRepository.findById(oiid).orElseThrow(() -> new OrderItemNotFoundException(oiid));
        return toDTO(orderItem);    //Converts the found OrderItem to DTO and returns it
    }

    @Override   //Fetches all OrderItems and converts them into DTOs
    public List<OrderItemDTO> getOrderItems() {
        //Finds all OrderItems from repository, converts the list into a stream for processing, convert seach order item enttiy to DTO
        //Collect results itno a list and returns it
        return orderItemRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override   //Updates an existing OrderItem with the provided DTO and saves the updated entity
    public OrderItemDTO updateOrderItem(Long oiid, OrderItemDTO dto) 
    {
        //Checks if the OrderItem exists by oiid, throws an exception if not found
        orderItemRepository.findById(oiid).orElseThrow(() -> new OrderItemNotFoundException(oiid));

        //Updates the DTO with the existing oiid value (to avoid overriding the ID)
        dto = OrderItemDTO.builder().oiid(oiid).oid(dto.getOid()).iid(dto.getIid()).build();

        //Converts the updated DTO back to an entity for saving
        OrderItem updated = toEntity(dto);                      //Converts DTO to OrderItem entity
        OrderItem saved = orderItemRepository.save(updated);    //Saves the updated entity to the repository
        return toDTO(saved);                                    //Converts the saved entity back to a DTO and returns it
    }

    @Override   //Deletes an OrderItem by its ID
    public void deleteOrderItem(Long oiid) 
    {
        //Looks for the OrderItem by oiid, throws an exception if not found
        OrderItem orderItem = orderItemRepository.findById(oiid).orElseThrow(() -> new OrderItemNotFoundException(oiid));
        orderItemRepository.delete(orderItem);  //Deletes the found OrderItem from the repository
    }
}