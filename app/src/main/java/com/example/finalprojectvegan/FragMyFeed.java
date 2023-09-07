package com.example.finalprojectvegan;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Adapter.MyFeedAdapter;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragMyFeed extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<FeedInfo> feedInfoList = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();
    MyFeedAdapter myFeedAdapter;

    TextView Tv_MyFeed_UerId, Tv_MyFeed_VeganType, Tv_MyFeed_Allergy, Tv_Notice;

    ImageView Iv_MyFeed_Profile;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db;
    //    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    String USER_ID;
    String USER_VEGAN_TYPE;
    String USER_ALLERGY;
    String USER_PROFILE_IMG;
    String Uid;

    ProgressDialog progressDialog;

    public FragMyFeed() {
        // Required empty public constructor
    }

    public static FragMyFeed newInstance(String param1, String param2) {
        FragMyFeed fragment = new FragMyFeed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragMyFeed newInstance() {
        return new FragMyFeed();
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
        View view =  inflater.inflate(R.layout.fragment_frag_my_feed, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Tv_Notice = view.findViewById(R.id.Tv_Notice);
        Tv_MyFeed_UerId = view.findViewById(R.id.Tv_MyFeed_UerId);
        Tv_MyFeed_VeganType = view.findViewById(R.id.Tv_MyFeed_VeganType);
        Tv_MyFeed_Allergy = view.findViewById(R.id.Tv_MyFeed_Allergy);

        Iv_MyFeed_Profile = view.findViewById(R.id.Iv_MyFeed_Profile);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.showDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.closeDialog();
            }
        }, 2000);

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser != null) {

                                Uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MYFEED", "SUCCESS");

                                    if (documentSnapshot.getId().equals(Uid)) {
                                        USER_ID = documentSnapshot.getData().get("userId").toString();
                                        USER_VEGAN_TYPE = documentSnapshot.getData().get("userVeganType").toString();
                                        USER_ALLERGY = documentSnapshot.getData().get("userAllergy").toString().replaceAll("\\[|\\]", "").replaceAll(", ",", ");
                                        USER_PROFILE_IMG = documentSnapshot.getData().get("userProfileImg").toString();
                                        Tv_MyFeed_UerId.setText(USER_ID);
                                        Tv_MyFeed_VeganType.setText(USER_VEGAN_TYPE);
                                        Tv_MyFeed_Allergy.setText(USER_ALLERGY);
                                        Glide.with(FragMyFeed.this)
                                                .load(USER_PROFILE_IMG)
                                                .into(Iv_MyFeed_Profile);
                                    }
                                }
                            }
                        } else {
                            Log.d("MYFEED", "ERROR", task.getException());
                        }
                    }
                });

        recyclerView = view.findViewById(R.id.Rv_MyFeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myFeedAdapter = new MyFeedAdapter(getActivity(), feedInfoList, uidList);
        recyclerView.setAdapter(myFeedAdapter);

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
                myFeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            ArrayList<FeedInfo> MyFeedList = new ArrayList<>();
//
//                            if (firebaseUser != null) {
//
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d("MYFEED POST", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                                MyFeedList.add(new FeedInfo(
//                                        documentSnapshot.getData().get("title").toString(),
//                                        documentSnapshot.getData().get("content").toString(),
//                                        documentSnapshot.getData().get("publisher").toString(),
//                                        documentSnapshot.getId(),
//                                        documentSnapshot.getData().get("uri").toString(),
//                                        new Date(documentSnapshot.getDate("createdAt").getTime())));
//
//                            }
//
//                                Tv_Notice.setVisibility(GONE);
//
//                                RecyclerView recyclerView = view.findViewById(R.id.Rv_MyFeed);
//                                recyclerView.setHasFixedSize(true);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                                RecyclerView.Adapter mAdapter = new MyFeedAdapter(getActivity(), MyFeedList);
//                                recyclerView.setAdapter(mAdapter);
//
//                            }
//
//                            Tv_Notice.setVisibility(VISIBLE);
//
//                        } else {
//                            Log.d("error", "Error getting documents", task.getException());
//                        }
//                    }
//                });
//
        return view;
    }


//    public void loadImage() {
//
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("users");
//        if (pathReference == null) {
//            Toast.makeText(getActivity(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
//        } else {
//            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(getActivity()).load(uri).centerCrop().override(300).into(imageView_profile);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }
//
//    private void getFirebaseProfileImage(FirebaseUser id) {
//        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profileImage");
//        if (!file.isDirectory()) {
//            file.mkdir();
//        }
//        loadImage();
//    }
}