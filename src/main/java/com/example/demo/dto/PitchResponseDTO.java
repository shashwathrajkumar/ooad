package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Pitch;

public class PitchResponseDTO {
    private String username;
    private String stockSymbol;
    private int quantity;
    private double price;
    private String action;
    private String status;
    private LocalDateTime createdAt;

    public PitchResponseDTO(Pitch pitch) {
        this.username = pitch.getUser().getUsername();
        this.stockSymbol = pitch.getStock().getSymbol(); // assuming getStock() gives Stock object
        this.quantity = pitch.getQty();
        this.price = pitch.getPrice();
        this.action = pitch.getAction();
        this.status = pitch.getStatus();
        this.createdAt = pitch.getCreatedAt();
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    // Getters and Setters
    public int getQuantity() {
        return quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }
}

