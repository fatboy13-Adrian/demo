package com.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication	//This annotation marks the class as a Spring Boot application. 
//It combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
public class DemoApplication
{
    //The main method serves as the entry point for the Spring Boot application
    public static void main(String[] args) 
	{
        SpringApplication.run(DemoApplication.class, args);	//SpringApplication.run starts the application. It bootstraps the Spring context and launches the application.
    }
}