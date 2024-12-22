package com.example.carpoolingapk1692022.models;

public class Ride {
    private int id;
    private int passengerId;
    private int driverId;
    private String fromLocation;
    private String toLocation;
    private double fromLat;
    private double fromLng;
    private double toLat;
    private double toLng;
    private double price;
    private String status; // PENDING, ACCEPTED, REJECTED, COMPLETED
    private long timestamp;

    public Ride() {
    }

    public Ride(int passengerId, int driverId, String fromLocation, String toLocation,
                double fromLat, double fromLng, double toLat, double toLng, double price) {
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.toLat = toLat;
        this.toLng = toLng;
        this.price = price;
        this.status = "PENDING";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public String getFromLocation() { return fromLocation; }
    public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }
    public String getToLocation() { return toLocation; }
    public void setToLocation(String toLocation) { this.toLocation = toLocation; }
    public double getFromLat() { return fromLat; }
    public void setFromLat(double fromLat) { this.fromLat = fromLat; }
    public double getFromLng() { return fromLng; }
    public void setFromLng(double fromLng) { this.fromLng = fromLng; }
    public double getToLat() { return toLat; }
    public void setToLat(double toLat) { this.toLat = toLat; }
    public double getToLng() { return toLng; }
    public void setToLng(double toLng) { this.toLng = toLng; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
} 