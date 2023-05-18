package com.example.finalprojectvegan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalprojectvegan.Model.MapData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MapInfoActivity extends AppCompatActivity {
    private Fragment fragment_info, fragment_menu, fragment_photo, fragment_review, fragment_map_bookmark;
    private TabLayout tabs;
    String name, addr, time, dayoff, category, menu, image;
    private ArrayList<MapData> mapListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_info);

        fragment_info = new MapTabInfo();
        fragment_menu = new MapTabMenu();
        fragment_photo = new MapTabPhoto();
        fragment_review = new MapTabReview();
        fragment_map_bookmark = new FragBookmark1();

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
        category = intent.getStringExtra("category");
        menu = intent.getStringExtra("menu");

        // Bundle에넣어 fragment로 보내기
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("image", image);
        bundle.putString("addr", addr);
        bundle.putString("time", time);
        bundle.putString("dayOff", dayoff);
        bundle.putString("category", category);
        bundle.putString("menu", menu);
        fragment_info.setArguments(bundle);
        fragment_menu.setArguments(bundle);
        fragment_review.setArguments(bundle);
        fragment_photo.setArguments(bundle);
        fragment_map_bookmark.setArguments(bundle);

    }
}