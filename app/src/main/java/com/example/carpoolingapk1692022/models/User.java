package com.example.carpoolingapk1692022.models;

public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private double rating;
    private int numberOfRatings;
    private boolean isDriver;

    public User() {
        this.rating = 0.0;
        this.numberOfRatings = 0;
    }

    public User(String name, String surname, String email, String password, boolean isDriver) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isDriver = isDriver;
        this.rating = 0.0;
        this.numberOfRatings = 0;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public double getRating() { return rating; }
    public int getNumberOfRatings() { return numberOfRatings; }
    public boolean isDriver() { return isDriver; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setDriver(boolean driver) { isDriver = driver; }

    // Методи за рејтинг
    public void addRating(double newRating) {
        this.rating = ((this.rating * this.numberOfRatings) + newRating) / (this.numberOfRatings + 1);
        this.numberOfRatings++;
    }

    public String getFullName() {
        return this.name + " " + this.surname;
    }
} 