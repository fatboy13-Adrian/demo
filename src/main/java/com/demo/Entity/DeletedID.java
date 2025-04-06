package com.demo.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity                         //This marks the class as a JPA entity (mapped to a database table)
@Table(name = "deleted_ids")    //Specifies the table name in the database that this entity maps to        
@Getter                         //Automatically generates getter methods for all fields
@Setter                         //Automatically generates setter methods for all fields
@NoArgsConstructor              //Generates a no-argument constructor
@AllArgsConstructor             //Generates a constructor with arguments for all fields
@Builder                        //Enables the builder pattern to create instances of this class easily
public class DeletedID 
{  
    @Id //Marks the field as the primary key for the entity and uses an auto-generated value strategy for it
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Automatically generate the primary key (if necessary)
    private Long id;                                    //Unique identifier for the deleted ID entry

    //Marks this field as a column in the database with a not-null constraint and validation on input
    @Column(name = "deleted_id", nullable = false)
    @NotBlank(message = "Deleted ID cannot be blank")   //Ensures that deletedId is not blank
    private Long deletedId;                             //The actual ID from the main entity (e.g., Order, Customer)

    //Marks this field as a column in the database with a not-null constraint and validation on input
    @Column(name = "entity_type", nullable = false)
    @NotBlank(message = "Entity type cannot be blank")  //Ensures that entityType is not blank
    private String entityType;                          //The type of entity (e.g., Order, Customer)
}