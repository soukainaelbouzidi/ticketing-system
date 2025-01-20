package com.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)  // Assurez-vous d'importer la configuration de sécurité

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test pour la création d'une réservation
    @Test
    @WithMockUser(username = "wiam")
    public void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation(2L, 3L, 5, LocalDateTime.now(), "CONFIRMED");
        Reservation savedReservation = new Reservation(1L, 2L, 3L, 5, LocalDateTime.now(), "CONFIRMED");

        when(reservationService.save(any(Reservation.class), eq("Bearer mock-token"))).thenReturn(savedReservation);

        mockMvc.perform(post("/reservation")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.eventId").value(2L))
                .andExpect(jsonPath("$.userId").value(3L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // Test pour la mise à jour du statut d'une réservation
    @Test
    @WithMockUser(username = "wiam")
    public void testUpdateReservationStatus() throws Exception {
        Reservation updatedReservation = new Reservation(1L, 2L, 3L, 5, LocalDateTime.now(), "CANCELLED");

        when(reservationService.updateReservationStatus(1L, "CANCELLED")).thenReturn(updatedReservation);

        mockMvc.perform(put("/reservation/update-status/1")
                        .param("status", "CANCELLED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    // Test pour la suppression d'une réservation
    @Test
    @WithMockUser(username = "wiam")
    public void testDeleteReservation() throws Exception {
        doNothing().when(reservationService).delete(1L);

        mockMvc.perform(delete("/reservation/1"))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).delete(1L);
    }

    // Test pour récupérer toutes les réservations d'un utilisateur
    @Test
    @WithMockUser(username = "wiam")
    public void testGetReservationsByUserId() throws Exception {
        Reservation reservation = new Reservation(1L, 2L, 3L, 5, LocalDateTime.now(), "CONFIRMED");
        List<Reservation> reservations = List.of(reservation);

        when(reservationService.findReservationsByUserId(3L)).thenReturn(reservations);

        mockMvc.perform(get("/reservation/user/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].eventId").value(2L))
                .andExpect(jsonPath("$[0].userId").value(3L))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }
}
