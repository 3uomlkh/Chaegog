package com.example.finalprojectvegan;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Adapter.MypageAdapter;
import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.UserVeganAllergyInfo;
import com.example.finalprojectvegan.Model.UserVeganTypeInfo;
import com.example.finalprojectvegan.Model.WritePostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class FragMypage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView userID, userVeganType, userAllergy, textView_notice;
    ImageView imageView_profile;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    String USER_ID;
    String USER_VEGAN_TYPE;
    String USER_ALLERGY;

    String uid;

    public FragMypage() {
        // Required empty public constructor
    }

    public static FragMypage newInstance(String param1, String param2) {
        FragMypage fragment = new FragMypage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragMypage newInstance() {
        return new FragMypage();
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
        View view =  inflater.inflate(R.layout.fragment_frag_mypage, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        textView_notice = view.findViewById(R.id.textView_notice);
        imageView_profile = view.findViewById(R.id.imageView_profile);
        userID = view.findViewById(R.id.userID);
        userVeganType = view.findViewById(R.id.userVeganType);
        userAllergy = view.findViewById(R.id.userAllergy);

        getFirebaseProfileImage(firebaseUser);

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserInfo(
                                            documentSnapshot.getData().get("userID").toString(),
                                            documentSnapshot.getData().get("userEmail").toString(),
                                            documentSnapshot.getData().get("userPassword").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_ID = documentSnapshot.getData().get("userID").toString();
                                        userID.setText(USER_ID);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        db.collection("userVeganType")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserVeganTypeInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserVeganTypeInfo(
                                            documentSnapshot.getData().get("veganType").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_VEGAN_TYPE = documentSnapshot.getData().get("veganType").toString();
                                        userVeganType.setText(USER_VEGAN_TYPE);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        db.collection("userVeganAllergy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserVeganAllergyInfo> postUserList = new ArrayList<>();

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {

                                String uid = firebaseUser.getUid();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    postUserList.add(new UserVeganAllergyInfo(
                                            documentSnapshot.getData().get("userAllergy").toString()));

                                    if (documentSnapshot.getId().equals(uid)) {
                                        USER_ALLERGY = documentSnapshot.getData().get("userAllergy").toString();
                                        userAllergy.setText(USER_ALLERGY);
                                    }
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<WritePostInfo> postList = new ArrayList<>();

                            if (firebaseUser != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                postList.add(new WritePostInfo(
                                        documentSnapshot.getData().get("title").toString(),
                                        documentSnapshot.getData().get("contents").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getData().get("imagePath").toString(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())));

//                                if (documentSnapshot.getData().get("publisher").toString() == uid) {
//                                    RecyclerView recyclerView = view.findViewById(R.id.mypage_recyclerView);
//                                    recyclerView.setHasFixedSize(true);
//                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                                    RecyclerView.Adapter mAdapter = new MypageAdapter(getActivity(), postList);
//                                    recyclerView.setAdapter(mAdapter);
//                                }
//                                if (documentSnapshot.getData().get("contents").toString() == "") {
//                                    textView_notice.setVisibility(View.VISIBLE);
//                                }

//                                textView_notice.setVisibility(View.VISIBLE);

//                                if (documentSnapshot.getId().equals(uid)) {
//                                    if (documentSnapshot.getData().get("contents") == null) {
//                                        textView_notice.setVisibility(VISIBLE);
//                                    } else {
//                                        textView_notice.setVisibility(GONE);
//                                    }
//                                }
                            }

                                textView_notice.setVisibility(GONE);

                                RecyclerView recyclerView = view.findViewById(R.id.mypage_recyclerView);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                RecyclerView.Adapter mAdapter = new MypageAdapter(getActivity(), postList);
                                recyclerView.setAdapter(mAdapter);


//                                if (documentSnapshot.getData().get("contents") == null) {
//                                    textView_notice.setVisibility(View.VISIBLE);
//                                } else {
//                                    textView_notice.setVisibility(View.GONE);
//                                }
                            }

//                            RecyclerView recyclerView = view.findViewById(R.id.mypage_recyclerView);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                            RecyclerView.Adapter mAdapter = new MypageAdapter(getActivity(), postList);
//                            recyclerView.setAdapter(mAdapter);

                            textView_notice.setVisibility(VISIBLE);

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        return view;
    }

    public void loadImage() {

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference pathReference = storageReference.child("users");
        if (pathReference == null) {
            Toast.makeText(getActivity(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child("users/" + firebaseUser.getUid() + "/profileImage.jpg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getActivity()).load(uri).centerCrop().override(300).into(imageView_profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void getFirebaseProfileImage(FirebaseUser id) {
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profileImage");
        if (!file.isDirectory()) {
            file.mkdir();
        }
        loadImage();
    }
}