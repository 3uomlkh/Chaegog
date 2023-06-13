package com.example.finalprojectvegan.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class WriteReviewInfo {

    private String rating;
    private String review;
    private String name;
    private String publisher;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String reviewId;
    private Date createdAt;
    private ArrayList<Uri> images;

    public WriteReviewInfo(String reviewId, String rating, String name, String review, String publisher, String imagePath1, Date createdAt) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.name = name;
        this.review = review;
        this.publisher = publisher;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.createdAt = createdAt;
    }

    public WriteReviewInfo(String rating, String name, String review, String publisher, ArrayList<Uri> images, Date createdAt) {
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.publisher = publisher;
        this.images = images;
        this.createdAt = createdAt;
    }

    public WriteReviewInfo(String reviewId, String rating, String name, String review, String publisher, Date createdAt) {
        this.reviewId = reviewId;
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }

    public String getReviewId() {
        return reviewId;
    }
}
