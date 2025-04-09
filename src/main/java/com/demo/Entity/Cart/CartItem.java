package com.demo.Entity.Cart;
import com.demo.Entity.Item;

import jakarta.persistence.*;
import lombok.*;

/**
 * Junction entity representing the many-to-many relationship between Item and Cart.
 * Each record associates one Item with one Cart entry.
 */
@Entity                     //Marks the class as a JPA entity
@Table(name = "cart_item")  //Specifies the table name in the database
@Getter                     //Generates getter methods for all fields
@Setter                     //Generates setter methods for all fields
@NoArgsConstructor          //Generates a no-argument constructor
@AllArgsConstructor         //Generates a constructor with all fields as arguments
@Builder                    //Generates a builder pattern for creating instances
public class CartItem 
{
    @Id                                                 //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increment the value of the primary key
    @Column(name = "ciid")                              //Maps the field to the corresponding column in the table
    private Long ciid;                                  //The unique identifier for the cart-item association

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Item
    @JoinColumn(name = "cid", nullable = false)                         //Specifies the foreign key column to join on
    private Item cid;                                                   //The cart associated with this item record

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Item
    @JoinColumn(name = "iid", nullable = false)                         //Specifies the foreign key column to join on
    private Item iid;                                                   //The item associated with this cart record
}