package com.demo.Repository.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Product.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
