package com.cchaegog.chaegog.Model;

import java.util.Date;

public class WritePostInfo {

    private String title;
    private String contents;
    private String publisher;
    private String imagePath;
    private Date createdAt;

    public WritePostInfo(String title, String contents, String publisher, String imagePath, Date createdAt) {
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }

    public void setContents(String contents) { this.contents = contents; }

    public String getPublisher() { return publisher; }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
