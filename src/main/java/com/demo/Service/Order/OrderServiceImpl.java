package com.demo.Service.Order;
import java.util.List;
import java.util.Optional; 
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.DTO.Order.OrderDTO;
import com.demo.Entity.Order.Order;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Interface.Order.OrderService;
import com.demo.Repository.Order.OrderRepository;
import jakarta.validation.Valid;

@Service  //Marks this class as a Spring service for dependency injection
public class OrderServiceImpl implements OrderService 
{
    private final OrderRepository orderRepository;

    @Autowired  //Constructor injection of the OrderRepository
    public OrderServiceImpl(OrderRepository orderRepository) 
    {
        this.orderRepository = orderRepository;  //Initialize the repository to interact with the database
    }

    @Override  //Method to create a new order
    public OrderDTO createOrder(@Valid OrderDTO orderDTO) 
    {
        //Map OrderDTO to Order entity
        Order order = new Order(orderDTO.getOid(), orderDTO.getTotalPrice(), orderDTO.getOrderStatus(), orderDTO.getOrderDateTime());

        //Save the order entity to the database
        order = orderRepository.save(order);

        //Convert the saved Order entity to OrderDTO and return it
        return convertToDTO(order);  //Return the DTO version of the created order
    }

    @Override   //Method to retrieve an order by its ID
    public OrderDTO getOrder(Long oid) 
    {
        Optional<Order> order = orderRepository.findById(oid);  //Fetch the order from the repository by its ID

        //If order is not found, throw a custom OrderNotFoundException
        if(order.isEmpty())
            throw new OrderNotFoundException(oid);              //Throw exception if order is not found

        //Convert the found Order entity to OrderDTO and return it
        return convertToDTO(order.get());                       //Return the DTO of the fetched order
    }

    @Override  //Method to retrieve all orders in the system
    public List<OrderDTO> getOrders() 
    {
        List<Order> orders = orderRepository.findAll();                                 //Fetch all orders from the repository

        //Convert the list of Order entities to a list of OrderDTOs and return it
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());    //Collect the results into a list and return it
    }

    @Override  //Method to partially update an existing order
    public OrderDTO partialUpdateOrder(Long oid, OrderDTO orderDTO) 
    {
        Optional<Order> existingOrder = orderRepository.findById(oid);  //Fetch the existing order by its ID

        //If order is not found, throw an OrderNotFoundException
        if(existingOrder.isEmpty()) 
            throw new OrderNotFoundException(oid);                      //Throw exception if the order to update is not found

        //Get the existing order entity
        Order order = existingOrder.get();                      

        //Update fields from the OrderDTO if they are not null
        if(orderDTO.getTotalPrice() != null) 
            order.setTotalPrice(orderDTO.getTotalPrice());              //Update totalPrice if provided

        if(orderDTO.getOrderStatus() != null)
            order.setOrderStatus(orderDTO.getOrderStatus());            //Update orderStatus if provided

        //Save the updated order entity
        order = orderRepository.save(order);

        //Convert the updated Order entity to OrderDTO and return it
        return convertToDTO(order); //Return the DTO of the updated order
    }

    @Override   //Method to delete an existing order by its ID
    public void deleteOrder(Long oid) 
    {
        //Fetch the order by its ID
        Optional<Order> order = orderRepository.findById(oid);

        //If the order is not found, throw an OrderNotFoundException
        if(order.isEmpty()) 
            throw new OrderNotFoundException(oid);  //Throw exception if the order to delete is not found

        orderRepository.delete(order.get());        //Proceed to delete the found order
    }

    //Helper method to convert an Order entity to OrderDTO
    private OrderDTO convertToDTO(Order order) 
    {
        //Map fields from Order entity to OrderDTO and return a new OrderDTO object
        return OrderDTO.builder().oid(order.getOid()).totalPrice(order.getTotalPrice()).orderStatus(order.getOrderStatus())
        .orderDateTime(order.getOrderDateTime()).build();  //Return the newly created OrderDTO
    }
}