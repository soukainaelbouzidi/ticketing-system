package com.eventmanagement;

public class UserResponse {
    private String username;
    private String email;

    // Constructeurs, getters et setters
    public UserResponse() {}

    public UserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
