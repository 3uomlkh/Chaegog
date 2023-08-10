package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectvegan.Adapter.CommentAdapter;
import com.example.finalprojectvegan.Model.WriteCommentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    private Button Btn_UploadComment;
    private EditText Et_Comment;
    private String FeedId;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Btn_UploadComment = findViewById(R.id.Btn_UploadComment);
        Et_Comment = findViewById(R.id.Et_Comment);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Btn_UploadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadComment();
                finish();
            }
        });

        Intent intent = getIntent();
        FeedId = intent.getStringExtra("POSTSDocumentId");
        Log.d("DOCUMENTID_Receive", FeedId);

        db = FirebaseFirestore.getInstance();
        db.collection("posts/" + FeedId + "/comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WriteCommentInfo> commentList = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("Comment success", documentSnapshot.getId() + "=>" + documentSnapshot.getData());
                                commentList.add(new WriteCommentInfo(
                                        documentSnapshot.getData().get("comment").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getId(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())
                                ));
                            }
                            RecyclerView recyclerView = findViewById(R.id.Rv_Comment);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

                            RecyclerView.Adapter cAdapter = new CommentAdapter(commentList);
                            recyclerView.setAdapter(cAdapter);

//                            ((CommentAdapter) cAdapter).setComment(commentList);
                        }
                    }
                });

    }

    // 툴바에서 뒤로가기 아이콘 클릭시 -> 해당 액티비티 종료 후 게시물 화면으로 돌아간다.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadComment() {

        final String comment = Et_Comment.getText().toString();

        if (comment.length() > 0) {
            WriteCommentInfo writeCommentInfo = new WriteCommentInfo(comment, firebaseUser.getUid(), FeedId, new Date());
            uploader(writeCommentInfo);
        } else {
            Toast.makeText(this, "댓글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploader (WriteCommentInfo writeCommentInfo) {

        CollectionReference collectionReference = db.collection("posts").document(FeedId).collection("comments");
        collectionReference
                .add(writeCommentInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("COMMENTSUPLOAD Success", FeedId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("COMMENTSUPLOAD Failure", FeedId);;
                    }
                });

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("posts");
//        databaseReference.child(FeedId).child("comment").setValue(writeCommentInfo);

    }
}