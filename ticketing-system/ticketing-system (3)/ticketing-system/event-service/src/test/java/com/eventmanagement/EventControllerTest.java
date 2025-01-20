package com.eventmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)  // Test only EventController
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register JavaTimeModule for handling LocalDateTime
    }

    @Test
    public void testCreateEvent() throws Exception {
        // Create an Event object using the correct constructor
        Event event = new Event("Concert", "Concert Description", LocalDateTime.now(), "Paris", 100, 50.0);

        // Mock the save method to return the same event
        when(eventService.save(any(Event.class))).thenReturn(event);

        // Perform the POST request to create the event
        mockMvc.perform(post("/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(event)))  // Convert the event object to JSON
                .andExpect(status().isCreated())  // Verify the HTTP status is 201 (Created)
                .andExpect(jsonPath("$.name").value("Concert"))  // Verify the event name is "Concert"
                .andExpect(jsonPath("$.location").value("Paris"));  // Verify the location is "Paris"
    }





    @Test
    public void testGetAllEvents() throws Exception {
        Event event1 = new Event(1L, "Concert", "A great concert", LocalDateTime.now(), "Paris", 100, 50.0);
        Event event2 = new Event(2L, "Festival", "A music festival", LocalDateTime.now(), "London", 200, 75.0);

        when(eventService.findAll()).thenReturn(Arrays.asList(event1, event2));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Concert"))
                .andExpect(jsonPath("$[1].name").value("Festival"));
    }

    @Test
    public void testGetEventById() throws Exception {
        Event event = new Event(1L, "Concert", "A great concert", LocalDateTime.now(), "Paris", 100, 50.0);

        when(eventService.findById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Concert"))
                .andExpect(jsonPath("$.location").value("Paris"));
    }

    @Test
    public void testCheckAvailability() throws Exception {
        Event event = new Event(1L, "Concert", "A great concert", LocalDateTime.now(), "Paris", 100, 50.0);

        when(eventService.findById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(get("/events/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));  // Assuming availability is true as seats are available
    }

    @Test
    public void testDeleteEvent() throws Exception {
        // Create an event object
        Event event = new Event(1L, "Concert", "Concert Description", LocalDateTime.now(), "Paris", 100, 50.0);

        // Mock the findById to return the event
        when(eventService.findById(1L)).thenReturn(Optional.of(event));

        // Mock the delete method
        doNothing().when(eventService).deleteById(1L);

        // Perform the DELETE request
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());  // Verify HTTP status 204 (No Content)


    }




    @Test
    public void testUpdateEvent() throws Exception {
        Event event = new Event(1L, "Concert", "A great concert", LocalDateTime.now(), "Paris", 100, 50.0);
        Event updatedEvent = new Event(1L, "Updated Concert", "An updated great concert", LocalDateTime.now(), "Paris", 100, 60.0);

        when(eventService.findById(1L)).thenReturn(Optional.of(event));
        when(eventService.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/events/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Concert"))
                .andExpect(jsonPath("$.price").value(60.0));
    }

    @Test
    public void testUpdateSeats() throws Exception {
        mockMvc.perform(put("/events/update-seats")
                        .param("eventId", "1")
                        .param("seatsReserved", "10"))
                .andExpect(status().isOk());
    }
}