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
    //서버 키와 보낼 형식을 헤더에 넣는다. (json)
    @Headers({"Authorization: key=" + SERVER_KEY
            , "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body PushNotification pushNotification);

}
