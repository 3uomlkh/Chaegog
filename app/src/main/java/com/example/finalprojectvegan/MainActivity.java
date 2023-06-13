package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton Btn_addFeed;

    // 객체 선언
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    Fragment fragment_homefeed;
    Fragment fragment_recipe;
    Fragment fragment_product;
    Fragment fragment_mypage;
    Fragment fragment_search;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn_addFeed = (FloatingActionButton) findViewById(R.id.Btn_addFeed);
        Btn_addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WritePostActivity.class);
                startActivity(intent);
            }
        });

        // BoredDeveloper 보고 작성한 내용
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, RegisterStep1Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null) {
                            if (documentSnapshot.exists()) {
                                Log.d("tag", "DocumentSnapshot data : " + documentSnapshot.getData());
                            } else {
                                Log.d("tag", "No such document");
                            }
                        }
                    } else {
                        Log.d("tag", "get failed with", task.getException());
                    }
                }
            });
//            for (UserInfo profile : firebaseUser.getProviderData()) {
//                String name = profile.getDisplayName();
//                if (neame == null) {
//                    Intent intent = new Intent(MainActivity.this, RegisterStep1Activity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
//            }
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment_homefeed = new FragHomeFeed();
        fragment_recipe = new FragHomeRecipe();
        fragment_product = new FragHomeProduct();
        fragment_mypage = new FragMypage();
        fragment_search = new UserSearchFragment();


        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_homefeed).commitAllowingStateLoss();

        // 상단 탭 레이아웃
        TabLayout tabLayout = findViewById(R.id.layout_tab);
        tabLayout.addTab(tabLayout.newTab().setText("홈"));
        tabLayout.addTab(tabLayout.newTab().setText("레시피"));
        tabLayout.addTab(tabLayout.newTab().setText("제품"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    selected = fragment_homefeed;
                } else if(position == 1){
                    selected = fragment_recipe;
                } else {
                    selected = fragment_product;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 리스너 등록
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.homefeed:
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_homefeed).commitAllowingStateLoss();
                        return true;
                    case R.id.map:
                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.ocr:
                        Intent ocrIntent = new Intent(MainActivity.this, OcrActivity.class);
                        startActivity(ocrIntent);
                        return true;
                    case R.id.bookmark:
                        Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
                        startActivity(bookmarkIntent);
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_mypage).commitAllowingStateLoss();
//                        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                        editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        editor.apply();
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
//                Toast.makeText(getApplicationContext(), "검색창 클릭됨", Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment_search).commitAllowingStateLoss();

                Intent intent1 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(), "마이페이지 클릭됨", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

}