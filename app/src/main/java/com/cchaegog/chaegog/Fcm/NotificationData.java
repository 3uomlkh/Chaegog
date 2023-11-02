package com.cchaegog.chaegog.Fcm;

public class NotificationData {
    public String title;
    public String body;
    public String click_action;
    public NotificationData(String title, String body) {
        this.title = title;
        this.body = body;
    }
    public NotificationData(String title, String body, String click_action) {
        this.title = title;
        this.body = body;
        this.click_action = click_action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
