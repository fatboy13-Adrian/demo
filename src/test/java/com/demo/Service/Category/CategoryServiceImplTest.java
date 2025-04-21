package com.demo.Service.Category;

import com.demo.DTO.Category.CategoryDTO;
import com.demo.Entity.Category.Category;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Exception.Category.InvalidCategoryException;
import com.demo.Repository.Category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .catId(1L)
                .catName("Electronics")
                .description("Electronic items")
                .build();

        categoryDTO = CategoryDTO.builder()
                .catId(1L)
                .catName("Electronics")
                .description("Electronic items")
                .build();
    }

    @Test
    void testCreateCategory_Positive() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(category.getCatName(), result.getCatName());
    }

    @Test
    void testCreateCategory_Negative_NullName() {
        CategoryDTO invalidDTO = CategoryDTO.builder()
                .description("Invalid")
                .build();

        assertThrows(InvalidCategoryException.class, () -> categoryService.createCategory(invalidDTO));
    }

    @Test
    void testCreateCategory_Negative_EmptyName() {
        CategoryDTO invalidDTO = CategoryDTO.builder()
                .catName("   ")
                .description("Invalid")
                .build();

        assertThrows(InvalidCategoryException.class, () -> categoryService.createCategory(invalidDTO));
    }

    @Test
    void testGetCategory_Positive() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals(category.getCatId(), result.getCatId());
    }

    @Test
    void testGetCategory_Negative_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    void testGetCategories_Positive() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDTO> result = categoryService.getCategories();

        assertEquals(1, result.size());
        assertEquals(category.getCatName(), result.get(0).getCatName());
    }

    @Test
    void testUpdateCategory_Positive() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO updateDTO = CategoryDTO.builder()
                .catName("Updated Name")
                .description("Updated Description")
                .build();

        CategoryDTO result = categoryService.updateCategory(1L, updateDTO);

        assertEquals("Updated Name", result.getCatName());
    }

    @Test
    void testUpdateCategory_Negative_EmptyName() {
        CategoryDTO updateDTO = CategoryDTO.builder()
                .catName("   ") // invalid
                .build();

        assertThrows(InvalidCategoryException.class, () -> categoryService.updateCategory(1L, updateDTO));
    }

    @Test
    void testUpdateCategory_Negative_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryDTO updateDTO = CategoryDTO.builder()
                .catName("Some Name")
                .build();

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, updateDTO));
    }

    @Test
    void testDeleteCategory_Positive() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategory_Negative_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
