package com.demo.Controller;

import com.demo.DTO.PaymentDTO;
import com.demo.Enum.PaymentMode;
import com.demo.Enum.PaymentStatus;
import com.demo.Service.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        return ResponseEntity.ok(paymentService.createPayment(paymentDTO));
    }

    @GetMapping("/{pid}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long pid) {
        return ResponseEntity.ok(paymentService.getPayment(pid));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }

    @PatchMapping("/{pid}")
    public ResponseEntity<PaymentDTO> partialUpdatePayment(
            @PathVariable Long pid,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) PaymentMode paymentMode,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestBody(required = false) PaymentDTO paymentDTO) {
        return ResponseEntity.ok(paymentService.partialUpdatePayment(pid, amount, paymentMode, paymentStatus, paymentDTO));
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long pid) {
        paymentService.deletePayment(pid);
        return ResponseEntity.noContent().build();
    }
}
