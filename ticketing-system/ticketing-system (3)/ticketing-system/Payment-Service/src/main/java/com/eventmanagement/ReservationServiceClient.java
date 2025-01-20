package com.eventmanagement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "Reservation-Service") // Nom du service dans le registre (Eureka/Consul)
public interface ReservationServiceClient {

    @GetMapping("/reservation/{id}")
    Reservation getReservationById(@PathVariable("id") Long id);

    @PutMapping("/reservation/{id}/validate")
    void validateReservation(@PathVariable("id") Long id);
}