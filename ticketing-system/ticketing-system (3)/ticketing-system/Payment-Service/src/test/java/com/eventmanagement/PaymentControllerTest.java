package com.eventmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@Import(SecurityConfig.class)  // Assurez-vous d'importer la configuration de sécurité

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService; // Mock PaymentService directly

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testCreateCheckoutSession() throws Exception {
        Long reservationId = 1L;
        Long eventId = 2L; // Ajout de l'eventId
        Double amount = 100.0;
        String email = "test@example.com";
        String sessionUrl = "http://localhost:3000/checkout";

        // Mock the PaymentService response
        when(paymentService.createCheckoutSession(reservationId, eventId, amount, email)).thenReturn(sessionUrl);

        // Perform POST request and check the response
        mockMvc.perform(post("/api/payments/create-checkout-session")
                        .param("reservationId", String.valueOf(reservationId))
                        .param("eventId", String.valueOf(eventId)) // Ajout du paramètre eventId
                        .param("amount", String.valueOf(amount))
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$").value(sessionUrl)); // Expect the session URL as response
    }
}
