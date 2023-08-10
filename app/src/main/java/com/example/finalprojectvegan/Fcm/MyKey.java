package com.example.finalprojectvegan.Fcm;

import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MyKey {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({BASE_URL, SERVER_KEY, CONTENT_TYPE})
    public @interface types{}

    public final static String BASE_URL = "https://fcm.googleapis.com/";
    public final static String SERVER_KEY = "AAAASO3yth0:APA91bGtvLyfHqh9Vy-jY9Z7Jsr-uH9QGP_2rT4tcXFD-LjyNlgzGTUIdGCN_wryGek8jruFNEJn4q6zR4vRHkEYo3nlTE70O5UcF-khzz6IUc_ciILYUvIagYRLmQYljhIfNoYSdEzj";
    public final static String CONTENT_TYPE = "application/json";
}
