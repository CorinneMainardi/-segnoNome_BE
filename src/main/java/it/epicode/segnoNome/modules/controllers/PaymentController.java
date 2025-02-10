package it.epicode.segnoNome.modules.controllers;

import it.epicode.segnoNome.auth.entities.AppUser;
import it.epicode.segnoNome.modules.dto.PaymentMethodRequest;
import it.epicode.segnoNome.modules.entities.PaymentMethod;
import it.epicode.segnoNome.modules.services.PaymentSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<PaymentMethod> addPaymentMethod(
            @AuthenticationPrincipal AppUser user,
            @RequestBody PaymentMethodRequest paymentMethodRequest
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(paymentSvc.addPaymentMethod(user, paymentMethodRequest));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentMethod>> getUserPayments(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentSvc.getUserPayments(userId));
    }
}