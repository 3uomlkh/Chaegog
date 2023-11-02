package com.cchaegog.chaegog.Fcm;

import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MyKey {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({BASE_URL, SERVER_KEY, CONTENT_TYPE})
    public @interface types{}

    public final static String BASE_URL = "https://fcm.googleapis.com/";
    public final static String SERVER_KEY = "AAAAEI8kC-Y:APA91bGKe1NplX0zdUabkDacezxYAGXQC2tMlqTkaflymEuMdujNZcpCgM6sBunkprkcS_LNNgdlZYboDFcf_C0OyKC_pTC_LzAlVoU8SkSRNi7nP3YB4NfZmZUgRKXyaagCFw1-A6tl";
    public final static String CONTENT_TYPE = "application/json";
}
