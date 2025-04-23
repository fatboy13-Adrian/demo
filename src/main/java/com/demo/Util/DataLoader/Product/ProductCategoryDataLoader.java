package com.demo.Util.DataLoader.Product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.demo.Entity.Category.Category;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Product.ProductCategory;
import com.demo.Repository.Category.CategoryRepository;
import com.demo.Repository.Product.ProductCategoryRepository;
import com.demo.Repository.Product.ProductRepository;

import java.util.List;

@Component
public class ProductCategoryDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryDataLoader(ProductRepository productRepository,
                                     CategoryRepository categoryRepository,
                                     ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public void run(String... args) {
        if (productCategoryRepository.count() == 0) {
            linkExistingProductsWithCategories();
        }
    }

    @Transactional
    private void linkExistingProductsWithCategories() {
        List<Product> products = productRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        if (products.isEmpty() || categories.isEmpty()) {
            System.out.println("No existing Products or Categories found. Skipping linkage.");
            return;
        }

        int categoryIndex = 0;

        for (Product product : products) {
            Category category = categories.get(categoryIndex);
            createAndSaveProductCategoryLink(product.getPid(), category.getCategoryId());
            categoryIndex = (categoryIndex + 1) % categories.size();
        }

        System.out.println("Linked " + products.size() + " products with categories.");
    }

    private void createAndSaveProductCategoryLink(Long pid, Long categoryId) {
        ProductCategory productCategory = ProductCategory.builder()
                .pid(pid)
                .categoryId(categoryId)
                .build();
        productCategoryRepository.save(productCategory);
        System.out.println("Linked Product ID: " + pid + " with Category ID: " + categoryId);
    }
}
