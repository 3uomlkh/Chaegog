package com.example.finalprojectvegan.Model;

import java.util.Date;

public class FeedInfo {
    private String title, content, publisher, postId, uri;
    private Long favorite;
    private Date createdAt;

    public FeedInfo(String title, String content, String publisher, String postId, String uri, Long favorite, Date createdAt) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.postId = postId;
        this.uri = uri;
        this.favorite = favorite;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getFavorite() {
        return favorite;
    }

    public void setFavorite(Long favorite) {
        this.favorite = favorite;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}