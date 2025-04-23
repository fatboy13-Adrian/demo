package com.demo.Service.Product;

import com.demo.DTO.Product.ProductCategoryDTO;
import com.demo.Entity.Product.ProductCategory;
import com.demo.Exception.Product.ProductCategoryNotFoundException;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Interface.Product.ProductCategoryService;
import com.demo.Repository.Product.ProductCategoryRepository;
import com.demo.Repository.Product.ProductRepository;
import com.demo.Repository.Category.CategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository,
                                      ProductRepository productRepository,
                                      CategoryRepository categoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductCategoryDTO createProductCategory(ProductCategoryDTO dto) {
        // Validate the existence of the product and category before creating the association
        validateCategoryAndProduct(dto.getPid(), dto.getCategoryId());

        ProductCategory entity = convertToEntity(dto);
        ProductCategory saved = productCategoryRepository.save(entity);
        logger.info("Created ProductCategory with ID: {}", saved.getPcid());
        return convertToDTO(saved);
    }

    @Override
    public ProductCategoryDTO getProductCategory(Long pcid) {
        ProductCategory entity = findById(pcid);
        return convertToDTO(entity);
    }

    @Override
    public List<ProductCategoryDTO> getProductCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        if (categories.isEmpty()) {
            logger.warn("No product categories found.");
        }
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryDTO updateProductCategory(Long pcid, ProductCategoryDTO dto) {
        ProductCategory existing = findById(pcid);

        // Validate the existence of the product and category before updating
        if (dto.getPid() != null) {
            validateProduct(dto.getPid());
            existing.setPid(dto.getPid());
        }

        if (dto.getCategoryId() != null) {
            validateCategory(dto.getCategoryId());
            existing.setCategoryId(dto.getCategoryId());
        }

        ProductCategory updated = productCategoryRepository.save(existing);
        logger.info("Updated ProductCategory with ID: {}", pcid);
        return convertToDTO(updated);
    }

    @Override
    public void deleteProductCategory(Long pcid) {
        ProductCategory entity = findById(pcid);
        productCategoryRepository.delete(entity);
        logger.info("Deleted ProductCategory with ID: {}", pcid);
    }

    private ProductCategory findById(Long pcid) {
        return productCategoryRepository.findById(pcid)
                .orElseThrow(() -> {
                    logger.error("ProductCategory with ID {} not found", pcid);
                    return new ProductCategoryNotFoundException(pcid);
                });
    }

    private ProductCategoryDTO convertToDTO(ProductCategory entity) {
        return ProductCategoryDTO.builder()
                .pcid(entity.getPcid())
                .pid(entity.getPid())
                .categoryId(entity.getCategoryId())
                .build();
    }

    private ProductCategory convertToEntity(ProductCategoryDTO dto) {
        return ProductCategory.builder()
                .pcid(dto.getPcid())
                .pid(dto.getPid())
                .categoryId(dto.getCategoryId())
                .build();
    }

    // Helper methods for validation
    private void validateCategoryAndProduct(Long pid, Long categoryId) {
        validateProduct(pid);
        validateCategory(categoryId);
    }

    private void validateProduct(Long pid) {
        if (!productRepository.existsById(pid)) {
            logger.error("Product with ID {} not found", pid);
            throw new ProductNotFoundException(pid);
        }
    }

    private void validateCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            logger.error("Category with ID {} not found", categoryId);
            throw new CategoryNotFoundException(categoryId);
        }
    }
}
