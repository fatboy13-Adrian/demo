package com.demo.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Exception.Category.InvalidCategoryException;
import com.demo.Exception.Item.ItemInventoryNotFoundException;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Exception.Order.OrderNotFoundException;

//Global exception handler for all REST controllers in the application
@RestControllerAdvice
public class GlobalExceptionHandler 
{
    //Logger instance for logging error messages
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrderNotFoundException.class) //Handles OrderNotFoundException and returns a custom error response
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) 
    {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);    //Builds a response with 404 status and the exception message
    }

    @ExceptionHandler(ItemNotFoundException.class)  //Handles ItemNotFoundException and returns a custom error response
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException ex) 
    {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);    //Builds a response with 404 status and the exception message
    }

    @ExceptionHandler(IllegalArgumentException.class)   //Handles IllegalArgumentException and returns a custom error response
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) 
    {
        //Builds a response with 400 status indicating bad request, along with the exception message
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class) //Handles HttpMediaTypeNotSupportedException and returns a custom error response
    public ResponseEntity<String> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) 
    {
        //Logs the error message with specific info about unsupported media type
        logger.error("Unsupported Media Type: {}", ex.getMessage());

        //Builds a response with 415 status indicating unsupported media type
        return buildErrorResponse("Unsupported Media Type: " + ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    @ExceptionHandler(CustomValidationException.class)  //Handles CustomValidationException and returns a custom error response
    public ResponseEntity<String> handleCustomValidationException(CustomValidationException ex) 
    {
        //Builds a response with 400 status indicating validation error
        return buildErrorResponse("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemInventoryNotFoundException.class) //Handles ItemInventoryNotFoundException and returns a custom error response
    public ResponseEntity<String> handleItemInventoryNotFound(ItemInventoryNotFoundException ex) 
    {
        //Builds a response with 404 status indicating that the item inventory was not found
        return buildErrorResponse("ItemInventory not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)    //Handles EntityNotFoundException and returns a custom error response
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        //Builds a response with 404 status indicating that the entity was not found
        return buildErrorResponse("Entity not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeletedIdNotFoundException.class) //Handles DeletedIdNotFoundException and returns a custom error response
    public ResponseEntity<String> handleDeletedIdNotFound(DeletedIdNotFoundException ex) 
    {
        //Builds a response with 404 status indicating that the deleted ID was not found
        return buildErrorResponse("Deleted ID not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)  //Fallback method for handling any unhandled exceptions
    public ResponseEntity<String> handleGeneralException(Exception ex) 
    {
        //Logs the unexpected error with detailed message and stack trace
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        //Builds a response with 500 status indicating internal server error
        return buildErrorResponse("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Helper method to create a consistent error response from an exception with the given status code
    private ResponseEntity<String> buildErrorResponse(Exception ex, HttpStatus status) 
    {
        //Log the detailed exception message and stack trace
        logger.error("Error occurred: {}", ex.getMessage(), ex);

        //Build and return a response entity with the exception message and the provided status code
        return buildErrorResponse(ex.getMessage(), status);
    }

    //Helper method to create a consistent error response using a string message and the given status code
    private ResponseEntity<String> buildErrorResponse(String message, HttpStatus status) 
    {
        return new ResponseEntity<>(message, status);   //Return a ResponseEntity containing the error message and the appropriate HTTP status
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFound(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<String> handleInvalidCategory(InvalidCategoryException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
