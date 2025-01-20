package com.eventmanagement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MICROSERVICE-EVENT")  // Correspond à l'application du microservice d'événements
public interface EventServiceClient {

    // Méthode pour vérifier la disponibilité d'un événement
    @GetMapping("/events/{id}/availability")
    Boolean checkAvailability(@PathVariable Long id);
    // Méthode pour mettre à jour les places disponibles d'un événement
    @PutMapping("/events/update-seats")
    void updateSeatsAvailable(@RequestParam("eventId") Long eventId, @RequestParam("seatsReserved") int seatsReserved);


    // Nouvelle méthode pour récupérer les places disponibles
    @GetMapping("/events/{id}/seats")
    int getAvailableSeats(@PathVariable Long id);
}