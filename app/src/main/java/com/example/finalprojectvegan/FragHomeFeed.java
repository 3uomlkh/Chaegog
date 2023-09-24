package com.example.finalprojectvegan;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Adapter.HomeFeedAdapter;
import com.example.finalprojectvegan.Model.BlockUserData;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.Model.MapData;
import com.example.finalprojectvegan.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragHomeFeed extends Fragment {
    private int myInt;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    private HomeFeedAdapter homeFeedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    private ArrayList<String> blockedUserNameList, blockedUserIdList, blockedUserProfileList, blockerIdList;
    private String blockId;

    public FragHomeFeed() {
    }

    public static FragHomeFeed newInstance(String param1, String param2) {
        FragHomeFeed fragment = new FragHomeFeed();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockedUserNameList = new ArrayList<>();
        blockedUserIdList = new ArrayList<>();
        blockedUserProfileList = new ArrayList<>();
        blockerIdList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_feed, container, false);

        pref = this.getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        myInt = pref.getInt("MyPrefInt", 1);

        // firebase 초기화
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        homeFeedAdapter.notifyDataSetChanged();

                    }
                }, 500);
            }
        });

        recyclerView = view.findViewById(R.id.homefeed_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeFeedAdapter = new HomeFeedAdapter(getActivity(), feedInfoList, uidList, myInt);

        getBlockedUser();
        //getPost();
        recyclerView.setAdapter(homeFeedAdapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.showDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.closeDialog();
                homeFeedAdapter.notifyDataSetChanged();
            }
        }, 2000);



        return view;
    }

    private void getBlockedUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference("block_user");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) { // 자신이 차단한 사용자의 ID 목록
                    blockedUserIdList.add(snapshot.child("id").getValue().toString());
                    blockedUserNameList.add(snapshot.child("name").getValue().toString());
                    blockedUserProfileList.add(snapshot.child("profile").getValue().toString());
                }
                Log.d("BLOCKUSER", "차단당한 ID : " + blockedUserIdList);

                for (int k = 0; k < blockedUserIdList.size(); k++) {

                    BlockUserData blockUserData = new BlockUserData(blockedUserIdList.get(k), blockedUserNameList.get(k), blockedUserProfileList.get(k));
                    homeFeedAdapter.addBlockUser(blockUserData);
                }

                homeFeedAdapter.notifyItemRemoved(homeFeedAdapter.blockPosition);
//                getPost();
                getBlockUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFeedFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(postListener);
    }

    public void getBlockUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference("blocked_user");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    blockerIdList.add(snapshot.getKey()); // 자신을 차단한 사용자의 ID 목록
                }
                Log.d("BLOCKUSER", "차단한 ID : " + blockerIdList);
                getPost();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HomeFeedFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(postListener);
    }

    public void getPost() {
        firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedInfoList.clear();
                uidList.clear();
                //homeFeedAdapter.removeItem();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FeedInfo feedInfo = dataSnapshot.getValue(FeedInfo.class);
                    String uidKey = dataSnapshot.getKey();
                    String publisher = dataSnapshot.child("publisher").getValue().toString();

                    if(!blockedUserIdList.contains(publisher) && !blockerIdList.contains(publisher)) {
                        feedInfoList.add(feedInfo);
                        uidList.add(uidKey);
                    }

                }
                homeFeedAdapter.notifyDataSetChanged();
                //homeFeedAdapter.notifyItemRemoved(homeFeedAdapter.blockPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}