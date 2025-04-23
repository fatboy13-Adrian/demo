package com.demo.Service.Product;

import com.demo.DTO.Product.ProductCategoryDTO;
import com.demo.Entity.Product.ProductCategory;
import com.demo.Exception.Product.ProductCategoryNotFoundException;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Repository.Product.ProductCategoryRepository;
import com.demo.Repository.Product.ProductRepository;
import com.demo.Repository.Category.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryServiceImplTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    private ProductCategoryDTO productCategoryDTO;

    @BeforeEach
    public void setUp() {
        productCategoryDTO = ProductCategoryDTO.builder()
                .pcid(1L)
                .pid(2L)
                .categoryId(3L)
                .build();
    }

    @Test
    public void testCreateProductCategoryPositive() {
        ProductCategory savedEntity = new ProductCategory(1L, 2L, 3L);

        when(productRepository.existsById(2L)).thenReturn(true);
        when(categoryRepository.existsById(3L)).thenReturn(true);
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(savedEntity);

        ProductCategoryDTO result = productCategoryService.createProductCategory(productCategoryDTO);

        assertNotNull(result);
        assertEquals(1L, result.getPcid());
        assertEquals(2L, result.getPid());
        assertEquals(3L, result.getCategoryId());
    }

    @Test
    public void testCreateProductCategory_ProductNotFound() {
        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> {
            productCategoryService.createProductCategory(productCategoryDTO);
        });
    }

    @Test
    public void testCreateProductCategory_CategoryNotFound() {
        when(productRepository.existsById(2L)).thenReturn(true);
        when(categoryRepository.existsById(3L)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> {
            productCategoryService.createProductCategory(productCategoryDTO);
        });
    }

    @Test
    public void testGetProductCategoryPositive() {
        ProductCategory category = new ProductCategory(1L, 2L, 3L);
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        ProductCategoryDTO result = productCategoryService.getProductCategory(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPcid());
        assertEquals(2L, result.getPid());
        assertEquals(3L, result.getCategoryId());
    }

    @Test
    public void testGetProductCategoryNegative() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        ProductCategoryNotFoundException exception = assertThrows(ProductCategoryNotFoundException.class, () -> {
            productCategoryService.getProductCategory(1L);
        });

        assertEquals("Product category with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testUpdateProductCategoryPositive() {
        ProductCategory existing = new ProductCategory(1L, 2L, 3L);
        ProductCategoryDTO update = ProductCategoryDTO.builder().pid(4L).categoryId(5L).build();

        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.existsById(4L)).thenReturn(true);
        when(categoryRepository.existsById(5L)).thenReturn(true);
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(existing);

        ProductCategoryDTO result = productCategoryService.updateProductCategory(1L, update);

        assertNotNull(result);
        assertEquals(4L, result.getPid());
        assertEquals(5L, result.getCategoryId());
    }

    @Test
    public void testUpdateProductCategory_ProductNotFound() {
        ProductCategory existing = new ProductCategory(1L, 2L, 3L);
        ProductCategoryDTO update = ProductCategoryDTO.builder().pid(999L).build();

        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> {
            productCategoryService.updateProductCategory(1L, update);
        });
    }

    @Test
    public void testUpdateProductCategory_CategoryNotFound() {
        ProductCategory existing = new ProductCategory(1L, 2L, 3L);
        ProductCategoryDTO update = ProductCategoryDTO.builder().categoryId(999L).build();

        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.existsById(999L)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> {
            productCategoryService.updateProductCategory(1L, update);
        });
    }

    @Test
    public void testDeleteProductCategoryPositive() {
        ProductCategory category = new ProductCategory(1L, 2L, 3L);
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        productCategoryService.deleteProductCategory(1L);

        verify(productCategoryRepository, times(1)).delete(category);
    }

    @Test
    public void testDeleteProductCategoryNegative() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductCategoryNotFoundException.class, () -> {
            productCategoryService.deleteProductCategory(1L);
        });
    }

    @Test
    public void testGetProductCategories_EmptyList() {
        when(productCategoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductCategoryDTO> result = productCategoryService.getProductCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetProductCategories_NonEmptyList() {
        ProductCategory category1 = new ProductCategory(1L, 2L, 3L);
        ProductCategory category2 = new ProductCategory(2L, 4L, 5L);

        when(productCategoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<ProductCategoryDTO> result = productCategoryService.getProductCategories();

        assertEquals(2, result.size());
    }
}
