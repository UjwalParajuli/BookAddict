package com.example.bookaddict.models;

import java.io.Serializable;

public class RatingModel implements Serializable {
    private String user_name, review_date, review_text;
    private float rating;

    public RatingModel(String user_name, String review_date, String review_text, float rating) {
        this.user_name = user_name;
        this.review_date = review_date;
        this.review_text = review_text;
        this.rating = rating;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
