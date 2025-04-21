package com.demo.Service.Product;

import com.demo.DTO.Product.ProductDTO;
import com.demo.Entity.Product.Product;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Interface.Product.ProductService;
import com.demo.Repository.Product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product newProduct = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(newProduct);
        logger.info("Created product with ID: {}", savedProduct.getPid());
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO getProduct(Long pid) {
        return convertToDTO(findById(pid));
    }

    @Override
    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO partialUpdateProduct(Long pid, ProductDTO productDTO) {
        Product existingProduct = findById(pid);
        updateProductFields(existingProduct, productDTO);
        Product updatedProduct = productRepository.save(existingProduct);
        logger.info("Updated product with ID: {}", pid);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long pid) {
        Product product = findById(pid);
        productRepository.delete(product);
    }

    private Product findById(Long pid) {
        return productRepository.findById(pid).orElseThrow(() -> new ProductNotFoundException(pid));
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .pid(product.getPid())
                .productName(product.getProductName())
                .unitPrice(product.getUnitPrice())
                .build();
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .pid(productDTO.getPid())
                .productName(productDTO.getProductName())
                .unitPrice(productDTO.getUnitPrice())
                .build();
    }

    private void updateProductFields(Product product, ProductDTO dto) {
        if (dto.getProductName() != null) {
            product.setProductName(dto.getProductName());
        }
        if (dto.getUnitPrice() != null) {
            product.setUnitPrice(dto.getUnitPrice());
        }
    }
}