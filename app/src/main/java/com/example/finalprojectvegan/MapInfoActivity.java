package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.finalprojectvegan.Adapter.ReviewAdapter;
import com.example.finalprojectvegan.Model.MapData;
import com.example.finalprojectvegan.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MapInfoActivity extends AppCompatActivity {
    private Fragment fragment_info, fragment_menu, fragment_photo, fragment_review;
    private TabLayout tabs;
    String name, addr, time, dayoff, category, menu, image, key, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_info);

        fragment_info = new MapTabInfo();
        fragment_menu = new MapTabMenu();
        fragment_photo = new MapTabPhoto();
        fragment_review = new MapTabReview();

        tabs = findViewById(R.id.map_info_tabs);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_info).commitAllowingStateLoss();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                    if (position == 0)
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_info).commitAllowingStateLoss();
                    else if (position == 1)
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_menu).commitAllowingStateLoss();
                    else if (position == 2)
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_photo).commitAllowingStateLoss();
                    else if (position == 3)
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_review).commitAllowingStateLoss();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        addr = intent.getStringExtra("addr");
        time = intent.getStringExtra("time");
        dayoff = intent.getStringExtra("dayOff");
        image = intent.getStringExtra("image");
        category = intent.getStringExtra("category");
        menu = intent.getStringExtra("menu");
        phone = intent.getStringExtra("phone");
        key = intent.getStringExtra("key");

        // Bundle에넣어 fragment로 보내기
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("image", image);
        bundle.putString("addr", addr);
        bundle.putString("time", time);
        bundle.putString("category", category);
        bundle.putString("menu", menu);
        bundle.putString("phone", phone);
        bundle.putString("key", key);
        fragment_info.setArguments(bundle);
        fragment_menu.setArguments(bundle);
        fragment_review.setArguments(bundle);
        fragment_photo.setArguments(bundle);

    }
}