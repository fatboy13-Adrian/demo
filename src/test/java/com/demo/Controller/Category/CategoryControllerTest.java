package com.demo.Controller.Category;

import com.demo.DTO.Category.CategoryDTO;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Exception.Category.InvalidCategoryException;
import com.demo.Interface.Category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    // Helper method to create a CategoryDTO
    private CategoryDTO createCategoryDTO(Long catId, String catName, String description) {
        return CategoryDTO.builder()
                .catId(catId)
                .catName(catName)
                .description(description)
                .build();
    }

    // Positive Test Case: Create Category
    @Test
    void testCreateCategory_Positive() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(1L, "Electronics", "Electronic products");

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content("{\"catId\": 1, \"catName\": \"Electronics\", \"description\": \"Electronic products\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.catId").value(1))
                .andExpect(jsonPath("$.catName").value("Electronics"));
    }

    // Negative Test Case: Create Category - Invalid Data
    @Test
    void testCreateCategory_Negative_InvalidData() throws Exception {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenThrow(new InvalidCategoryException("Invalid category"));

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content("{\"catName\": \"Invalid Category\"}"))
                .andExpect(status().isBadRequest());
    }

    // Positive Test Case: Get Category
    @Test
    void testGetCategory_Positive() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(1L, "Electronics", "Electronic products");

        when(categoryService.getCategory(1L)).thenReturn(categoryDTO);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catId").value(1))
                .andExpect(jsonPath("$.catName").value("Electronics"));
    }

    // Negative Test Case: Get Category - Not Found
    @Test
    void testGetCategory_Negative_NotFound() throws Exception {
        when(categoryService.getCategory(1L)).thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound());
    }

    // Positive Test Case: Get Categories (List)
    @Test
    void testGetCategories_Positive() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(1L, "Electronics", "Electronic products");

        when(categoryService.getCategories()).thenReturn(Collections.singletonList(categoryDTO));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].catId").value(1))
                .andExpect(jsonPath("$[0].catName").value("Electronics"));
    }

    // Negative Test Case: Update Category - Not Found
    @Test
    void testUpdateCategory_Negative_NotFound() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(patch("/categories/1")
                .contentType("application/json")
                .content("{\"catId\": 1, \"catName\": \"Updated Electronics\", \"description\": \"Updated description\"}"))
                .andExpect(status().isNotFound());
    }

    // Positive Test Case: Update Category
    @Test
    void testUpdateCategory_Positive() throws Exception {
        CategoryDTO categoryDTO = createCategoryDTO(1L, "Updated Electronics", "Updated description");

        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(patch("/categories/1")
                .contentType("application/json")
                .content("{\"catId\": 1, \"catName\": \"Updated Electronics\", \"description\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catId").value(1))
                .andExpect(jsonPath("$.catName").value("Updated Electronics"));
    }

    // Negative Test Case: Update Category - Invalid Data
    @Test
    void testUpdateCategory_Negative_InvalidData() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenThrow(new InvalidCategoryException("Invalid category update"));

        mockMvc.perform(patch("/categories/1")
                .contentType("application/json")
                .content("{\"catId\": 1, \"catName\": \"Invalid Electronics\", \"description\": \"Invalid description\"}"))
                .andExpect(status().isBadRequest());
    }

    // Positive Test Case: Delete Category
    @Test
    void testDeleteCategory_Positive() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    // Negative Test Case: Delete Category - Not Found
    @Test
    void testDeleteCategory_Negative_NotFound() throws Exception {
        doThrow(new CategoryNotFoundException(1L)).when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNotFound());
    }
}
