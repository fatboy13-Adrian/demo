package com.demo.Repository.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Item.ItemInventory;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long>
{
    
}