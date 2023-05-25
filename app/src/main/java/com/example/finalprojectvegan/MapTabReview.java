package com.example.finalprojectvegan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.finalprojectvegan.Adapter.ReviewAdapter;
import com.example.finalprojectvegan.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MapTabReview extends Fragment {
    Button review_button;
    TextView review_textView;
    RatingBar arg_ratingBar;
    private String name;
    float result_rating;
    float arg_rating;
//    private String[] ratingList;
    private ArrayList<String> ratingList = new ArrayList<>();
    public MapTabReview() {
        // Required empty public constructor
    }

    public static MapTabReview newInstance(String param1, String param2) {
        MapTabReview fragment = new MapTabReview();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_tab_review, container, false);
        review_textView = view.findViewById(R.id.review_textView);
        arg_ratingBar = view.findViewById(R.id.arg_ratingBar);

        ratingList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("review")
                    .whereEqualTo("name", name) // "name" 필드의 값이 클릭한 식당 이름과 같아야 불러옴
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {


                                ArrayList<WriteReviewInfo> postList = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("success_review", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    ratingList.add(documentSnapshot.getData().get("rating").toString());

                                    postList.add(new WriteReviewInfo(
                                            documentSnapshot.getData().get("rating").toString(),
                                            documentSnapshot.getData().get("name").toString(),
                                            documentSnapshot.getData().get("review").toString(),
                                            documentSnapshot.getData().get("publisher").toString(),
                                            documentSnapshot.getData().get("imagePath1").toString(),
                                            new Date(documentSnapshot.getDate("createdAt").getTime())));

                                }

                                RecyclerView recyclerView = view.findViewById(R.id.review_recyclerview);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                RecyclerView.Adapter mAdapter = new ReviewAdapter(getActivity(), postList);
                                recyclerView.setAdapter(mAdapter);

                                result_rating = 0;
                                arg_rating = 0;

                                // 평균 별점 구하기
                                for(int i=0; i<ratingList.size(); i++) {
                                    float rating = Float.parseFloat(ratingList.get(i));
                                    result_rating += rating;
                                    Log.d("result_rating : ", result_rating + "");
                                    arg_rating = result_rating / ratingList.size();
                                    Log.d("arg_rating : ", arg_rating + "");
                                }
                                Log.d("ratingSize : ", ratingList.size() + "");
                                review_textView.setText(String.valueOf(arg_rating));
                                arg_ratingBar.setRating(arg_rating);


                            } else {
                                Log.d("error", "Error getting documents", task.getException());
                            }
                        }
                    });


            review_button = view.findViewById(R.id.review_button);
            review_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),WriteReviewActivity.class); //fragment라서 activity intent와는 다른 방식
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });

            return view;
    }
}