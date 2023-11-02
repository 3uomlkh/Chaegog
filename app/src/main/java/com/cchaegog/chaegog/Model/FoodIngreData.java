package com.cchaegog.chaegog.Model;

import com.google.gson.annotations.SerializedName;

public class FoodIngreData {
    @SerializedName("FoodNum") // php 파일과 이름 같게
    private String FoodNum;
    @SerializedName("FoodGroup")
    private String FoodGroup;
    @SerializedName("FoodName")
    private String FoodName;

    public String getFoodGroup() {
        return FoodGroup;
    }

    public String getFoodName() {
        return FoodName;
    }
}
