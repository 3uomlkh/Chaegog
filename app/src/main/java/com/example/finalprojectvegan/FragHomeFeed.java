package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Adapter.HomeFeedAdapter;
import com.example.finalprojectvegan.Model.FeedInfo;
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

    public static final String ARG_OBJECT = "object";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    HomeFeedAdapter homeFeedAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

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

        homeFeedAdapter = new HomeFeedAdapter(getActivity(), feedInfoList, uidList);
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

        firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedInfoList.clear();
                uidList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FeedInfo feedInfo = dataSnapshot.getValue(FeedInfo.class);
                    String uidKey = dataSnapshot.getKey();

                    feedInfoList.add(feedInfo);
                    uidList.add(uidKey);
                }
                homeFeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}