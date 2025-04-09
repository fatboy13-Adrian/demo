package com.demo.Util.DataLoader.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.demo.Entity.Item;
import com.demo.Entity.Order;
import com.demo.Entity.OrderItem;
import com.demo.Repository.ItemRepository;
import com.demo.Repository.OrderItemRepository;
import com.demo.Repository.OrderRepository;
import java.util.List;

@Component  //Marks this class as a Spring component to be discovered and instantiated by Spring
public class OrderItemDataLoader implements CommandLineRunner 
{
    //Injecting repositories for accessing Item, Order, and OrderItem entities
    private final ItemRepository itemRepository;            //Repository to access Item data
    private final OrderRepository orderRepository;          //Repository to access Order data
    private final OrderItemRepository orderItemRepository;  //Repository to access OrderItem data

    //Constructor for dependency injection of the repositories
    public OrderItemDataLoader(ItemRepository itemRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) 
    {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void run(String... args) 
    {
        if(orderItemRepository.count() == 0)  //Proceed only if no OrderItem records exist in the database
            linkExistingOrdersWithItems();    //Trigger the method to link orders with items
    }

    @Transactional  //Ensures that all operations in this method are executed as a single transaction
    private void linkExistingOrdersWithItems() 
    {
        List<Order> orders = orderRepository.findAll();  //Fetch all orders from the database
        List<Item> items = itemRepository.findAll();     //Fetch all items from the database

        //Early exit if either Items or Orders are missing
        if(items.isEmpty() || orders.isEmpty()) 
        {
            //Log message if no items or orders are found
            System.out.println("No existing Items or Orders found. Skipping linkage.");
            return;  //Exit the method if there are no items or orders
        }

        //Link Orders to items based on the desired pattern
        int itemIndex = 0;  //Start from the first item (index 0)

        //Iterate through all orders and associate them with items
        for(Order order : orders) 
        {
            //Get the current item using the itemIndex, linking the order to the item
            Item item = items.get(itemIndex);

            //Create and save the association between order and item
            createAndSaveOrderItemLink(order, item);

            //Increment itemIndex and cycle back to the first item if needed
            itemIndex = (itemIndex + 1) % items.size();  //Cycle through items in a round-robin fashion
        }

        //Log the linkage status after processing all orders
        System.out.println("Linked " +orders.size()+ " orders with items.");
    }

    //Helper method to create and save OrderItem link
    public void createAndSaveOrderItemLink(Order order, Item item) 
    {
        //Create a new OrderItem instance that links the current Item and Order
        OrderItem orderItem = OrderItem.builder().oid(item).iid(item).build();

        //Save the newly created OrderItem instance to the database
        orderItemRepository.save(orderItem);

        //Log the successful linkage between the Order and Item
        System.out.println("Linked Order with Item ID: " +item.getIid());
    }
}