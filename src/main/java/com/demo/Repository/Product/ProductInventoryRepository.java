package com.demo.Repository.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Product.ProductInventory;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long>
{
    
}
