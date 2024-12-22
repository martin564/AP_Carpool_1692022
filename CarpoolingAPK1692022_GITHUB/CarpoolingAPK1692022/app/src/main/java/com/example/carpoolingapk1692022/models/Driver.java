package com.example.carpoolingapk1692022.models;

public class Driver extends User {
    private String carModel;
    private String carPlate;
    private int carYear;
    private int seats;
    private boolean isActive;
    private double pricePerKm;
    private String activeTimeStart;
    private String activeTimeEnd;
    private double rating;
    private int numberOfRatings;

    public Driver() {
        super();
        setDriver(true);
        this.rating = 0.0;
        this.numberOfRatings = 0;
    }

    public Driver(String name, String surname, String email, String password,
                 String carModel, String carPlate, int carYear, int seats, double pricePerKm) {
        super(name, surname, email, password, true);
        this.carModel = carModel;
        this.carPlate = carPlate;
        this.carYear = carYear;
        this.seats = seats;
        this.pricePerKm = pricePerKm;
        this.isActive = false;
        this.rating = 0.0;
        this.numberOfRatings = 0;
    }

    // Getters
    public String getCarModel() { return carModel; }
    public String getCarPlate() { return carPlate; }
    public int getCarYear() { return carYear; }
    public int getSeats() { return seats; }
    public boolean isActive() { return isActive; }
    public double getPricePerKm() { return pricePerKm; }
    public String getActiveTimeStart() {
        return activeTimeStart;
    }

    public String getActiveTimeEnd() {
        return activeTimeEnd;
    }

    public double getRating() {
        return rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    // Setters
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public void setCarPlate(String carPlate) { this.carPlate = carPlate; }
    public void setCarYear(int carYear) { this.carYear = carYear; }
    public void setSeats(int seats) { this.seats = seats; }
    public void setActive(boolean active) { isActive = active; }
    public void setPricePerKm(double pricePerKm) { this.pricePerKm = pricePerKm; }
    public void setActiveTimeStart(String activeTimeStart) {
        this.activeTimeStart = activeTimeStart;
    }

    public void setActiveTimeEnd(String activeTimeEnd) {
        this.activeTimeEnd = activeTimeEnd;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    // Помошни методи
    public String getCarInfo() {
        return String.format("%s %d (%s)", carModel, carYear, carPlate);
    }

    public double calculatePrice(double distance) {
        return distance * pricePerKm;
    }
} 