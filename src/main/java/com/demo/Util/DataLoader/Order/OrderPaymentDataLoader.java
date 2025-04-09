package com.demo.Util.DataLoader.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.demo.Entity.Order;
import com.demo.Entity.OrderPayment;
import com.demo.Entity.Payment;
import com.demo.Repository.OrderPaymentRepository;
import com.demo.Repository.OrderRepository;
import com.demo.Repository.PaymentRepository;
import java.util.List;

@Component  //Marks this class as a Spring-managed component, allowing it to be automatically detected and instantiated
public class OrderPaymentDataLoader implements CommandLineRunner 
{
    //Repositories to interact with the database
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    //Constructor-based dependency injection
    public OrderPaymentDataLoader(PaymentRepository paymentRepository, OrderRepository orderRepository, OrderPaymentRepository orderPaymentRepository) 
    {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderPaymentRepository = orderPaymentRepository;
    }

    @Override
    public void run(String... args) 
    {
        //Executes after the application context is loaded
        //Only execute linking if there are no existing OrderPayment entries
        if (orderPaymentRepository.count() == 0)
            linkOrdersWithPayments();   //Proceed to link Orders and Payments
    }

    @Transactional  //Ensures the database operations are executed in a single transaction
    private void linkOrdersWithPayments() 
    {
        List<Order> orders = fetchOrders();         //Fetch all orders
        List<Payment> payments = fetchPayments();   //Fetch all payments

        //Abort if there are no orders or payments to work with
        if (orders.isEmpty() || payments.isEmpty()) 
        {
            logError("Orders or Payments are missing. Skipping linkage.");  //Log an error and stop
            return;
        }
        
        linkOrdersToPayments(orders, payments); //Perform the linking logic (each order gets assigned a payment in round-robin fashion)
    }

    //Retrieve all Order entities from the database
    private List<Order> fetchOrders() 
    {
        return orderRepository.findAll();
    }

    //Retrieve all Payment entities from the database
    private List<Payment> fetchPayments() 
    {
        return paymentRepository.findAll();
    }

    //Links each Order to a Payment, distributing payments evenly (round-robin)
    private void linkOrdersToPayments(List<Order> orders, List<Payment> payments) 
    {
        int paymentIndex = 0;   //Index to keep track of which payment to assign

        for (Order order : orders) 
        {
            //Get the current payment from the list using the index
            Payment payment = payments.get(paymentIndex);

            //Create a new OrderPayment record linking the order and payment
            createAndSaveOrderPaymentLink(order, payment);

            //Move to the next payment; loop back to start if end is reached
            paymentIndex = (paymentIndex + 1) % payments.size();
        }

        //Log the successful operation
        logInfo("Linked " + orders.size() + " orders with payments.");
    }

    //Creates and persists an OrderPayment link between an Order and a Payment
    private void createAndSaveOrderPaymentLink(Order order, Payment payment) 
    {
        OrderPayment orderPayment = new OrderPayment(); //Create new link entity
        orderPayment.setOid(order);                     //Set the associated Order
        orderPayment.setPid(payment);                   //Set the associated Payment
        orderPaymentRepository.save(orderPayment);      //Save the link to the database

        //Log the successful link creation
        logInfo("Linked Order (ID: " + order.getOid() + ") with Payment (ID: " + payment.getPid() + ")");
    }

    //Utility method for logging informational messages
    private void logInfo(String message) 
    {
        System.out.println("[INFO] " + message);
    }

    //Utility method for logging error messages
    private void logError(String message) 
    {
        System.err.println("[ERROR] " + message);
    }
}