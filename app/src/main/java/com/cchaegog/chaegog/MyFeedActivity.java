//package com.example.finalprojectvegan;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.finalprojectvegan.Model.WritePostInfo;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//public class MyFeedActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_feed);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            ArrayList<WritePostInfo> postList = new ArrayList<>();
//
//                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//                            String uid = firebaseUser.getUid();
//
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                                postList.add(new WritePostInfo(
//                                        documentSnapshot.getData().get("title").toString(),
//                                        documentSnapshot.getData().get("contents").toString(),
//                                        documentSnapshot.getData().get("publisher").toString(),
//                                        documentSnapshot.getData().get("imagePath").toString(),
//                                        new Date(documentSnapshot.getDate("createdAt").getTime())));
//
////                                if (documentSnapshot.getData().get("publisher").toString() == uid) {
////                                    RecyclerView recyclerView = view.findViewById(R.id.mypage_recyclerView);
////                                    recyclerView.setHasFixedSize(true);
////                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////
////                                    RecyclerView.Adapter mAdapter = new MypageAdapter(getActivity(), postList);
////                                    recyclerView.setAdapter(mAdapter);
////                                }
//                            }
//
////                            RecyclerView recyclerView = findViewById(R.id.MyFeed_recyclerview);
////                            recyclerView.setHasFixedSize(true);
////                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////                            RecyclerView.Adapter mAdapter = new MyfeedAdapter();
////                            recyclerView.setAdapter(mAdapter);
//
//                        } else {
//                            Log.d("error", "Error getting documents", task.getException());
//                        }
//                    }
//                });
//    }
//}