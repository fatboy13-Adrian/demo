package com.demo.Repository.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Order.OrderPayment;  

//OrderPaymentRepository interface extends JpaRepository for easy database operations
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> 
{

}