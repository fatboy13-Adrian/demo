package com.demo.Repository.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entity.Category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category> findById(Long categoryId);
}
