package com.demo.DTO.Product;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long pid;
    private String productName;
    private BigDecimal unitPrice;
}