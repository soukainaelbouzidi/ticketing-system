package com.eventmanagement;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event save(Event event) {
        return repository.save(event);
    }

    public List<Event> findAll() {
        return repository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Méthode pour vérifier la disponibilité d'un événement
    public Boolean checkAvailability(Long eventId) {
        Optional<Event> event = repository.findById(eventId);
        if (event.isPresent()) {
            // Si le nombre de places disponibles est supérieur à 0, l'événement est disponible
            return event.get().getSeatsAvailable() > 0;
        } else {
            throw new RuntimeException("Event not found");
        }
    }
    // Méthode pour mettre à jour les places disponibles
    public void updateSeatsAvailable(Long eventId, int seatsReserved) {
        // Trouver l'événement par ID
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Réduire les places disponibles
        int newSeatsAvailable = event.getSeatsAvailable() - seatsReserved;

        // Vérifier que le nombre de places disponibles est suffisant
        if (newSeatsAvailable < 0) {
            throw new RuntimeException("Not enough available seats.");
        }

        // Mettre à jour l'événement
        event.setSeatsAvailable(newSeatsAvailable);
        repository.save(event);
    }

    // Delete event by ID
    public void deleteById(long id) {
        // Check if the event exists before attempting to delete
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Event with ID " + id + " not found.");
        }
    }
    public int getAvailableSeats(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return event.getSeatsAvailable();
    }
}