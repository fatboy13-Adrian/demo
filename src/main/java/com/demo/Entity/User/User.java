package com.demo.Entity.User;
import com.demo.Enum.User.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")   //Specifies the table name in the database as "users"
@Getter                   //Automatically generates getter methods for all fields
@Setter                   //Automatically generates setter methods for all fields
@NoArgsConstructor        //Generates a no-argument constructor (needed by JPA)
@AllArgsConstructor       //Generates a constructor with arguments for all fields
@Builder                  //Enables the builder pattern to easily create instances
public class User 
{
    @Id                                                     //Marks this field as the primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //Auto-incrementing ID generation
    @Column(name = "uid")                                   //Specifies the column name in the database as "uid"
    private Long uid;                                       //Unique identifier for the user

    @Column(name = "username", nullable = false)            //Specifies the "username" column and makes it non-nullable
    @NotBlank(message = "Username is mandatory")            //Validation: Username must not be blank
    private String username;                                //Username for the user

    @Column(name = "email", nullable = false)               //Specifies the "email" column and makes it non-nullable
    @NotBlank(message = "Email is mandatory")               //Validation: Email must not be blank
    private String email;                                   //Email for the user

    @Enumerated(EnumType.STRING)                            //Stores the enum value as a String in the database
    @Column(name = "user_role", nullable = false)           //Specifies the "user_role" column and makes it non-nullable
    @NotBlank(message = "User role needs to be defined")    //Validation: Role must not be blank
    private Role role;                                      //Role for the user (e.g., ADMIN, USER)

    @Column(name = "password", nullable = false)            //Specifies the "password" column and makes it non-nullable
    @NotBlank(message = "Password is mandatory")            //Validation: Password must not be blank
    private String password;                                //Password for the user

    //Method to hash the password before saving to the database
    public void setPassword(String password) 
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  //Create an instance of BCryptPasswordEncoder
        this.password = passwordEncoder.encode(password);               //Encode the password using BCrypt
    }

    //Method to validate the password during login
    public boolean validatePassword(String rawPassword) 
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  //Create an instance of BCryptPasswordEncoder
        return passwordEncoder.matches(rawPassword, this.password);     //Check if the raw password matches the hashed password
    }
}