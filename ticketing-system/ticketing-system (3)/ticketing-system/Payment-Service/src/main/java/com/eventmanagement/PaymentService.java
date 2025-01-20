package com.eventmanagement;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;




    public String createCheckoutSession(Long reservationId, Long eventId, Double amount, String email) throws StripeException {
        // Configuration de la clé API Stripe
        Stripe.apiKey = stripeSecretKey;

        // Créer une session Stripe Checkout avec l'eventId inclus
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // Ajouter les paramètres reservationId, eventId, amount, et email à l'URL de succès
                .setSuccessUrl("http://localhost:3000/success?session_id={CHECKOUT_SESSION_ID}&reservationId=" + reservationId
                        + "&eventId=" + eventId
                        + "&amount=" + amount
                        + "&email=" + email)
                .setCancelUrl("http://localhost:3000/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd") // Définir la devise en USD
                                                .setUnitAmount((long) (amount * 100)) // Convertir le montant en cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Réservation #" + reservationId) // Nom du produit
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                // Ajouter l'email du client pour Stripe
                .setCustomerEmail(email)
                .build();

        // Créer la session Stripe et renvoyer l'URL
        Session session = Session.create(params);
        return session.getUrl();
    }

}
