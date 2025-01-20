package com.eventmanagement;

import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @CrossOrigin(origins = "http://localhost:3000")

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestParam Long reservationId,
            @RequestParam Long eventId, // Ajout du paramètre eventId
            @RequestParam Double amount,
            @RequestParam String email) {
        try {
            // Appeler le service pour créer la session de paiement et obtenir l'URL
            String sessionUrl = paymentService.createCheckoutSession(reservationId, eventId, amount, email);
            return ResponseEntity.ok(sessionUrl);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de la session Stripe.");
        }
    }

}
