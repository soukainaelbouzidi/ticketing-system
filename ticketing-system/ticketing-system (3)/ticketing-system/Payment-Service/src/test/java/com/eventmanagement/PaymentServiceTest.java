package com.eventmanagement;

import com.stripe.Stripe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentServiceTest {

    @BeforeAll
    public static void setup() {
        // Configurer la clé API Stripe avant d'exécuter les tests
        Stripe.apiKey = "sk_test_51QVfdtKycUC9MuJlVXENy5FiCixevJpVimucQXfmL3KjUqNK3iJmqvs6Vcsmlnj040EG1rQzl63KK07aWksfMpzR00U4yLmXOl";
    }

    @Test
    public void testCreateCheckoutSession() {
        // Implémentez ici votre test pour créer une session de paiement
        System.out.println("Test Stripe Checkout Session started");

        // Exemple d'appel à Stripe pour tester une session
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", Arrays.asList("card"));
            params.put("line_items", Arrays.asList(
                    Map.of("price_data", Map.of(
                                    "currency", "usd",
                                    "product_data", Map.of("name", "Test Ticket"),
                                    "unit_amount", 2000
                            ),
                            "quantity", 1)
            ));
            params.put("mode", "payment");
            params.put("success_url", "https://example.com/success");
            params.put("cancel_url", "https://example.com/cancel");

            com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(params);

            System.out.println("Checkout session ID: " + session.getId());
            assertNotNull(session.getId());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Stripe session creation failed.");
        }
    }
}
