package com.cchaegog.chaegog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchaegog.chaegog.Adapter.HomeFeedAdapter;
import com.cchaegog.chaegog.Model.FeedInfo;
import com.cchaegog.chaegog.Model.ReportInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragHomeFeed extends Fragment {
    private int myInt;
    private SharedPreferences pref;

    private RecyclerView recyclerView;
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    private List<String> ReportPost = new ArrayList<>();
    private HomeFeedAdapter homeFeedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    Map<String, Boolean> mail = new HashMap<>();
    Map<String, Object> mailSend = new HashMap<>();


    public FragHomeFeed() {
    }

    public static FragHomeFeed newInstance(String param1, String param2) {
        FragHomeFeed fragment = new FragHomeFeed();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_feed, container, false);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        pref = this.getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        myInt = pref.getInt("MyPrefInt", 1);

        // firebase 초기화
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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

//        DatabaseReference postRef = databaseReference.child("post");
//        DatabaseReference reportRef = databaseReference.child("report");

        firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedInfoList.clear();
                uidList.clear();
                ReportPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FeedInfo feedInfo = dataSnapshot.getValue(FeedInfo.class);
                    String uidKey = dataSnapshot.getKey();

                    databaseReference.child("report").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ReportInfo reportInfo = dataSnapshot.getValue(ReportInfo.class);
                                String post = reportInfo.getPostId();
                                ReportPost.add(post);
                            }
                            if (ReportPost.contains(feedInfo.getPostId())) {

                            } else {
                                feedInfoList.add(feedInfo);
                                uidList.add(uidKey);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if (feedInfo.getReportMail().equals("")) {
                        if (feedInfo.getReportCount() == 3) {
                            // 이메일 전송 구현
                            SendMail mailServer = new SendMail();
                            mailServer.sendSecurityCode(getActivity(), "cchaegog@gmail.com", feedInfo.getPostId());
//                            Log.d("이메일 전송", "성공");
                            mailSend.put("reportMail", "SEND");
                            databaseReference.child("posts").child(uidKey).updateChildren(mailSend);
                            mail.put(feedInfo.getPostId() + " Mail Send", true);
                            ReportInfo reportInfo = new ReportInfo(null, mail);
                            databaseReference.child("report").child("mail").child(feedInfo.getPostId()).setValue(reportInfo);
                        }
                    }

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