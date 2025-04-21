package com.demo.Interface.Category;

import java.util.List;

import com.demo.DTO.Category.CategoryDTO;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO getCategory(Long catId);
    List<CategoryDTO> getCategories();
    CategoryDTO updateCategory(Long catId, CategoryDTO dto);
    void deleteCategory(Long catId);
}
