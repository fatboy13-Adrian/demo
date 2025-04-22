package com.demo.Service.Category;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.demo.DTO.Category.CategoryDTO;
import com.demo.Entity.Category.Category;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Interface.Category.CategoryService;
import com.demo.Repository.Category.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService 
{
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) 
    {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) 
    {
        Category newCategory = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(newCategory);
        logger.info("Created new category with ID: {}", savedCategory.getCategoryId());
        return convertToDTO(savedCategory);
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) 
    {
        Category category = findById(categoryId);
        return convertToDTO(category);
    }

    @Override
    public List<CategoryDTO> getCategories() 
    {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) 
    {
        Category existingCategory = findById(categoryId);
        updateCategoryFields(existingCategory, categoryDTO);
        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("Updated category with ID: {}", categoryId);
        return convertToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) 
    {
        Category category = findById(categoryId);
        categoryRepository.delete(category);
        logger.info("Deleted category with ID: {}", categoryId);
    }

    private Category findById(Long categoryId) 
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            logger.error("Category with ID {} not found", categoryId);
            return new CategoryNotFoundException(categoryId);
        });
    }

    private void updateCategoryFields(Category existingCategory, CategoryDTO categoryDTO) 
    {
        if (categoryDTO.getCategoryName() != null)
            existingCategory.setCategoryName(categoryDTO.getCategoryName());

        if (categoryDTO.getDescription() != null)
            existingCategory.setDescription(categoryDTO.getDescription());
    }

    private CategoryDTO convertToDTO(Category category) 
    {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    private Category convertToEntity(CategoryDTO categoryDTO) 
    {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .categoryName(categoryDTO.getCategoryName())
                .description(categoryDTO.getDescription())
                .build();
    }
}
