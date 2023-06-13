package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Model.MapData;
import com.example.finalprojectvegan.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 식당 북마크
public class FragBookmark1 extends Fragment {
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listAddr = new ArrayList<>();
    ArrayList<String> listCategory = new ArrayList<>();
    ArrayList<String> listImage = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private View view;
    RecyclerView recyclerView;
    MapBookmarkAdapter adapter;
    public FragBookmark1() {

    }

    public static FragBookmark1 newInstance(String param1, String param2) {
        FragBookmark1 fragment = new FragBookmark1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listName = new ArrayList<>();
        listAddr = new ArrayList<>();
        listCategory = new ArrayList<>();
       listImage = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_bookmark1, container, false);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.bookmark1_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MapBookmarkAdapter();

        getBookmarkData();

        return view;
    }
    private void getCategoryData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(bookmarkIdList.contains(snapshot.getKey())) {
                        itemKeyList.add(snapshot.getKey());
                    }

                }

                for(int i=0; i<listName.size(); i++) {
                    MapData data = new MapData(listName.get(i), listAddr.get(i), listCategory.get(i), listImage.get(i));
                    data.setItemKeyList(itemKeyList.get(i));
                    data.setBookmarkIdList(bookmarkIdList);
                    adapter.addItem(data);
                }

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getBookmarkData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    bookmarkIdList.add(snapshot.getKey());
                    listImage.add(snapshot.child("imageUrl").getValue().toString());
                    listName.add(snapshot.child("name").getValue().toString());
                    listAddr.add(snapshot.child("address").getValue().toString());
                    listCategory.add(snapshot.child("category").getValue().toString());

                }

                getCategoryData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("map_bookmark").addValueEventListener(postListener);
    }
}