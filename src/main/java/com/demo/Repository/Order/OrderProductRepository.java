package com.demo.Repository.Order;
import org.springframework.data.jpa.repository.JpaRepository;   //Importing the JpaRepository interface from Spring Data JPA
import com.demo.Entity.Order.OrderProduct;

//Interface for the OrderProduct repository, extending JpaRepository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> 
{

}
