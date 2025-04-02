package com.demo.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.demo.Enum.OrderStatus;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Long oid;

    @Column(name = "totalPrice", nullable = false)
    @NotNull(message = "Total price is mandatory")
    @DecimalMin(value = "0.01", message = "Total price must be greater than zero")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    @NotNull(message = "Order status is mandatory")
    private OrderStatus orderStatus;

    @Column(name = "orderDateTime", nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @PrePersist
    protected void onCreate() {
        this.orderDateTime = LocalDateTime.now();
    }
}