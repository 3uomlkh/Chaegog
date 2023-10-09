package com.example.finalprojectvegan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.finalprojectvegan.Adapter.BlockListAdapter;
import com.example.finalprojectvegan.Model.BlockUserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class BlockActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BlockListAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> blockedUserIdList, blockedUserNameList, blockedUserProfileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        blockedUserIdList = new ArrayList<>();
        blockedUserNameList = new ArrayList<>();
        blockedUserProfileList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.block_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BlockListAdapter();

        getBlockUser();
        recyclerView.setAdapter(adapter);
    }

    private void getBlockUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference("block_user");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                blockedUserIdList.clear();
                blockedUserNameList.clear();
                blockedUserProfileList.clear();
                adapter.removeItem();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    blockedUserIdList.add(Objects.requireNonNull(snapshot.child("id").getValue()).toString());
                    blockedUserNameList.add(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    blockedUserProfileList.add(Objects.requireNonNull(snapshot.child("profile").getValue()).toString());
                }

                for (int i=0; i<blockedUserIdList.size(); i++) {
                    BlockUserData blockUserData = new BlockUserData(blockedUserIdList.get(i), blockedUserNameList.get(i), blockedUserProfileList.get(i));
                    adapter.addItem(blockUserData);
                }

                adapter.notifyItemRemoved(adapter.position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("BlockActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(postListener);
    }
}