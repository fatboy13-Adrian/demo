package com.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  //This annotation already includes @ComponentScan, so no need to explicitly define it
public class DemoApplication 
{
    //Main method to start the Spring Boot application
    public static void main(String[] args) 
    {
        SpringApplication.run(DemoApplication.class, args);
    }
}