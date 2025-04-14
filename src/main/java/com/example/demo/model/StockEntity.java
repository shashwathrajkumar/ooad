package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String symbol;
    private String name;

    private double price; // Add this field

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Setter for price (optional, if needed)
    public void setPrice(double price) {
        this.price = price;
    }

    public StockEntity() {}

    public StockEntity(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public Stock toStock() {
        Stock stock = new Stock();
        stock.setSymbol(this.symbol);
        stock.setPrice(this.price);
        return stock;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }
}

