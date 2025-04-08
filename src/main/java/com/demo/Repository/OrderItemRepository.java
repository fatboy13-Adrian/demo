package com.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;   //Importing the JpaRepository interface from Spring Data JPA
import com.demo.Entity.OrderItem;                               //Importing the OrderItem entity

//Interface for the OrderItem repository, extending JpaRepository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> 
{

}