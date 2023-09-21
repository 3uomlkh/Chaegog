package com.example.finalprojectvegan.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedInfo {
    private String title, content, publisher, postId, uri, reportMail;
    private Date createdAt;
    private ArrayList<String> favoriteIdList;
    public int favoriteCount, reportCount;
    public Map<String, Boolean> favorites, report = new HashMap<>();

    public FeedInfo(String title, String content, String publisher, String postId, String uri, Date createdAt, int favoriteCount, Map<String, Boolean> favorites, int reportCount, Map<String, Boolean> report, String reportMail) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.postId = postId;
        this.uri = uri;
        this.createdAt = createdAt;
        this.favoriteCount = favoriteCount;
        this.favorites = favorites;
        this.reportCount = reportCount;
        this.report = report;
        this.reportMail = reportMail;
    }

    public FeedInfo() {

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Map<String, Boolean> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public Map<String, Boolean> getReport() {
        return report;
    }

    public void setReport(Map<String, Boolean> report) {
        this.report = report;
    }

    public String getReportMail() {
        return reportMail;
    }

    public void setReportMail(String reportMail) {
        this.reportMail = reportMail;
    }
}