package com.example.finalprojectvegan.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.R;

public class PhotoActivity extends AppCompatActivity {
    ImageView imageView;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = findViewById(R.id.map_tab_photo_view);

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");

        Glide.with(this).load(imgUrl).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}