package com.demo.Repository.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Order.Order;

//Repository interface for interacting with the 'Order' entity in the database
//Extends JpaRepository to provide CRUD operations for the 'Order' entity
public interface OrderRepository extends JpaRepository<Order, Long> 
{
    //Custom method to find an order by its ID
    Optional<Order> findById(Long oid);  //Returns an Optional of Order to handle the case where the order might not be found
}