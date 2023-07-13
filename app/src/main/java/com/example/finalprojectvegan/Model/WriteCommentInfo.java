package com.example.finalprojectvegan.Model;

import java.util.Date;

public class WriteCommentInfo {

    private String comment;
    private String publisher;
    private String feedId;
    private Date createdAt;

    public WriteCommentInfo(String comment, String publisher, String feedId, Date createdAt) {
        this.comment = comment;
        this.publisher = publisher;
        this.feedId = feedId;
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

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
