package com.demo.Util.DataLoader.Category;

import com.demo.DTO.Category.CategoryDTO;
import com.demo.Interface.Category.CategoryService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryService categoryService;

    public CategoryDataLoader(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {
        loadCategoryData();
    }

    private void loadCategoryData() {
        if (categoryService.getCategories().isEmpty()) {
            categoryService.createCategory(CategoryDTO.builder()
                    .categoryName("Electronics")
                    .description("Devices and gadgets")
                    .build());

            categoryService.createCategory(CategoryDTO.builder()
                    .categoryName("Books")
                    .description("Educational and leisure reading")
                    .build());

            categoryService.createCategory(CategoryDTO.builder()
                    .categoryName("Clothing")
                    .description("Apparel and accessories")
                    .build());
        }
    }
}
