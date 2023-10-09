package com.cchaegog.chaegog;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, "3f92264bac3809194a24cb15484ee0e4");
    }
}
