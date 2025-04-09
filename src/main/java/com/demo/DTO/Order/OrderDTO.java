package com.demo.DTO.Order;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.demo.Enum.Order.OrderStatus;

@Getter             //Generates getters for all fields
@Setter             //Generates setters for all fields
@NoArgsConstructor  //Generates a no-arguments constructor
@AllArgsConstructor //Generates a constructor with arguments for all fields
@Builder            //Enables the builder pattern for creating instances of OrderDTO
public class OrderDTO 
{
    private Long oid;   //Order ID

    @NotNull(message = "Total price is mandatory")                                  //Ensures the field is not null
    @DecimalMin(value = "0.01", message = "Total price must be greater than zero")  //Ensures total price is greater than or equal to 0.01
    private BigDecimal totalPrice;                                                  //Total price of the order

    @NotNull(message = "Order status is mandatory")                                 //Ensures the field is not null
    private OrderStatus orderStatus;                                                //The status of the order (e.g., PENDING, COMPLETED)

    private LocalDateTime orderDateTime;                                            //The date and time the order was created
}