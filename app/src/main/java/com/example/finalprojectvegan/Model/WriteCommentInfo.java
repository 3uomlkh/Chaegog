package com.example.finalprojectvegan.Model;

import java.util.Date;

public class WriteCommentInfo {

    private String comment;
    private String publisher;
    private Date createdAt;

    public WriteCommentInfo(String comment, String publisher, Date createdAt) {
        this.comment = comment;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
