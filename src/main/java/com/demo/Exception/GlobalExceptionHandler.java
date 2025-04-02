package com.demo.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.demo.Exception.Item.ItemNotFoundException;
import com.demo.Exception.Order.OrderNotFoundException;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Util.ErrorResponse;

//This class handles global exceptions for the entire application
@RestControllerAdvice   //This annotation indicates that the class will handle exceptions globally for REST controllers
public class GlobalExceptionHandler 
{
        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class); //Logger instance for logging error messages

    //Handles the specific OrderNotFoundException and returns a custom error response
    @ExceptionHandler(OrderNotFoundException.class)  //This method will catch OrderNotFoundException
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) 
    {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); //Returning a ResponseEntity with a custom error message and the 404 Not Found HTTP status
    }

    //Handles the specific ItemNotFoundException and returns a custom error response
    @ExceptionHandler(ItemNotFoundException.class)  //This method will catch ItemNotFoundException
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException ex) 
    {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); //Returning a ResponseEntity with a custom error message and the 404 Not Found HTTP status
    }

    @ExceptionHandler(IllegalArgumentException.class) //Handles exceptions globally within this controller
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) 
    {
        //Return 400 (Bad Request) with exception message
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    //Global Exception handler for Unsupported Media Types (415)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        logger.error("Unsupported Media Type: {}", ex.getMessage());
        return new ResponseEntity<>("Unsupported Media Type: " + ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<String> handleCustomValidationException(CustomValidationException ex) {
        // Return an appropriate response with error details
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Error: " + ex.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Object> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        // Log the exception (optional)
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), ex.getPid()), 
                HttpStatus.NOT_FOUND
        );
    }
    
    //A fallback method to handle any other unhandled exceptions in the application
    @ExceptionHandler(Exception.class)  //This will catch any general exception
    public ResponseEntity<String> handleGeneralException(Exception ex) 
    {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);   //Return a generic error message with a 500 Internal Server Error HTTP status
    }
}