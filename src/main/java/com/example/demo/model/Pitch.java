package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pitch")
public class Pitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;


    @Column(name = "team_id")
    private int teamId;

    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private StockEntity stock;

    private String action;
    private int qty;
    private double price;

    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Pitch() {}

    public Pitch(int userId, int teamId, StockEntity stock, String action, int qty, double price) {
        this.userId = userId;
        this.teamId = teamId;
        this.stock = stock;
        this.action = action;
        this.qty = qty;
        this.price = price;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters and Setters (You can generate them with your IDE)
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTeamId() {
        return teamId;
    }

    public StockEntity getStock() {
        return stock;
    }

    public String getAction() {
        return action;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
