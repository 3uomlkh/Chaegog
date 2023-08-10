package com.example.finalprojectvegan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.finalprojectvegan.Adapter.HomefeedAdapter;
import com.example.finalprojectvegan.Model.FeedFavorite;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.Model.WritePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class FragHomeFeed extends Fragment {

    public static final String ARG_OBJECT = "object";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;

//    private ArrayList<FeedInfo> FeedList = new ArrayList<>();
//    private ArrayList<FeedFavorite> FeedFavoriteList = new ArrayList<>();

    ProgressDialog progressDialog;

    public FragHomeFeed() {
        // Required empty public constructor
    }

    public static FragHomeFeed newInstance(String param1, String param2) {
        FragHomeFeed fragment = new FragHomeFeed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragHomeFeed newInstance() {
        return new FragHomeFeed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_feed, container, false);

        // firebase 초기화
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.showDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.closeDialog();
            }
        }, 3000);

        recyclerView = view.findViewById(R.id.homefeed_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        RecyclerView.Adapter mAdapter = new HomefeedAdapter(getActivity(), FeedList, FeedFavoriteList);
//        recyclerView.setAdapter(mAdapter);

        // posts DB로부터 정보 가져오기
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // FeedInfo 형식으로 리스트 선언
                            ArrayList<FeedInfo> FeedList = new ArrayList<>();
                            ArrayList<FeedFavorite> FeedFavoriteList = new ArrayList<>();

                            // document 실행
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                // 선언한 리스트에 정보 추가하기 -> DB로부터 가져온 정보들(제목, 내용, 작성자, 포스트ID, 사진, 날짜)
                                FeedList.add(new FeedInfo(
                                        documentSnapshot.getData().get("title").toString(),
                                        documentSnapshot.getData().get("content").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getId(),
                                        documentSnapshot.getData().get("uri").toString(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())));

                            }

                            firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    FeedFavoriteList.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        FeedFavorite feedFavorite = dataSnapshot.getValue(FeedFavorite.class);
                                        FeedFavoriteList.add(feedFavorite);
                                    }
//                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            // 연결할 RecyclerView 선언
                            RecyclerView recyclerView = view.findViewById(R.id.homefeed_recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            RecyclerView.Adapter mAdapter = new HomefeedAdapter(getActivity(), FeedList, FeedFavoriteList);
                            recyclerView.setAdapter(mAdapter);

//                            firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    FeedFavoriteList.clear();
//                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                        FeedFavorite feedFavorite = dataSnapshot.getValue(FeedFavorite.class);
//                                        FeedFavoriteList.add(feedFavorite);
//                                    }
//                                    mAdapter.notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });

                            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {

                                    mAdapter.notifyItemInserted(FeedList.size());
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });

//                            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                                @Override
//                                public void onRefresh() {
//                                    mAdapter.notifyItemInserted(FeedList.size());
//                                    mAdapter.notifyDataSetChanged();
////                                   mAdapter.notifyItemChanged(0);
//                                    recyclerView.setAdapter(mAdapter);
//                                    swipeRefreshLayout.setRefreshing(false);
//                                }
//                            });

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        return view;

//        return view;
    }

}