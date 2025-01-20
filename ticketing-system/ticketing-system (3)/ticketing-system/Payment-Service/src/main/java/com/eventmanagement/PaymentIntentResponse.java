package com.eventmanagement;

public class PaymentIntentResponse {

    private String clientSecret;

    // Constructeur
    public PaymentIntentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    // Getter
    public String getClientSecret() {
        return clientSecret;
    }

    // Setter
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
