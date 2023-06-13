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
import com.example.finalprojectvegan.Model.RecipeData;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchBtn;
    private TextView textView;
    private TabLayout tabs;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ArrayList<String> recipeTitleArray;
    ArrayList<String> recipeUrlArray;
    ArrayList<String> recipeThumbArray;
    ArrayList<String> title;
    Fragment fragment_recipe_search;
    private int TabPosition;
    RecyclerView recyclerView;
//    RecyclerView.Adapter adapter;

    RecipeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_btn);
//        fragment_recipe_search = new RecipeSearchFragment();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.search_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        tabs = findViewById(R.id.search_tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0)
                    TabPosition = 0;
                else if (position == 1) // 레시피
                    adapter = new RecipeAdapter();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.search_container,fragment_recipe_search).commitAllowingStateLoss();
                else if (position == 2)
                    TabPosition = 2;
                else if (position == 3)
                    TabPosition = 3;
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
                String searchText = searchEditText.getText().toString();
                adapter.removeItem(); // 쌓인 검색 결과를 모두 지움
                Query myTopPostsQuery = mDatabase.child("recipe");
                myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            recipeTitleArray = new ArrayList<>();
                            recipeUrlArray = new ArrayList<>();
                            recipeThumbArray = new ArrayList<>();

                            recipeTitleArray.add(postSnapshot.child("title").getValue().toString());
                            recipeUrlArray.add(postSnapshot.child("clickUrl").getValue().toString());
                            recipeThumbArray.add(postSnapshot.child("imageUrl").getValue().toString());

                            for(int i=0; i<recipeTitleArray.size(); i++) {
                                if(searchText == null) {
                                    Toast.makeText(SearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }else {
                                    if(recipeTitleArray.get(i).contains(searchText)) {

                                        RecipeData data = new RecipeData();
                                        data.setTitle(recipeTitleArray.get(i));
                                        data.setClickUrl(recipeUrlArray.get(i));
                                        data.setImageUrl(recipeThumbArray.get(i));

                                        recyclerView.setAdapter(adapter);
                                        adapter.addItem(data);

                                        Log.d("recipe_search",recipeTitleArray.get(i));
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("SearchActivity", "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });

//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("searchRecipeTitle",title);
//        bundle.putStringArrayList("searchRecipeUrl", recipeUrlArray);
//        bundle.putStringArrayList("searchRecipeThumb",recipeThumbArray);
//        fragment_recipe_search.setArguments(bundle);

    }
}