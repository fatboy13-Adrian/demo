package com.demo.Interface.Product;

import com.demo.DTO.Product.ProductDTO;
import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProduct(Long pid);
    List<ProductDTO> getProducts();
    ProductDTO partialUpdateProduct(Long pid, ProductDTO productDTO);
    void deleteProduct(Long pid);
}
