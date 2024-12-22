package com.example.carpoolingapk1692022.models;

public class Review {
    private int id;
    private String reviewText;
    private float rating;
    private long reviewDate;
    private String reviewerName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    
    public long getReviewDate() { return reviewDate; }
    public void setReviewDate(long reviewDate) { this.reviewDate = reviewDate; }
    
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
} 