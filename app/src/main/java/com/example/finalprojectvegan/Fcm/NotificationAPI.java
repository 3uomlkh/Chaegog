package com.example.finalprojectvegan.Fcm;

import static com.example.finalprojectvegan.Fcm.MyKey.CONTENT_TYPE;
import static com.example.finalprojectvegan.Fcm.MyKey.SERVER_KEY;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {
    @Headers({"Authorization: key=" + SERVER_KEY, "Content-Type:" + CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body PushNotification pushNotification);
}
