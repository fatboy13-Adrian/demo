package com.demo.Service.Order;

import com.demo.DTO.Order.OrderProductDTO;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Order.Order;
import com.demo.Entity.Order.OrderProduct;
import com.demo.Exception.Order.OrderProductNotFoundException;
import com.demo.Interface.Order.OrderProductService;
import com.demo.Repository.Product.ProductRepository;
import com.demo.Repository.Order.OrderProductRepository;
import com.demo.Repository.Order.OrderRepository;  // Import OrderRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;  // Inject OrderRepository

    // Constructor-based injection of repositories
    public OrderProductServiceImpl(OrderProductRepository orderProductRepository,
                                    ProductRepository productRepository,
                                    OrderRepository orderRepository) {  // Include OrderRepository in constructor
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;  // Initialize the orderRepository
    }

    // Helper method to convert OrderProduct entity to OrderProductDTO
    private OrderProductDTO toDTO(OrderProduct orderProduct) {
        return OrderProductDTO.builder()
                .opid(orderProduct.getOpid())
                .oid(orderProduct.getOid().getOid())  // Assuming 'oid' is the order ID
                .pid(orderProduct.getPid().getPid())  // Assuming 'pid' is the product ID
                .build();
    }

    // Helper method to convert OrderProductDTO to OrderProduct entity
    private OrderProduct toEntity(OrderProductDTO dto) {
        // Lookup Order for 'oid' (which is an order reference)
        Order order = orderRepository.findById(dto.getOid()).orElseThrow(() -> new OrderProductNotFoundException(dto.getOid()));

        // Lookup Product for 'pid' (which is a product reference)
        Product product = productRepository.findById(dto.getPid()).orElseThrow(() -> new OrderProductNotFoundException(dto.getPid()));

        // Convert the DTO to an OrderProduct entity
        return OrderProduct.builder()
                .opid(dto.getOpid())
                .oid(order)
                .pid(product)
                .build();
    }

    @Override
    public OrderProductDTO createOrderProduct(OrderProductDTO dto) {
        OrderProduct entity = toEntity(dto);
        OrderProduct saved = orderProductRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public OrderProductDTO getOrderProduct(Long opid) {
        OrderProduct orderProduct = orderProductRepository.findById(opid).orElseThrow(() -> new OrderProductNotFoundException(opid));
        return toDTO(orderProduct);
    }

    @Override
    public List<OrderProductDTO> getOrderProducts() {
        return orderProductRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderProductDTO updateOrderProduct(Long opid, OrderProductDTO dto) {
        orderProductRepository.findById(opid).orElseThrow(() -> new OrderProductNotFoundException(opid));

        dto = OrderProductDTO.builder().opid(opid).oid(dto.getOid()).pid(dto.getPid()).build();

        OrderProduct updated = toEntity(dto);
        OrderProduct saved = orderProductRepository.save(updated);
        return toDTO(saved);
    }

    @Override
    public void deleteOrderProduct(Long opid) {
        OrderProduct orderProduct = orderProductRepository.findById(opid).orElseThrow(() -> new OrderProductNotFoundException(opid));
        orderProductRepository.delete(orderProduct);
    }
}
