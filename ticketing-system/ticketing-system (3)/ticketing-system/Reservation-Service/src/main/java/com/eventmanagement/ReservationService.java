package com.eventmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final EventServiceClient eventServiceClient;

    @Autowired
    public ReservationService(ReservationRepository repository, EventServiceClient eventServiceClient) {
        this.repository = repository;
        this.eventServiceClient = eventServiceClient;
    }

    // Méthode pour enregistrer la réservation
    public Reservation save(Reservation reservation, String token) {
        // Enregistrer directement la réservation sans vérifier la disponibilité
        Reservation savedReservation = repository.save(reservation);

        // Mettre à jour les places disponibles de l'événement
        updateSeatsAvailable(reservation.getEventId(), reservation.getSeatsReserved());

        return savedReservation;
    }

    // Méthode pour mettre à jour le nombre de places disponibles
    private void updateSeatsAvailable(Long eventId, int seatsReserved) {
        // Appeler votre EventServiceClient pour mettre à jour les places disponibles de l'événement
        // Cette méthode doit envoyer une requête PUT ou PATCH à votre microservice d'événements
        eventServiceClient.updateSeatsAvailable(eventId, seatsReserved);
    }

    // Méthode pour obtenir toutes les réservations
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    // Méthode pour obtenir une réservation par ID
    public Optional<Reservation> findById(Long id) {
        return repository.findById(id);
    }

    // Méthode pour supprimer une réservation par ID
    public void delete(Long id) {
        // Récupérer la réservation à supprimer
        Optional<Reservation> optionalReservation = repository.findById(id);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // Mettre à jour les places disponibles pour l'événement associé
            updateSeatsAvailable(reservation.getEventId(), -reservation.getSeatsReserved());

            // Supprimer la réservation
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }

    // Méthode pour obtenir les réservations d'un utilisateur par ID
    public List<Reservation> findReservationsByUserId(Long userId) {
        return repository.findByUserId(userId);  // Supposons que le repository a une méthode pour cela
    }

    // Modification du statut d'une réservation
    public Reservation updateReservationStatus(Long id, String status) {
        Optional<Reservation> optionalReservation = repository.findById(id);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // Conserver les champs existants, ne mettre à jour que le statut
            Long eventId = reservation.getEventId();
            Long userId = reservation.getUserId();
            int seatsReserved = reservation.getSeatsReserved();
            String reservationDate = reservation.getReservationDate().toString(); // On garde la date actuelle

            // Mise à jour uniquement du statut
            reservation.setStatus(status);

            // Sauvegarder en utilisant les mêmes valeurs sauf le statut
            reservation.setEventId(eventId);
            reservation.setUserId(userId);
            reservation.setSeatsReserved(seatsReserved);
            reservation.setReservationDate(LocalDateTime.parse(reservationDate));

            return repository.save(reservation);
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }

    // Méthode pour supprimer toutes les réservations
    public void deleteAllReservations() {
        // Récupérer toutes les réservations
        List<Reservation> reservations = repository.findAll();

        // Parcourir chaque réservation et mettre à jour les places disponibles pour chaque événement
        for (Reservation reservation : reservations) {
            // Mettre à jour les places disponibles en annulant la réservation
            updateSeatsAvailable(reservation.getEventId(), -reservation.getSeatsReserved());
        }

        // Supprimer toutes les réservations après avoir mis à jour les places
        repository.deleteAll();
    }
}
