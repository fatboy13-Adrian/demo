package com.demo.Entity.Order;
import com.demo.Entity.Product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity                     //Marks the class as a JPA entity
@Table(name = "order_product") //Specifies the table name in the database
@Getter                     //Generates getter methods for all fields
@Setter                     //Generates setter methods for all fields
@NoArgsConstructor          //Generates a no-argument constructor
@AllArgsConstructor         //Generates a constructor with all fields as arguments
@Builder                    //Generates a builder pattern for creating instances
public class OrderProduct 
{
    @Id                                                                 //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)                 //Auto-increment the value of the primary key
    @Column(name = "opid")                                              //Maps the field to the corresponding column in the table
    private Long opid;                                                  //The unique identifier for the order-product association

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Product
    @JoinColumn(name = "oid", nullable = false)                         //Specifies the foreign key column to join on
    private Order oid;                                                  //The order associated with this product record

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Product
    @JoinColumn(name = "pid", nullable = false)                         //Specifies the foreign key column to join on
    private Product pid;                                                //The product associated with this order record
}
