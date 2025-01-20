package com.eventmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }



    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = service.save(event);  // Save the event
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);  // Return the created event with 201 status
    }


    // Endpoint pour obtenir tous les événements
    @GetMapping
    public List<Event> getAllEvents() {
        return service.findAll();
    }

    // Endpoint pour obtenir un événement par ID
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();  // Returning 204 status without body
    }


    // Endpoint pour mettre à jour un événement
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return service.findById(id).map(event -> {
            // Mise à jour des champs existants de l'événement
            event.setName(updatedEvent.getName());
            event.setDate(updatedEvent.getDate());
            event.setLocation(updatedEvent.getLocation());
            event.setSeatsAvailable(updatedEvent.getSeatsAvailable());
            event.setDescription(updatedEvent.getDescription());
            return service.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    // Endpoint pour vérifier la disponibilité d'un événement
    @GetMapping("/{id}/availability")
    public Boolean checkAvailability(@PathVariable Long id) {
        Event event = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return event.getSeatsAvailable() > 0;
    }

    // Endpoint pour mettre à jour les places disponibles d'un événement
    @PutMapping("/update-seats")
    public void updateSeats(@RequestParam("eventId") Long eventId, @RequestParam("seatsReserved") int seatsReserved) {
        service.updateSeatsAvailable(eventId, seatsReserved);
    }
    // Endpoint pour récupérer les places disponibles d'un événement
    @GetMapping("/{id}/seats")
    public int getAvailableSeats(@PathVariable Long id) {
        return service.getAvailableSeats(id);
    }
}