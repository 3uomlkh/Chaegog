package com.example.finalprojectvegan.Fcm;

import static com.example.finalprojectvegan.Fcm.MyKey.BASE_URL;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static NotificationAPI api;

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    public static NotificationAPI getApi() {
//        api = retrofit.create(NotificationAPI.class);
//        return api;
//    }
}
