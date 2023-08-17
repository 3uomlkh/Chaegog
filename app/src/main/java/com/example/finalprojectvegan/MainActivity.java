package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalprojectvegan.Fcm.NotificationAPI;
import com.example.finalprojectvegan.Fcm.NotificationData;
import com.example.finalprojectvegan.Fcm.PushNotification;
import com.example.finalprojectvegan.Fcm.RetrofitInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userToken, refreshToken;

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
        fragment_mypage = new FragMyFeed();
//        fragment_search = new UserSearchFragment();


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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseMessaging", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        refreshToken = task.getResult();
                        Log.d("MyToken", "My New Token : " + refreshToken);
                    }
                });

        db.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userToken = documentSnapshot.get("userToken").toString();
                        Log.d("MyToken", "My Old Token : " + userToken);
                        if(!userToken.equals(refreshToken)) {
                            myTokenUpdate();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
    public void myTokenUpdate() {
        DocumentReference reference = db.collection("users").document(firebaseUser.getUid());

        reference.update("userToken", refreshToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("myTokenUpdate", "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("myTokenUpdate", "Error : ", e);
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