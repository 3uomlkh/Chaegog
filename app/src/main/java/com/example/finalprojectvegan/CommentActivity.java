package com.example.finalprojectvegan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectvegan.Adapter.CommentAdapter;
import com.example.finalprojectvegan.Fcm.NotificationAPI;
import com.example.finalprojectvegan.Fcm.NotificationData;
import com.example.finalprojectvegan.Fcm.PushNotification;
import com.example.finalprojectvegan.Fcm.RetrofitInstance;
import com.example.finalprojectvegan.Model.WriteCommentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CommentActivity extends AppCompatActivity {

    private Button Btn_UploadComment;
    private EditText Et_Comment;
    private String FeedId;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
//    private DatabaseReference databaseReference;
//    private FirebaseDatabase firebaseDatabase;
    private String postPublisher, token;
    PushNotification pushNotification;
    static String TOPIC = "/topics/myTopic";
    private int myInt;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // SharedPreferences 초기화 후 저장해둔 값 불러오기
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        myInt = pref.getInt("MyPrefInt", 1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Btn_UploadComment = findViewById(R.id.Btn_UploadComment);
        Et_Comment = findViewById(R.id.Et_Comment);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

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

        //fcm 메세지 클릭시
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            FeedId = extras.getString("POSTSDocumentId");
            Log.d("Mybundle" , "fcm 클릭시 " + FeedId);
        }

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

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if(documentSnapshot.getData().get("postId").equals(FeedId)) {
                                    // 댓글이 달린 게시물의 작성자 id를 변수에 저장
                                    postPublisher = documentSnapshot.getData().get("publisher").toString();
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
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
            if(myInt == 0) {
                WriteCommentInfo writeCommentInfo = new WriteCommentInfo(comment, firebaseUser.getUid(), FeedId, new Date());
                uploader(writeCommentInfo);
            } else {
            if(postPublisher.equals(firebaseUser.getUid())) { // 게시물 작성자 == 댓글 작성자라면 알림을 보내지 않음
                WriteCommentInfo writeCommentInfo = new WriteCommentInfo(comment, firebaseUser.getUid(), FeedId, new Date());
                uploader(writeCommentInfo);
            } else {
                sendCommentToFCM();
                WriteCommentInfo writeCommentInfo = new WriteCommentInfo(comment, firebaseUser.getUid(), FeedId, new Date());
                uploader(writeCommentInfo);
            }
            }
        } else {
            Toast.makeText(this, "댓글 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCommentToFCM() {
        final String comment = Et_Comment.getText().toString();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.getId().equals(postPublisher)) { // user 테이블의 사용자 id가 댓글이 달린 게시글 작성자 id와 같다면
                                    token = documentSnapshot.getData().get("userToken").toString(); // 해당 사용자의 토큰을 얻는다.

                                    NotificationData data = new NotificationData("채곡채곡", "댓글이 달렸습니다 : " + comment + FeedId);
                                    pushNotification = new PushNotification(data, token);
//                                    Log.d("pushNoti",  "알림 메세지 : " + pushNotification.getNotificationData().getTitle() + ", " + pushNotification.getNotificationData().getBody()
//                                            +"\n" + "게시글 작성자 토큰 :  " + pushNotification.getTo());
                                    SendNotification(pushNotification);
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    public void SendNotification(PushNotification pushNotification) {

        NotificationAPI api = RetrofitInstance.getClient().create(NotificationAPI.class);
        retrofit2.Call<ResponseBody> responseBodyCall = api.sendNotification(pushNotification);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("SendNotification","성공");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SendNotification","실패");
            }
        });

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