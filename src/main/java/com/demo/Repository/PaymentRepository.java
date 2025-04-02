package com.demo.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> 
{
    //Custom method to find an payment by its ID
    Optional<Payment> findById(Long pid);  //Returns an Optional of pAYMENT to handle the case where the payment might not be found
}