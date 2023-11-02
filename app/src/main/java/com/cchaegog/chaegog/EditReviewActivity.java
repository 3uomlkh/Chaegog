package com.cchaegog.chaegog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditReviewActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private EditText editReview;
    private RatingBar editRatingBar;
    private Button editButton;
    private String reviewId, reviewRating, review, reviewPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        db = FirebaseFirestore.getInstance();

        editReview = findViewById(R.id.edit_review_review);
        editRatingBar = findViewById(R.id.edit_review_ratingBar);
        editButton = findViewById(R.id.btn_edit_review);

        Intent intent = getIntent();
        reviewId = intent.getStringExtra("EditReviewId");
        review = intent.getStringExtra("EditReview");
        reviewRating = intent.getStringExtra("EditReviewRatingBar");
//        reviewPublisher = intent.getStringExtra("EditReviewPublisher");

        editReview.setText(review);
        editRatingBar.setRating(Float.parseFloat(reviewRating));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String review = editReview.getText().toString();
                String rating = editRatingBar.getRating() + "";
//                String timeStamp = EditDate.format(new Date());
                Log.d("editreviewBtn", reviewId);
                db.collection("review").document(reviewId)
                        .update("review", review,
                        "rating", rating)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditReviewActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("EditReviewFailure", "Failure");
                            }
                        });
            }
        });
    }
}