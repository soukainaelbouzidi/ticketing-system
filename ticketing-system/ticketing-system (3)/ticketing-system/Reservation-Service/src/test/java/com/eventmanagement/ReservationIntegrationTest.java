package com.eventmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationIntegrationTest {

    @Autowired
    private EventServiceClient eventServiceClient;  // Injecting EventServiceClient

    private static final Long EVENT_ID = 1L;  // Example Event ID
    private static final int SEATS_RESERVED = 2;  // Number of seats to reserve

    @BeforeEach
    void setUp() {
        // You may want to set up the event data before each test (if needed)
    }

    @Test
    void testCheckEventAvailability() {
        // Using the EventServiceClient to check if the event has available seats
        Boolean isAvailable = eventServiceClient.checkAvailability(EVENT_ID);

        // Assert that the event is available (or adjust the assertion based on your test data)
        assertTrue(isAvailable, "Event should have available seats");
    }

    @Test
    void testReserveSeats() {
        // Premièrement, vérifier si l'événement a suffisamment de sièges disponibles
        int initialSeats = eventServiceClient.getAvailableSeats(EVENT_ID);
        assertTrue(initialSeats >= SEATS_RESERVED, "Il ne reste pas assez de sièges disponibles pour cette réservation");

        // Tenter de réserver les sièges
        try {
            // Mise à jour du nombre de sièges disponibles après réservation
            eventServiceClient.updateSeatsAvailable(EVENT_ID, SEATS_RESERVED);

            // Récupérer le nombre de sièges disponibles après la réservation
            int availableSeatsAfterReservation = eventServiceClient.getAvailableSeats(EVENT_ID);

            // Vérifier que le nombre de sièges disponibles a bien diminué de SEATS_RESERVED
            assertEquals(initialSeats - SEATS_RESERVED, availableSeatsAfterReservation,
                    "Les sièges disponibles devraient avoir diminué après la réservation");

        } catch (HttpClientErrorException.BadRequest e) {
            fail("Exception lors de la réservation des sièges : " + e.getMessage());
        }
    }

    //    @Test
//    void testNotEnoughSeatsAvailable() {
//        // Trying to reserve more seats than available
//        try {
//            eventServiceClient.updateSeatsAvailable(EVENT_ID, 5);  // Attempt to reserve 100 seats
//
//            // If the reservation went through, the test should fail
//            fail("Expected exception for not enough available seats");
//        } catch (HttpClientErrorException.BadRequest e) {
//            // Assert that the exception is thrown due to insufficient seats
//            assertTrue(e.getMessage().contains("Not enough available seats"));
//        }
//    }
//
    private int getInitialSeatsAvailable() {
        // Fetch the initial number of seats available for the event
        return eventServiceClient.getAvailableSeats(EVENT_ID);
    }
}