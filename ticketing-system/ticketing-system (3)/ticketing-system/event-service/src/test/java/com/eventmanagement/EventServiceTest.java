package com.eventmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository; // Mock du repository

    @InjectMocks
    private EventService eventService; // Service à tester

    private Event event;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks

        // Créer un événement de test
        event = new Event(1L, "Concert", "Music concert", LocalDateTime.now(), "Paris", 100, 50.0);
    }

    // Test de la création d'un événement
    @Test
    public void testSaveEvent() {
        // Configurer le comportement du mock repository
        when(eventRepository.save(event)).thenReturn(event);

        // Appeler la méthode du service
        Event savedEvent = eventService.save(event);

        // Vérifier que la méthode repository a été appelée
        verify(eventRepository, times(1)).save(event);

        // Vérifier que l'événement retourné est correct
        assertNotNull(savedEvent);
        assertEquals("Concert", savedEvent.getName());
        assertEquals("Music concert", savedEvent.getDescription());
    }

    // Test de la récupération d'un événement par ID
    @Test
    public void testFindEventById() {
        // Configurer le comportement du mock repository
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Appeler la méthode du service
        Optional<Event> foundEvent = eventService.findById(1L);

        // Vérifier que la méthode repository a été appelée
        verify(eventRepository, times(1)).findById(1L);

        // Vérifier que l'événement retourné est correct
        assertTrue(foundEvent.isPresent());
        assertEquals("Concert", foundEvent.get().getName());
    }

    // Test du cas où l'événement n'existe pas
    @Test
    public void testFindEventByIdNotFound() {
        // Configurer le comportement du mock repository
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // Appeler la méthode du service
        Optional<Event> foundEvent = eventService.findById(999L);

        // Vérifier que l'événement n'a pas été trouvé
        assertFalse(foundEvent.isPresent());
    }

    // Test de la vérification de la disponibilité des places
    @Test
    public void testCheckAvailability() {
        // Configurer le comportement du mock repository
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Appeler la méthode du service
        Boolean isAvailable = eventService.checkAvailability(1L);

        // Vérifier que la méthode repository a été appelée
        verify(eventRepository, times(1)).findById(1L);

        // Vérifier que l'événement est disponible
        assertTrue(isAvailable);
    }

    // Test du cas où l'événement n'a pas de places disponibles
    @Test
    public void testCheckAvailabilityNoSeats() {
        // Modifier l'événement pour qu'il n'ait plus de places disponibles
        event.setSeatsAvailable(0);

        // Configurer le comportement du mock repository
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Appeler la méthode du service
        Boolean isAvailable = eventService.checkAvailability(1L);

        // Vérifier que l'événement n'est pas disponible
        assertFalse(isAvailable);
    }

    // Test de la mise à jour des places disponibles
    @Test
    public void testUpdateSeatsAvailable() {
        // Configurer le comportement du mock repository
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        // Appeler la méthode du service pour réserver 10 places
        eventService.updateSeatsAvailable(1L, 10);

        // Vérifier que le nombre de places a été mis à jour
        assertEquals(90, event.getSeatsAvailable()); // 100 - 10 = 90

        // Vérifier que la méthode repository a été appelée pour sauver l'événement mis à jour
        verify(eventRepository, times(1)).save(event);
    }

    // Test de la mise à jour des places avec trop de réservations
    @Test
    public void testUpdateSeatsAvailableNotEnoughSeats() {
        // Configurer le comportement du mock repository
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Vérifier que l'exception est lancée lorsque les places réservées sont plus nombreuses que les places disponibles
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> eventService.updateSeatsAvailable(1L, 200));

        assertEquals("Not enough available seats.", thrown.getMessage());
    }
}