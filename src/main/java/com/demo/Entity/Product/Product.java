package com.demo.Entity.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private Long pid;

    @Column(name = "productName")
    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @Column(name = "unitPrice")
    @NotNull(message = "Unit price is mandatory")
    private BigDecimal unitPrice;
}
