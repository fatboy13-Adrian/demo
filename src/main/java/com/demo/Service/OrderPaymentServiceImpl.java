package com.demo.Service;
import com.demo.DTO.OrderPaymentDTO;  
import com.demo.Entity.OrderPayment;  
import com.demo.Entity.Payment;  
import com.demo.Exception.Order.OrderPaymentNotFoundException;  
import com.demo.Exception.Payment.PaymentNotFoundException;  
import com.demo.Interface.OrderPaymentService;  
import com.demo.Repository.OrderPaymentRepository;  
import com.demo.Repository.PaymentRepository;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  
import java.util.List;  
import java.util.stream.Collectors;  

@Service        //Marks this class as a Spring service, indicating it's a service component
@Transactional  //Ensures that methods in this class run within a transaction context
public class OrderPaymentServiceImpl implements OrderPaymentService 
{
    private static final Logger logger = LoggerFactory.getLogger(OrderPaymentServiceImpl.class);    //Logger to log service actions
    private final OrderPaymentRepository orderPaymentRepository;    //Repository for handling OrderPayment entities
    private final PaymentRepository paymentRepository;              //Repository for handling Payment entities

    //Constructor to inject dependencies (repositories) into this service
    public OrderPaymentServiceImpl(OrderPaymentRepository orderPaymentRepository, PaymentRepository paymentRepository) 
    {
        this.orderPaymentRepository = orderPaymentRepository;   //Initializes the OrderPaymentRepository
        this.paymentRepository = paymentRepository;             //Initializes the PaymentRepository
    }

    //Method to convert OrderPayment entity to OrderPaymentDTO
    private OrderPaymentDTO toDTO(OrderPayment orderPayment) 
    {
        return OrderPaymentDTO.builder().poid(orderPayment.getPoid()).oid(orderPayment.getOid().getOid())
        .pid(orderPayment.getPid().getPid()).build();
    }

    //Method to convert OrderPaymentDTO to OrderPayment entity
    private OrderPayment toEntity(OrderPaymentDTO dto) 
    {
        Payment orderPayment = findPaymentById(dto.getOid());   //Find Order (OID) by ID from DTO
        Payment payment = findPaymentById(dto.getPid());        //Find Payment (PID) by ID from DTO
        return OrderPayment.builder().poid(dto.getPoid()).pid(orderPayment).pid(payment).build();
    }

    //Helper method to find a Payment by ID and throw exception if not found
    private Payment findPaymentById(Long id) 
    {
        //Find Payment entity by ID and throw exception if Payment is not found
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public OrderPaymentDTO createOrderPayment(OrderPaymentDTO dto) 
    {
        logger.info("Creating OrderPayment with POID: {}", dto.getPoid());  //Log the start of OrderPayment creation
        OrderPayment entity = toEntity(dto);                                        //Convert OrderPaymentDTO to OrderPayment entity
        OrderPayment saved = orderPaymentRepository.save(entity);                   //Save the entity to the repository
        logger.info("OrderPayment created with POID: {}", saved.getPoid()); //Log successful creation
        return toDTO(saved);    //Return the created OrderPayment as DTO
    }

    @Override
    public OrderPaymentDTO getOrderPayment(Long poid) 
    {
        logger.info("Fetching OrderPayment with POID: {}", poid);   //Log fetching process

        //Fetch OrderPayment entity by POID from the repository
        OrderPayment orderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid));

        return toDTO(orderPayment);                                         //Convert OrderPayment entity to DTO and return
    }

    @Override
    public List<OrderPaymentDTO> getOrderPayments() 
    {
        logger.info("Fetching all OrderPayments."); //Log fetching all OrderPayments

        //Fetch all OrderPayments from the repository
        List<OrderPayment> orderPayments = orderPaymentRepository.findAll();

        if (orderPayments.isEmpty()) 
            logger.warn("No OrderPayments found."); //Log warning if no OrderPayments found

        //Convert all OrderPayments to DTOs and return as a list
        return orderPayments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrderPaymentDTO updateOrderPayment(Long poid, OrderPaymentDTO dto) 
    {
        logger.info("Updating OrderPayment with POID: {}", poid);   //Log the update process

        //Fetch the existing OrderPayment entity to update
        OrderPayment existingOrderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid)); 

        //Find updated Payments for OID and PID from DTO
        Payment updatedOrderPayment = findPaymentById(dto.getOid());
        Payment updatedPayment = findPaymentById(dto.getPid());

        //Update the existing OrderPayment entity with new values
        existingOrderPayment.setPid(updatedOrderPayment);   //Set updated Order (OID)
        existingOrderPayment.setPid(updatedPayment);        //Set updated Payment (PID)

        //Save and return the updated OrderPayment as DTO
        OrderPayment updatedEntity = orderPaymentRepository.save(existingOrderPayment);
        logger.info("OrderPayment updated with POID: {}", updatedEntity.getPoid()); //Log the successful update
        return toDTO(updatedEntity);    //Return the updated OrderPayment as DTO
    }

    @Override
    public void deleteOrderPayment(Long poid) 
    {
        logger.info("Deleting OrderPayment with POID: {}", poid);   //Log the deletion process

        //Fetch the OrderPayment entity to delete
        OrderPayment orderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid)); 

        //Delete the found OrderPayment entity
        orderPaymentRepository.delete(orderPayment);
        logger.info("OrderPayment with POID: {} deleted.", poid);   //Log successful deletion
    }
}