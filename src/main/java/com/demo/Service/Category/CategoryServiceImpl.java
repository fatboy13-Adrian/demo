package com.demo.Service.Category;

import com.demo.DTO.Category.CategoryDTO;
import com.demo.Entity.Category.Category;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Exception.Category.InvalidCategoryException;
import com.demo.Interface.Category.CategoryService;
import com.demo.Repository.Category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Validation check
        if (categoryDTO.getCatName() == null || categoryDTO.getCatName().trim().isEmpty()) {
            throw new InvalidCategoryException("Category name cannot be null or empty");
        }
        
        Category category = Category.builder()
                .catName(categoryDTO.getCatName())
                .description(categoryDTO.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);
        
        return CategoryDTO.builder()
                .catId(savedCategory.getCatId())
                .catName(savedCategory.getCatName())
                .description(savedCategory.getDescription())
                .build();
    }

    @Override
    public CategoryDTO getCategory(Long catId) {
        // Fetching category from the database
        Optional<Category> categoryOptional = categoryRepository.findById(catId);

        // If category is not found, throw CategoryNotFoundException
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(catId);
        }

        Category category = categoryOptional.get();
        
        return CategoryDTO.builder()
                .catId(category.getCatId())
                .catName(category.getCatName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public List<CategoryDTO> getCategories() {
        // Fetching all categories from the database
        List<Category> categories = categoryRepository.findAll();
        
        // Mapping Category entities to CategoryDTO
        return categories.stream()
                .map(category -> CategoryDTO.builder()
                        .catId(category.getCatId())
                        .catName(category.getCatName())
                        .description(category.getDescription())
                        .build())
                .toList();
    }

    @Override
    public CategoryDTO updateCategory(Long catId, CategoryDTO categoryDTO) {
        // Validation check for the name field
        if (categoryDTO.getCatName() != null && categoryDTO.getCatName().trim().isEmpty()) {
            throw new InvalidCategoryException("Category name cannot be null or empty");
        }

        // Fetching the existing category from the database
        Optional<Category> existingCategoryOptional = categoryRepository.findById(catId);

        // If category is not found, throw CategoryNotFoundException
        if (existingCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(catId);
        }

        Category existingCategory = existingCategoryOptional.get();

        // Update only the non-null fields (partial update)
        if (categoryDTO.getCatName() != null) {
            existingCategory.setCatName(categoryDTO.getCatName());
        }
        if (categoryDTO.getDescription() != null) {
            existingCategory.setDescription(categoryDTO.getDescription());
        }

        // Saving the updated category
        Category updatedCategory = categoryRepository.save(existingCategory);

        // Returning the updated CategoryDTO
        return CategoryDTO.builder()
                .catId(updatedCategory.getCatId())
                .catName(updatedCategory.getCatName())
                .description(updatedCategory.getDescription())
                .build();
    }

    @Override
    public void deleteCategory(Long catId) {
        // Fetching the category to ensure it exists before deletion
        Optional<Category> categoryOptional = categoryRepository.findById(catId);

        // If category is not found, throw CategoryNotFoundException
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(catId);
        }

        // Deleting the category
        categoryRepository.delete(categoryOptional.get());
    }
}
