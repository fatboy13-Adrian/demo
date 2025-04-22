package com.demo.Util.DataLoader.Order;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Order.Order;
import com.demo.Entity.Order.OrderProduct;
import com.demo.Repository.Product.ProductRepository;
import com.demo.Repository.Order.OrderProductRepository;
import com.demo.Repository.Order.OrderRepository;

import java.util.List;

@Component
public class OrderProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderProductDataLoader(ProductRepository productRepository, 
                                  OrderRepository orderRepository, 
                                  OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public void run(String... args) {
        if (orderProductRepository.count() == 0) {
            linkOrdersToProducts();
        }
    }

    @Transactional
    private void linkOrdersToProducts() {
        List<Order> orders = orderRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (orders.isEmpty() || products.isEmpty()) {
            System.out.println("No products or orders found to link.");
            return;
        }

        // Link orders to products in a round-robin fashion
        int productIndex = 0;
        for (Order order : orders) {
            Product product = products.get(productIndex);
            createAndSaveOrderProductLink(order, product);

            // Move to the next product (cycle back to the first product if needed)
            productIndex = (productIndex + 1) % products.size();
        }

        System.out.println("Successfully linked " + orders.size() + " orders with products.");
    }

    private void createAndSaveOrderProductLink(Order order, Product product) {
        // Ensure you're linking the Order (oid) and Product (pid)
        OrderProduct orderProduct = OrderProduct.builder()
                .oid(order)  // Order should be passed to oid
                .pid(product)  // Product should be passed to pid
                .build();
        
        orderProductRepository.save(orderProduct);
        System.out.println("Linked Order with Product ID: " + product.getPid());
    }
    
}
