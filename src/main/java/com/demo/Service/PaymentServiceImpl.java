package com.demo.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.demo.DTO.PaymentDTO;
import com.demo.Entity.Payment;
import com.demo.Exception.CustomValidationException;
import com.demo.Exception.Payment.PaymentNotFoundException;
import com.demo.Interface.PaymentService;
import com.demo.Repository.PaymentRepository;
import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService 
{
    private final PaymentRepository paymentRepository;

    //Constructor to inject PaymentRepository
    public PaymentServiceImpl(PaymentRepository paymentRepository) 
    {
        this.paymentRepository = paymentRepository;
    }

    @Override   //Creates a new payment by validating the input DTO, converting it to an entity, and saving it
    public PaymentDTO createPayment(PaymentDTO paymentDTO) 
    {
        validatePaymentDTO(paymentDTO);                         //Validate payment data
        Payment payment = convertToEntity(paymentDTO);          //Convert DTO to entity
        return convertToDTO(paymentRepository.save(payment));   //Save the payment and convert it back to DTO
    }
    
    @Override   //Retrieves a payment by its ID, converting it to DTO format
    public PaymentDTO getPayment(Long pid) 
    {
        return convertToDTO(findPaymentById(pid));  //Convert the payment entity to DTO
    }

    @Override   //Retrieves all payments and converts them into a list of DTOs
    public List<PaymentDTO> getPayments() 
    {
        //Retreive all payment from repositiory, convert list to a stream for processing and each payment entity to DTO and collect converted DTO into a list
        return paymentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList()); 
    }

    @Override   //Partially updates an existing payment by its ID with the provided data in DTO
    @Transactional
    public PaymentDTO partialUpdatePayment(Long pid, PaymentDTO paymentDTO) 
    {
        //Fetch the existing payment by its ID
        Optional<Payment> existingPaymentOptional = paymentRepository.findById(pid);

        //If payment is not found, throw a PaymentNotFoundException
        if (existingPaymentOptional.isEmpty())
            throw new PaymentNotFoundException(pid);

        Payment existingPayment = existingPaymentOptional.get();                //Get the existing payment entity

        //Update fields from the PaymentDTO if they are not null
        if (paymentDTO.getAmount() != null)
            existingPayment.setAmount(paymentDTO.getAmount());                  //Update amount if provided

        if (paymentDTO.getPaymentMode() != null)
            existingPayment.setPaymentMode(paymentDTO.getPaymentMode());        //Update payment mode if provided

        if (paymentDTO.getPaymentStatus() != null)
            existingPayment.setPaymentStatus(paymentDTO.getPaymentStatus());    //Update payment status if provided
        
        existingPayment = paymentRepository.save(existingPayment);              //Save the updated payment entity
        
        return convertToDTO(existingPayment);                                   //Convert the updated Payment entity to PaymentDTO and return
    }

    @Override   //Deletes a payment by its ID
    public void deletePayment(Long pid) 
    {
        Payment payment = findPaymentById(pid); //Fetch the payment to be deleted
        paymentRepository.delete(payment);      //Delete the payment from the repository
    }

    //Validates the PaymentDTO object before creating or updating
    private void validatePaymentDTO(PaymentDTO paymentDTO) 
    {
        if (paymentDTO.getAmount() == null) 
            throw new CustomValidationException("Amount is mandatory");         //Amount is mandatory
        
        validateAmount(paymentDTO.getAmount());                                         //Ensure amount is greater than zero
        
        if (paymentDTO.getPaymentMode() == null)
            throw new CustomValidationException("Payment mode is mandatory");   //Payment mode is mandatory

        if (paymentDTO.getPaymentStatus() == null)
            throw new CustomValidationException("Payment status is mandatory"); //Payment status is mandatory
    }

    //Validates that the amount is greater than zero
    private void validateAmount(BigDecimal amount) 
    {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new CustomValidationException("Amount must be greater than zero.");           //Amount must be greater than zero
    }

    //Finds a payment by its ID or throws a PaymentNotFoundException if not found
    private Payment findPaymentById(Long pid) 
    {
        return paymentRepository.findById(pid).orElseThrow(() -> new PaymentNotFoundException(pid));    //If not found, throw exception
    }

    //Converts a Payment entity to a PaymentDTO
    private PaymentDTO convertToDTO(Payment payment) 
    {
        return PaymentDTO.builder().pid(payment.getPid()).amount(payment.getAmount()).paymentMode(payment.getPaymentMode())
        .paymentStatus(payment.getPaymentStatus()).paymentDateTime(payment.getPaymentDateTime()).build(); 
    }

    //Converts a PaymentDTO to a Payment entity
    private Payment convertToEntity(PaymentDTO paymentDTO) 
    {
        return Payment.builder().amount(paymentDTO.getAmount()).paymentMode(paymentDTO.getPaymentMode()) .paymentStatus(paymentDTO.getPaymentStatus())
        .paymentDateTime(paymentDTO.getPaymentDateTime() != null ? paymentDTO.getPaymentDateTime() : LocalDateTime.now()).build(); 
    }
}