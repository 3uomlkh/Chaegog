package com.cchaegog.chaegog.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodIngreItem {
    @SerializedName("FoodIngredient")
    public List<FoodIngreData> FoodIngredient;
}
