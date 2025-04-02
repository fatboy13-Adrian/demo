package com.demo.DTO;

import com.demo.Enum.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    @Setter(AccessLevel.NONE) // Prevents modification of the primary key
    private Long oid;

    @NotNull(message = "Total price is mandatory")
    @DecimalMin(value = "0.01", message = "Total price must be greater than zero")
    private BigDecimal totalPrice;

    @NotNull(message = "Order status is mandatory")
    private OrderStatus orderStatus;

    private LocalDateTime orderDateTime;
}