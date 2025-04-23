package com.demo.Controller.Product;

import com.demo.DTO.Product.ProductCategoryDTO;
import com.demo.Exception.Product.ProductCategoryNotFoundException;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Interface.Product.ProductCategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryControllerTest {

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductCategoryController controller;

    private ProductCategoryDTO sampleDTO;

    @BeforeEach
    public void setUp() {
        sampleDTO = ProductCategoryDTO.builder()
                .pcid(1L)
                .pid(2L)
                .categoryId(3L)
                .build();
    }

    // ─────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────

    @Test
    void testCreateProductCategory_Success() {
        when(productCategoryService.createProductCategory(any())).thenReturn(sampleDTO);

        ResponseEntity<ProductCategoryDTO> response = controller.createProductCategory(sampleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleDTO, response.getBody());
    }

    @Test
    void testCreateProductCategory_ProductOrCategoryNotFound() {
        when(productCategoryService.createProductCategory(any()))
                .thenThrow(new ProductNotFoundException(2L));

        ResponseEntity<ProductCategoryDTO> response = controller.createProductCategory(sampleDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ─────────────────────────────────────────
    // GET BY ID
    // ─────────────────────────────────────────

    @Test
    void testGetProductCategory_Success() {
        when(productCategoryService.getProductCategory(1L)).thenReturn(sampleDTO);

        ResponseEntity<ProductCategoryDTO> response = controller.getProductCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDTO, response.getBody());
    }

    @Test
    void testGetProductCategory_NotFound() {
        when(productCategoryService.getProductCategory(1L))
                .thenThrow(new ProductCategoryNotFoundException(1L));

        ResponseEntity<ProductCategoryDTO> response = controller.getProductCategory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ─────────────────────────────────────────
    // GET ALL
    // ─────────────────────────────────────────

    @Test
    void testGetProductCategories_Success() {
        when(productCategoryService.getProductCategories()).thenReturn(List.of(sampleDTO));

        ResponseEntity<List<ProductCategoryDTO>> response = controller.getProductCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProductCategories_EmptyList() {
        when(productCategoryService.getProductCategories()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductCategoryDTO>> response = controller.getProductCategories();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ─────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────

    @Test
    void testUpdateProductCategory_Success() {
        when(productCategoryService.updateProductCategory(eq(1L), any())).thenReturn(sampleDTO);

        ResponseEntity<ProductCategoryDTO> response = controller.updateProductCategory(1L, sampleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDTO, response.getBody());
    }

    @Test
    void testUpdateProductCategory_NotFound() {
        when(productCategoryService.updateProductCategory(eq(1L), any()))
                .thenThrow(new ProductCategoryNotFoundException(1L));

        ResponseEntity<ProductCategoryDTO> response = controller.updateProductCategory(1L, sampleDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateProductCategory_ProductOrCategoryInvalid() {
        when(productCategoryService.updateProductCategory(eq(1L), any()))
                .thenThrow(new ProductNotFoundException(2L));

        ResponseEntity<ProductCategoryDTO> response = controller.updateProductCategory(1L, sampleDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ─────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────

    @Test
    void testDeleteProductCategory_Success() {
        doNothing().when(productCategoryService).deleteProductCategory(1L);

        ResponseEntity<Void> response = controller.deleteProductCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteProductCategory_NotFound() {
        doThrow(new ProductCategoryNotFoundException(1L)).when(productCategoryService).deleteProductCategory(1L);

        ResponseEntity<Void> response = controller.deleteProductCategory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
