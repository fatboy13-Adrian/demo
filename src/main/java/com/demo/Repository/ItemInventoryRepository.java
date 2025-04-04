package com.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entity.ItemInventory;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long>
{
}