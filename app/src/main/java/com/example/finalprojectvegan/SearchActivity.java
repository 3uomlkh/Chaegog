package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectvegan.Adapter.RecipeAdapter;
import com.example.finalprojectvegan.Adapter.ReviewAdapter;
import com.example.finalprojectvegan.Adapter.ReviewWithoutImageAdapter;
import com.example.finalprojectvegan.Adapter.UserAdapter;
import com.example.finalprojectvegan.Adapter.UserSearchAdapter;
import com.example.finalprojectvegan.Model.MapData;
import com.example.finalprojectvegan.Model.RecipeData;
import com.example.finalprojectvegan.Model.User;
import com.example.finalprojectvegan.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchBtn;
    private TabLayout tabs;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<String> recipeTitleArray, recipeUrlArray, recipeThumbArray;
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private ArrayList<String> MapName, MapAddr, MapTime, MapDayoff,
    MapCategory, MapImage, MapMenu, MapPhone;
    private ArrayList<String> UserName, UserProfile, UserEmail;
    private int TabPosition;
    String searchText;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    MapBookmarkAdapter mapAdapter;
    UserSearchAdapter userSearchAdapter;
    Fragment fragment_search_recipe;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.search_recipe_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        //fragment_search_recipe = new SearchRecipeFragment();

        tabs = findViewById(R.id.search_tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 0) {
                    searchEditText.setHint("계정");
                    TabPosition = 0;
                    userSearchAdapter = new UserSearchAdapter();
                } else if (position == 1) {
                    searchEditText.setHint("레시피");
                    TabPosition = 1;
                    recipeAdapter = new RecipeAdapter();
                } else if (position == 2) {
                    searchEditText.setHint("제품");
                    TabPosition = 2;
                } else if (position == 3) {
                    searchEditText.setHint("식당");
                    TabPosition = 3;
                    mapAdapter = new MapBookmarkAdapter();
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = searchEditText.getText().toString();
                if(searchText.equals("")) {
                    Toast.makeText(SearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    switch (TabPosition) {
                        case 0:
                            Toast.makeText(SearchActivity.this, "유저 검색", Toast.LENGTH_SHORT).show();
                            getUserSearchResult();
                            break;
                        case 1:
                            Toast.makeText(SearchActivity.this, "레시피 검색", Toast.LENGTH_SHORT).show();
                            getRecipeBookmark();
                            break;
                        case 2:
                            Toast.makeText(SearchActivity.this, "제품 검색", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(SearchActivity.this, "식당 검색", Toast.LENGTH_SHORT).show();
                            getMapBookmark();
                            break;
                    }
//                    Fragment selected = null;
//                    if(TabPosition == 0){
//
//                    } else if(TabPosition == 1){
//                        selected = fragment_search_recipe;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("searchText", searchText);
//                        fragment_search_recipe.setArguments(bundle);
//                    } else if(TabPosition == 2) {
//
//                    } else {
//
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.search_container, selected).commit();
                }
            }
        });
    }

    private void getUserSearchResult() {
        userSearchAdapter.removeItem();

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        UserName = new ArrayList<>();
                        //UserProfile = new ArrayList<>();
                        UserEmail = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("search_user", documentSnapshot.getId() + " => " + documentSnapshot.getData().values());
                                UserName.add(Objects.requireNonNull(documentSnapshot.getData().get("userID")).toString());
                                UserEmail.add(Objects.requireNonNull(documentSnapshot.getData().get("userEmail")).toString());
                            }
                            Log.d("user_name","name : " + UserName);

                            for(int i=0; i<UserName.size(); i++) {
                                if (UserName.get(i).contains(searchText)) {
                                    Log.d("user_name","search : " + UserName.get(i));
                                    User user = new User();
                                    user.setId(UserName.get(i));
                                    //user.setImageurl(UserProfile.get(i));
                                    user.setUserEmail(UserEmail.get(i));

                                    userSearchAdapter.addItem(user);
                                }
                            }
                            recyclerView.setAdapter(userSearchAdapter);

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    private void getRecipeSearchResult() {
        recipeAdapter.removeItem();
        itemKeyList.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference("recipe");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeTitleArray = new ArrayList<>();
                recipeUrlArray = new ArrayList<>();
                recipeThumbArray = new ArrayList<>();
                itemKeyList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    itemKeyList.add(snapshot.getKey());
                    recipeTitleArray.add(snapshot.child("title").getValue().toString());
                    recipeUrlArray.add(snapshot.child("clickUrl").getValue().toString());
                    recipeThumbArray.add(snapshot.child("imageUrl").getValue().toString());

                }

                for(int i=0; i<recipeTitleArray.size(); i++) {
                    if(recipeTitleArray.get(i).contains(searchText)) {

                        RecipeData data = new RecipeData();
                        data.setTitle(recipeTitleArray.get(i));
                        data.setClickUrl(recipeUrlArray.get(i));
                        data.setImageUrl(recipeThumbArray.get(i));
                        data.setItemKeyList(itemKeyList.get(i));
                        data.setBookmarkIdList(bookmarkIdList);

                        recipeAdapter.addItem(data);

                        Log.d("recipe_search",recipeTitleArray.get(i));
                    }
                }
                recyclerView.setAdapter(recipeAdapter);
//                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("SearchActivity", "loadPost:onCancelled", error.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getMapSearchResult() {
        mapAdapter.removeItem();
        itemKeyList.clear();

        mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MapName = new ArrayList<>();
                MapAddr = new ArrayList<>();
                MapCategory = new ArrayList<>();
                MapImage = new ArrayList<>();
                MapMenu = new ArrayList<>();
                MapTime = new ArrayList<>();
                MapDayoff = new ArrayList<>();
                MapPhone = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    MapName.add(snapshot.child("storeName").getValue().toString());
                    MapAddr.add(snapshot.child("storeAddr").getValue().toString());
                    MapCategory.add(snapshot.child("storeCategory").getValue().toString());
                    MapImage.add(snapshot.child("storeImage").getValue().toString());
                    MapMenu.add(snapshot.child("storeMenu").getValue().toString());
                    MapTime.add(snapshot.child("storeTime").getValue().toString());
                    MapDayoff.add(snapshot.child("storeDayOff").getValue().toString());
                    MapPhone.add(snapshot.child("storePhonenum").getValue().toString());
                    itemKeyList.add(snapshot.getKey());

                    Log.d("map_search",MapName.toString());
                }

                for(int i=0; i<MapName.size(); i++) {
                    if(MapName.get(i).contains(searchText) || MapCategory.get(i).contains(searchText) || MapMenu.get(i).contains(searchText)) {

                        MapData data = new MapData(MapName.get(i), MapAddr.get(i), MapCategory.get(i), MapImage.get(i));
                        data.setItemKeyList(itemKeyList.get(i));
                        data.setBookmarkIdList(bookmarkIdList);
                        data.setTime(MapTime.get(i));
                        data.setMenu(MapMenu.get(i));
                        data.setDayoff(MapDayoff.get(i));
                        data.setPhone(MapPhone.get(i));
                        data.setItemKeyList(itemKeyList.get(i));

                        mapAdapter.addItem(data);
                    }
                }
                recyclerView.setAdapter(mapAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("SearchActivity", "loadPost:onCancelled", error.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getRecipeBookmark() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                }

                getRecipeSearchResult();
                //recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("SearchActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("recipe_bookmark").addValueEventListener(postListener);
    }
    private void getMapBookmark() {

        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    bookmarkIdList.add(snapshot.getKey());

                }

                getMapSearchResult();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("SearchActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("map_bookmark").addValueEventListener(postListener);
    }
}