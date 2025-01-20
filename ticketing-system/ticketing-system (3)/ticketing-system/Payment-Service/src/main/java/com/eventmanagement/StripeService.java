package com.eventmanagement;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    public StripeService() {
        // Initialize Stripe with the secret key
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentIntent createPaymentIntent(long amount) throws StripeException {
        // Create a PaymentIntent with the amount in cents
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);  // Amount in cents
        params.put("currency", "usd"); // Currency (you can make it dynamic)

        // Create and return the PaymentIntent
        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(Long reservationId, Double amount, String email) throws StripeException {
        // Create the session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail(email)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amount.longValue() * 100) // Amount in cents
                                                .build())
                                .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:3000/cancel")
                .build();

        // Create and return the Stripe checkout session
        return Session.create(params);
    }
}
