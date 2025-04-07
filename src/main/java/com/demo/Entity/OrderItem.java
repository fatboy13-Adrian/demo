package com.demo.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity                         //Marks the class as a JPA entity
@Table(name = "order_item") //Specifies the table name in the database
@Getter                         //Generates getter methods for all fields
@Setter                         //Generates setter methods for all fields
@NoArgsConstructor              //Generates a no-argument constructor
@AllArgsConstructor             //Generates a constructor with all fields as arguments
@Builder                        //Generates a builder pattern for creating instances
public class OrderItem 
{
    @Id                                                 //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increment the value of the primary key
    @Column(name = "oiid")                              //Maps the field to the corresponding column in the table
    private Long siid;                                  //The unique identifier for the order-item association

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Item
    @JoinColumn(name = "oid", nullable = false)                         //Specifies the foreign key column to join on
    private Item oid;                                                   //The order associated with this item record

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Item
    @JoinColumn(name = "iid", nullable = false)                         //Specifies the foreign key column to join on
    private Item iid;                                                   //The item associated with this order record
}
