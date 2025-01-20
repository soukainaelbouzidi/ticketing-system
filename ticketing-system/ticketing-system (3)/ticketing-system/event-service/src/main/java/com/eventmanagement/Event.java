package com.eventmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data // Lombok generates getters, setters, toString, etc.
@NoArgsConstructor // Lombok generates the no-args constructor
@AllArgsConstructor // Lombok generates the all-args constructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime date;
    private String location;
    private int seatsAvailable;  // Number of available seats
    private double price;

    // Custom constructor (no need for this if you use Lombok, but included for clarity)
    public Event(String name, String description, LocalDateTime date, String location, int seatsAvailable, double price) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
    }

    // Method to get the number of available seats
    public int getAvailableSeats() {
        return seatsAvailable;  // Simply returns the value of seatsAvailable
    }
}