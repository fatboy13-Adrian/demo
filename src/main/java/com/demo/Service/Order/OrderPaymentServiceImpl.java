package com.demo.Service.Order;
import com.demo.Entity.Order.Order;  
import com.demo.Entity.Order.OrderPayment;  
import com.demo.Entity.Payment.Payment;  
import com.demo.Exception.Payment.PaymentNotFoundException;  
import com.demo.Exception.Order.OrderPaymentNotFoundException;  
import com.demo.Repository.Order.OrderPaymentRepository;  
import com.demo.Repository.Payment.PaymentRepository;  
import com.demo.DTO.Order.OrderPaymentDTO;  
import com.demo.Interface.Order.OrderPaymentService;  
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;  
import java.util.List;  
import java.util.stream.Collectors; 

@Service  //Indicate that this class is a Spring service
public class OrderPaymentServiceImpl implements OrderPaymentService 
{
    private final OrderPaymentRepository orderPaymentRepository;    //Repository to manage OrderPayment entities
    private final PaymentRepository paymentRepository;              //Repository to manage Payment entities

    @Autowired  //Automatically inject dependencies via constructor
    public OrderPaymentServiceImpl(OrderPaymentRepository orderPaymentRepository, PaymentRepository paymentRepository) 
    {
        this.orderPaymentRepository = orderPaymentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public OrderPaymentDTO createOrderPayment(OrderPaymentDTO dto) 
    {
        validateOrderPaymentDTO(dto);   //Validate the DTO to ensure it has correct data

        //Retrieve the Payment entity using the Payment ID from the DTO
        Payment payment = paymentRepository.findById(dto.getPid()).orElseThrow(() -> new PaymentNotFoundException(dto.getPid()));

        //Create a new Order object and set its Order ID
        Order order = new Order();
        order.setOid(dto.getOid());

        //Create a new OrderPayment entity, associate the Order and Payment entities with it
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setOid(order);
        orderPayment.setPid(payment);

        //Save the OrderPayment entity to the database
        OrderPayment savedOrderPayment = orderPaymentRepository.save(orderPayment);

        //Return a DTO with the saved OrderPayment's details
        return new OrderPaymentDTO(savedOrderPayment.getPoid(), order.getOid(), payment.getPid());
    }

    @Override
    public OrderPaymentDTO getOrderPayment(Long poid) 
    {
        if (poid == null)
            throw new IllegalArgumentException("POID cannot be null");

        //Retrieve the OrderPayment by POID, throw exception if not found
        OrderPayment orderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid));

        //Retrieve the associated Payment entity using its Payment ID
        Payment payment = paymentRepository.findById(orderPayment.getPid().getPid()).orElseThrow(() -> 
        new PaymentNotFoundException(orderPayment.getPid().getPid()));

        //Return a DTO containing both Order and Payment details
        return new OrderPaymentDTO(orderPayment.getPoid(), orderPayment.getOid().getOid(), payment.getPid());
    }

    @Override
    public List<OrderPaymentDTO> getOrderPayments() 
    {
        //Retrieve all OrderPayments from the repository
        List<OrderPayment> orderPayments = (List<OrderPayment>) orderPaymentRepository.findAll();

        //Map each OrderPayment entity to a DTO and return the list
        return orderPayments.stream().map(orderPayment -> new OrderPaymentDTO(orderPayment.getPoid(), orderPayment.getOid().getOid(), 
        orderPayment.getPid().getPid())).collect(Collectors.toList());
    }

    @Override
    public OrderPaymentDTO updateOrderPayment(Long poid, OrderPaymentDTO dto) 
    {
        validateOrderPaymentDTO(dto);   //Validate the DTO input

        if (poid == null)
            throw new IllegalArgumentException("POID cannot be null");

        //Retrieve the existing OrderPayment entity by POID
        OrderPayment existingOrderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid));

        //Retrieve the associated Payment entity by its Payment ID
        Payment payment = paymentRepository.findById(dto.getPid()).orElseThrow(() -> new PaymentNotFoundException(dto.getPid()));

        //Update the Order with the new Order ID from DTO
        Order order = existingOrderPayment.getOid();
        order.setOid(dto.getOid());

        //Update the existing OrderPayment with new Order and Payment
        existingOrderPayment.setOid(order);
        existingOrderPayment.setPid(payment);

        //Save the updated OrderPayment entity
        OrderPayment updatedOrderPayment = orderPaymentRepository.save(existingOrderPayment);

        //Return a DTO containing the updated OrderPayment details
        return new OrderPaymentDTO(updatedOrderPayment.getPoid(), order.getOid(), payment.getPid());
    }

    @Override
    public void deleteOrderPayment(Long poid) 
    {
        if (poid == null) 
            throw new IllegalArgumentException("POID cannot be null");

        //Retrieve the OrderPayment entity by POID
        OrderPayment orderPayment = orderPaymentRepository.findById(poid).orElseThrow(() -> new OrderPaymentNotFoundException(poid));
        orderPaymentRepository.delete(orderPayment);    //Delete the retrieved OrderPayment entity
    }

    //Helper method to validate the OrderPaymentDTO
    private void validateOrderPaymentDTO(OrderPaymentDTO dto) 
    {
        if (dto == null)
            throw new IllegalArgumentException("OrderPaymentDTO cannot be null");

        if (dto.getOid() == null || dto.getOid() <= 0) 
            throw new IllegalArgumentException("Invalid Order ID");
    
        if (dto.getPid() == null || dto.getPid() <= 0)  
            throw new IllegalArgumentException("Invalid Payment ID");
    }
}