package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectvegan.Adapter.MapSearchAdapter;
import com.example.finalprojectvegan.Model.MapData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MapSearchActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<String> MapName, MapAddr, MapTime, MapDayoff,
            MapCategory, MapImage, MapMenu, MapPhone;
    private MapSearchAdapter mapAdapter;
    private RecyclerView recyclerView;
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private TextView searchEditText;
    private Button searchBtn;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        searchEditText = findViewById(R.id.search_edit_text);
        searchBtn = findViewById(R.id.search_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.map_search_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mapAdapter = new MapSearchAdapter();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = searchEditText.getText().toString();
                if(searchText.equals("")) {
                    Toast.makeText(MapSearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    getMapBookmark();
                }
            }
        });

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