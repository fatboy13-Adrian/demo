package com.demo.Interface.Product;

import java.util.List;

import com.demo.DTO.Product.ProductCategoryDTO;

public interface ProductCategoryService {
    ProductCategoryDTO createProductCategory(ProductCategoryDTO dto);
    ProductCategoryDTO getProductCategory(Long pcid);
    List<ProductCategoryDTO> getProductCategories();
    ProductCategoryDTO updateProductCategory(Long pcid, ProductCategoryDTO dto);
    void deleteProductCategory(Long pcid);
}
