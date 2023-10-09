package com.cchaegog.chaegog.Fcm;

import static com.cchaegog.chaegog.Fcm.MyKey.CONTENT_TYPE;
import static com.cchaegog.chaegog.Fcm.MyKey.SERVER_KEY;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {
    @Headers({"Authorization: key=" + SERVER_KEY, "Content-Type:" + CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body PushNotification pushNotification);
}
