package com.demo.Controller.Payment;
import com.demo.DTO.Payment.PaymentDTO;
import com.demo.Service.Payment.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payments")  //Base URL for payment-related API endpoints
public class PaymentController 
{
    private final PaymentServiceImpl paymentService;

    //Constructor injection of the PaymentServiceImpl to handle business logic
    public PaymentController(PaymentServiceImpl paymentService) 
    {
        this.paymentService = paymentService;
    }

    //Endpoint to create a new payment
    @PostMapping  //Maps HTTP POST requests to this method
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) 
    {
        //Call service layer to create the payment and return the created PaymentDTO
        return ResponseEntity.ok(paymentService.createPayment(paymentDTO));  
    }

    //Endpoint to retrieve a payment by its ID
    @GetMapping("/{pid}")   //Maps HTTP GET requests with a payment ID parameter
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long pid) 
    {
        //Call service layer to fetch the payment by its ID and return the PaymentDTO
        return ResponseEntity.ok(paymentService.getPayment(pid));  
    }

    //Endpoint to retrieve all payments
    @GetMapping //Maps HTTP GET requests to retrieve a list of all payments
    public ResponseEntity<List<PaymentDTO>> getPayments() 
    {
        //Call service layer to fetch all payments and return as a list of PaymentDTOs
        return ResponseEntity.ok(paymentService.getPayments());  
    }

    //Endpoint to partially update a payment (e.g., update payment status)
    @PatchMapping("/{pid}")     //Maps HTTP PATCH requests with a payment ID parameter for partial update
    public ResponseEntity<PaymentDTO> partialUpdatePayment(@PathVariable Long pid, @RequestBody PaymentDTO paymentDTO) 
    {
        //Call service layer to partially update the payment and return the updated PaymentDTO
        return ResponseEntity.ok(paymentService.partialUpdatePayment(pid, paymentDTO));
    }

    //Endpoint to delete a payment by its ID
    @DeleteMapping("/{pid}")    //Maps HTTP DELETE requests with a payment ID parameter
    public ResponseEntity<Void> deletePayment(@PathVariable Long pid) 
    {
        paymentService.deletePayment(pid);           //Call service layer to delete the payment by its ID
        return ResponseEntity.noContent().build();  //Return HTTP status 204 (No Content) indicating successful deletion      
    }
}