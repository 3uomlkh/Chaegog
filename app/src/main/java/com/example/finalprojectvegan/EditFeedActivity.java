package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditFeedActivity extends AppCompatActivity {

    // 객체 선언
    private EditText Et_EditFeed_Title, Et_EditFeed_Contents;
    private ImageView Iv_EditFeedPhoto;
    private Button Btn_EditFeed;
    private String FeedId, FeedPublisher, FeedTitle, FeedContent, FeedUri, FeedKey;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feed);

        // 객체 초기화
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Et_EditFeed_Title = findViewById(R.id.Et_EditFeed_Title);
        Et_EditFeed_Contents = findViewById(R.id.Et_EditFeed_Contents);
        Iv_EditFeedPhoto = findViewById(R.id.Iv_EditFeedPhoto);

        // 수정을 위해 HomeFeedAdapter로부터 Feed 정보 받아오기
        Intent intent = getIntent();
        FeedId = intent.getStringExtra("EditFeedId");
        FeedTitle = intent.getStringExtra("EditFeedTitle");
        FeedContent = intent.getStringExtra("EditFeedContent");
        FeedUri = intent.getStringExtra("EditFeedUri");
        FeedKey = intent.getStringExtra("EditFeedKey");

        // 받아온 내용으로 수정 창에 원래 내용 나타내기
        Et_EditFeed_Title.setText(FeedTitle);
        Et_EditFeed_Contents.setText(FeedContent);
        Glide.with(this)
                .load(FeedUri)
                .into(Iv_EditFeedPhoto);

        // 수정 버튼 클릭시
        Btn_EditFeed = findViewById(R.id.Btn_EditFeed);
        Btn_EditFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 수정한 내용 String에 다시 저장하기
                String Title = Et_EditFeed_Title.getText().toString();
                String Content = Et_EditFeed_Contents.getText().toString();

                // 수정된 내용 db에 올리기
                db.collection("posts").document(FeedId)
                        .update("title", Title,
                                "content", Content)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Edit_Feed_Success", "Success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Edit_Feed_Failure", "Failure");
                            }
                        });

                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("title", Title);
                taskMap.put("content", Content);
                databaseReference.child("posts").child(FeedKey).updateChildren(taskMap);
                finish();

//                databaseReference.child("posts").child(FeedKey).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        FeedInfo feedInfo = snapshot.getValue(FeedInfo.class);
//                        String title = feedInfo.getTitle();
//                        title.replace(title, Et_EditFeed_Title.getText().toString());
////                        title = Et_EditFeed_Title.getText().toString();
//                        feedInfo.setTitle(title);
//                        feedInfo.setContent(Content);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });

    }
}