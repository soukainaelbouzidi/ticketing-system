package com.eventmanagement;

import java.time.LocalDateTime;

// DTO représentant un événement
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private int seatsAvailable;
    private double price;

    // Constructeurs, getters et setters

    public EventDTO() {}

    public EventDTO(Long id, String name, String description, LocalDateTime dateTime, String location, int seatsAvailable, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

