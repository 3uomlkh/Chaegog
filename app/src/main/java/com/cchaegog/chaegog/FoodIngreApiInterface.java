package com.cchaegog.chaegog;

import com.cchaegog.chaegog.Model.FoodIngreItem;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodIngreApiInterface {
    @GET("VeganFoodIngredient.php") // Base URL 이후 상세주소
    Call<FoodIngreItem> getFoodIngredientData();
}
