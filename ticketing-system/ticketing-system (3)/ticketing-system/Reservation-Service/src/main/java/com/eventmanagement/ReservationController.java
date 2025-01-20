package com.eventmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reservation")  // Route de base pour les réservations
public class ReservationController {

    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    // Endpoint pour créer une nouvelle réservation
    @PostMapping
    public Reservation createReservation(
            @RequestHeader("Authorization") String token,
            @RequestBody Reservation reservation) {
        // Vérification de la disponibilité et validation de l'utilisateur
        return service.save(reservation, token);
    }

    // Endpoint pour obtenir toutes les réservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return service.findAll();
    }

    // Endpoint pour obtenir une réservation par ID
    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    // Endpoint pour supprimer une réservation par ID
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        service.delete(id);
    }

    // Endpoint pour obtenir les réservations d'un utilisateur par ID
    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUserId(@PathVariable Long userId) {
        return service.findReservationsByUserId(userId);  // Appel à la méthode de service
    }


    // Endpoint pour modifier le statut de la réservation
    @PutMapping("/update-status/{id}")
    public Reservation updateReservationStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        // Mettre à jour le statut de la réservation
        return service.updateReservationStatus(id, status);
    }

    // Endpoint pour supprimer toutes les réservations
    @DeleteMapping("/all")
    public void deleteAllReservations() {
        service.deleteAllReservations(); // Appel au service pour supprimer toutes les réservations
    }

}