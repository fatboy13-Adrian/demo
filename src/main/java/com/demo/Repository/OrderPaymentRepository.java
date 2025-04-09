package com.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;  
import com.demo.Entity.OrderPayment;  

//OrderPaymentRepository interface extends JpaRepository for easy database operations
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> 
{

}