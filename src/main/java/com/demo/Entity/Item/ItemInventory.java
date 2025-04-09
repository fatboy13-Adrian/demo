package com.demo.Entity.Item;
import com.demo.Entity.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.*;

/**
 * Junction entity representing the many-to-many relationship between Item and Inventory.
 * Each record associates one Item with one Inventory entry.
 */
@Entity                         //Marks the class as a JPA entity
@Table(name = "item_inventory") //Specifies the table name in the database
@Getter                         //Generates getter methods for all fields
@Setter                         //Generates setter methods for all fields
@NoArgsConstructor              //Generates a no-argument constructor
@AllArgsConstructor             //Generates a constructor with all fields as arguments
@Builder                        //Generates a builder pattern for creating instances
public class ItemInventory 
{
    @Id                                                 //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-increment the value of the primary key
    @Column(name = "siid")                              //Maps the field to the corresponding column in the table
    private Long siid;                                  //The unique identifier for the item-inventory association

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Item
    @JoinColumn(name = "iid", nullable = false)                         //Specifies the foreign key column to join on
    private Item iid;                                                   //The item associated with this inventory record

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)    //Defines a many-to-one relationship with Inventory
    @JoinColumn(name = "sid", nullable = false)                         //Specifies the foreign key column to join on
    private Inventory sid;                                              //The inventory associated with this item record
}