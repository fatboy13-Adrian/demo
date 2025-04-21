package com.demo.Entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catId")
    private Long catId;

    @Column(name = "category_name")
    @NotBlank(message = "Category name cannot be blank")
    private String catName;

    @Column(name = "description")
    @NotBlank(message = "Description cannot be blank")
    private String description;
}