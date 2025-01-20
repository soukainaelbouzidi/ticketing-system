package com.eventmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EventServiceClient eventServiceClient;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReservation() {
        // Arrange
        Reservation reservation = new Reservation(1L, 2L, 3, LocalDateTime.now(), "CONFIRMED");
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.save(reservation, "dummy-token");

        // Assert
        assertEquals("CONFIRMED", result.getStatus());
        verify(eventServiceClient, times(1)).updateSeatsAvailable(1L, 3);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testFindById() {
        // Arrange
        Reservation reservation = new Reservation(1L, 2L, 3, LocalDateTime.now(), "CONFIRMED");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        Optional<Reservation> result = reservationService.findById(1L);

        // Assert
        assertEquals(2L, result.get().getUserId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteReservation() {
        // Arrange
        Reservation reservation = new Reservation(1L, 2L, 3, LocalDateTime.now(), "CONFIRMED");

        // Mock de la méthode findById pour simuler que la réservation existe
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        reservationService.delete(1L); // Appeler la méthode delete

        // Assert
        verify(reservationRepository, times(1)).deleteById(1L);  // Vérifier que la méthode deleteById a été appelée
        verify(reservationRepository, times(1)).findById(1L);    // Vérifier que findById a été appelé
        verify(eventServiceClient, times(1)).updateSeatsAvailable(reservation.getEventId(), -reservation.getSeatsReserved());  // Vérifier que les places ont été mises à jour
    }

}
