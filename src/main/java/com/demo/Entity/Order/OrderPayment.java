package com.demo.Entity.Order;
import com.demo.Entity.Payment.Payment;
import jakarta.persistence.*;
import lombok.*;

@Entity                         //Marks the class as a JPA entity
@Table(name = "order_payment")  //Specifies the table name in the database
@Getter                         //Generates getter methods for all fields
@Setter                         //Generates setter methods for all fields
@NoArgsConstructor              //Generates a no-argument constructor
@AllArgsConstructor             //Generates a constructor with all fields as arguments
@Builder                        //Generates a builder pattern for creating instances
public class OrderPayment 
{
    @Id                                                                 //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)                 //Auto-increment the value of the primary key
    @Column(name = "poid")                                              //Maps the field to the corresponding column in the table
    private Long poid;                                                  //The unique identifier for the order-payment association

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Order
    @JoinColumn(name = "pid", nullable = false)                         //Specifies the foreign key column to join on
    private Payment pid;                                                //The order associated with this order record

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Payment
    @JoinColumn(name = "oid", nullable = false)                         //Specifies the foreign key column to join on
    private Order oid;                                                  //The order associated with this payment record
}