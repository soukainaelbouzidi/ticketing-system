package com.eventmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private Long userId;
    private int seatsReserved;
    private LocalDateTime reservationDate;
    private String status;

    // Constructeur par défaut
    public Reservation() {}

    // Constructeur sans ID (utilisé lors de la création)
    public Reservation(Long eventId, Long userId, int seatsReserved, LocalDateTime reservationDate, String status) {
        this.eventId = eventId;
        this.userId = userId;
        this.seatsReserved = seatsReserved;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    // Constructeur complet avec ID (utilisé pour les tests ou mises à jour)
    public Reservation(Long id, Long eventId, Long userId, int seatsReserved, LocalDateTime reservationDate, String status) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.seatsReserved = seatsReserved;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(int seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
