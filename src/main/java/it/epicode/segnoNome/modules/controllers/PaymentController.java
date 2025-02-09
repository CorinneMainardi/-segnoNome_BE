package it.epicode.segnoNome.modules.controllers;

import it.epicode.segnoNome.modules.entities.PaymentMethod;
import it.epicode.segnoNome.modules.services.PaymentSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
     PaymentSvc paymentSvc;

    public PaymentController(PaymentSvc paymentSvc) {
        this.paymentSvc = paymentSvc;
    }

    @PostMapping("/addPayment")
    public ResponseEntity<PaymentMethod> addPaymentMethod(@AuthenticationPrincipal Long userId,
                                                          @RequestBody PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentSvc.addPaymentMethod(userId, paymentMethod));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentMethod>> getUserPayments(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentSvc.getUserPayments(userId));
    }
}