package com.demo.Repository.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> 
{
    //Custom method to find an item by its ID
    Optional<Inventory> findById(Long sid);  //Returns an Optional of Inventory to handle the case where the inventory might not be found
}