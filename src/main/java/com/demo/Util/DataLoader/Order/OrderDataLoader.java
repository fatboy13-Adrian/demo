package com.demo.Util.DataLoader.Order;
import com.demo.Entity.Order.Order;
import com.demo.Enum.Order.OrderStatus;
import com.demo.Repository.Order.OrderRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component  //Marks this class as a Spring-managed bean to be executed at application startup
public class OrderDataLoader implements CommandLineRunner 
{
    private final OrderRepository orderRepository; //Repository for performing CRUD operations on Order entities

    //Constructor injection for OrderRepository
    public OrderDataLoader(OrderRepository orderRepository) 
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception 
    {
        //Check if there are any existing orders in the database
        if (orderRepository.count() == 0) 
        {
            //First sample order
            Order order1 = new Order();
            order1.setTotalPrice(new BigDecimal("100.00")); //Setting total price
            order1.setOrderStatus(OrderStatus.NEW);             //Setting order status to "NEW"
            order1.setOrderDateTime(LocalDateTime.now());       //Setting order timestamp to the current time

            //Second sample order
            Order order2 = new Order();
            order2.setTotalPrice(new BigDecimal("200.50"));
            order2.setOrderStatus(OrderStatus.PENDING);         //Setting order status to "PENDING"
            order2.setOrderDateTime(LocalDateTime.now());

            //Third sample order
            Order order3 = new Order();
            order3.setTotalPrice(new BigDecimal("350.75"));
            order3.setOrderStatus(OrderStatus.COMPLETED);       //Setting order status to "COMPLETED"
            order3.setOrderDateTime(LocalDateTime.now());

            //Saving sample orders to the database
            orderRepository.save(order1);
            orderRepository.save(order2);
            orderRepository.save(order3);

            System.out.println("Sample order data loaded successfully.");                       //Logging success message
        } 
        
        else 
            System.out.println("Orders already exist in the database. Skipping data loading."); //Logging if data already exists
    }
}