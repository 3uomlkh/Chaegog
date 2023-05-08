package com.example.finalprojectvegan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    private Intent intent;
    TextView getRecipeName, getRecipeType, getRecipeWay, getRecipeIngre,
            getRecipeManual01, getRecipeManual02, getRecipeManual03, getRecipeManual04,
            getRecipeManual05, getRecipeManual06;
    ImageView getRecipeImage, getRecipeManualImg01, getRecipeManualImg02, getRecipeManualImg03,
            getRecipeManualImg04, getRecipeManualImg05, getRecipeManualImg06;
    String name, image, type, way, ingre, manual01, manual_image01, manual02, manual_image02,
            manual03, manual_image03, manual04, manual_image04, manual05, manual_image05, manual06, manual_image06;
    String ingredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        getRecipeName = findViewById(R.id.recipe_detail_name);
        getRecipeImage = findViewById(R.id.recipe_detail_image);
        getRecipeType = findViewById(R.id.recipe_detail_type);
        getRecipeWay = findViewById(R.id.recipe_detail_way);
        getRecipeIngre = findViewById(R.id.recipe_ingredient);
        getRecipeManualImg01 = findViewById(R.id.recipe_manual_image01);
        getRecipeManual01 = findViewById(R.id.recipe_manual01);
        getRecipeManualImg02 = findViewById(R.id.recipe_manual_image02);
        getRecipeManual02 = findViewById(R.id.recipe_manual02);
        getRecipeManualImg03 = findViewById(R.id.recipe_manual_image03);
        getRecipeManual03 = findViewById(R.id.recipe_manual03);
        getRecipeManualImg04 = findViewById(R.id.recipe_manual_image04);
        getRecipeManual04 = findViewById(R.id.recipe_manual04);
        getRecipeManualImg05 = findViewById(R.id.recipe_manual_image05);
        getRecipeManual05 = findViewById(R.id.recipe_manual05);
        getRecipeManualImg06 = findViewById(R.id.recipe_manual_image06);
        getRecipeManual06 = findViewById(R.id.recipe_manual06);

    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        //레시피 이름, 메인 이미지,타입, 방법,재료 받아오기
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        type = intent.getStringExtra("type");
        way = intent.getStringExtra("way");
        ingre = intent.getStringExtra("ingre");

        String ingreArr[] = ingre.split(",");
        for(int i=0; i < ingreArr.length; i++){
            if(ingredient != null){
                ingredient = ingredient + "\n" + ingreArr[i];
            }else{
                ingredient = ingreArr[i];
            }
        }

        // 레시피 이미지, 설명 받아오기
        manual_image01 = intent.getStringExtra("m_Image01");
        manual01 = intent.getStringExtra("manual01");
        manual_image02 = intent.getStringExtra("m_Image02");
        manual02 = intent.getStringExtra("manual02");
        manual_image03 = intent.getStringExtra("m_Image03");
        manual03 = intent.getStringExtra("manual03");
        manual_image04 = intent.getStringExtra("m_Image04");
        manual04 = intent.getStringExtra("manual04");
        manual_image05 = intent.getStringExtra("m_Image05");
        manual05 = intent.getStringExtra("manual05");
        manual_image06 = intent.getStringExtra("m_Image06");
        manual06 = intent.getStringExtra("manual06");

        // 레시피 상세정보창 메인 이미지
        Glide.with(this)
                .load(image)

                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(getRecipeImage);

        // 레시피 과정
        getRecipeManualImg01.setVisibility(View.VISIBLE);
        getRecipeManual01.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(manual_image01)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(getRecipeManualImg01);
        getRecipeManual01.setText(manual01);

        getRecipeManualImg02.setVisibility(View.VISIBLE);
        getRecipeManual02.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(manual_image02)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(getRecipeManualImg02);
        getRecipeManual02.setText(manual02);

        getRecipeManualImg03.setVisibility(View.VISIBLE);
        getRecipeManual03.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(manual_image03)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(getRecipeManualImg03);
        getRecipeManual03.setText(manual03);

        if(manual_image06 != null && manual06 != null){
            getRecipeManualImg04.setVisibility(View.VISIBLE);
            getRecipeManual04.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image04)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg04);
            getRecipeManual04.setText(manual04);

            getRecipeManualImg05.setVisibility(View.VISIBLE);
            getRecipeManual05.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image05)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg05);
            getRecipeManual05.setText(manual05);

            getRecipeManualImg06.setVisibility(View.VISIBLE);
            getRecipeManual06.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image06)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg06);
            getRecipeManual06.setText(manual06);
        } else if(manual_image05 != null && manual05 != null && manual06 == null){
            getRecipeManualImg04.setVisibility(View.VISIBLE);
            getRecipeManual04.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image04)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg04);
            getRecipeManual04.setText(manual04);

            getRecipeManualImg05.setVisibility(View.VISIBLE);
            getRecipeManual05.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image05)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg05);
            getRecipeManual05.setText(manual05);
        } else if(manual_image04 != null && manual04 != null && manual05 == null){
            getRecipeManualImg04.setVisibility(View.VISIBLE);
            getRecipeManual04.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(manual_image04)
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(getRecipeManualImg04);
            getRecipeManual04.setText(manual04);
        }

        getRecipeName.setText(name);
        getRecipeType.setText(type);
        getRecipeWay.setText(way);
        getRecipeIngre.setText(ingredient);

    }
}