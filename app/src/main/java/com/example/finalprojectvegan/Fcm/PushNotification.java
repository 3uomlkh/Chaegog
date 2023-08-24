package com.example.finalprojectvegan.Fcm;

import com.example.finalprojectvegan.Fcm.NotificationData;
import com.google.gson.annotations.SerializedName;

public class PushNotification {
    @SerializedName("notification")
    public NotificationData notificationData;

    @SerializedName("to")
    public String to;

    public PushNotification(NotificationData notificationData, String to) {
        this.notificationData = notificationData;
        this.to = to;
    }
}
