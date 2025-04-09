package com.demo.Repository.Order;
import org.springframework.data.jpa.repository.JpaRepository;   //Importing the JpaRepository interface from Spring Data JPA
import com.demo.Entity.Order.OrderItem;

//Interface for the OrderItem repository, extending JpaRepository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> 
{

}