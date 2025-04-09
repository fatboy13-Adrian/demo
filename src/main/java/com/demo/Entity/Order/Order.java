package com.demo.Entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.demo.Enum.Order.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                 //Marks this class as a JPA entity
@Table(name = "orders") //Specifies the table name in the database
@Getter                 //Generates getters for all fields
@Setter                 //Generates setters for all fields
@NoArgsConstructor      //Generates a no-arguments constructor
@AllArgsConstructor     //Generates a constructor with arguments for all fields
@Builder                //Enables the builder pattern for creating instances of Order
public class Order 
{
    @Id                                                 //Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Automatically generates the primary key value
    @Column(name = "oid")                               //Specifies the column name in the table
    private Long oid;                                   //Order ID

    @Column(name = "totalPrice", nullable = false)                                  //Maps to the totalPrice column in the database
    @NotNull(message = "Total price is mandatory")                                  //Ensures the field is not null
    @DecimalMin(value = "0.01", message = "Total price must be greater than zero")  //Ensures the total price is greater than 0.01
    private BigDecimal totalPrice;                                                  //Total price of the order

    @Enumerated(EnumType.STRING)                    //Maps the enum to a string in the database
    @Column(name = "orderStatus", nullable = false) //Maps to the orderStatus column in the table
    @NotNull(message = "Order status is mandatory") //Ensures the field is not null
    private OrderStatus orderStatus;                //Status of the order (e.g., PENDING, COMPLETED)

    @Column(name = "orderDateTime", nullable = false, updatable = false)    //Maps to the orderDateTime column
    private LocalDateTime orderDateTime;                                    //Date and time when the order was created

    @PrePersist //This method is automatically called before the entity is persisted to the database
    protected void onCreate() 
    {
        this.orderDateTime = LocalDateTime.now();   //Sets the order date-time to the current time before saving
    }
}