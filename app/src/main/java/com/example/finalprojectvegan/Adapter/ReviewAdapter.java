package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.EditReviewActivity;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WriteReviewInfo> reviewDataset;
    ImageView review_item_imageView, menu_option;
    PopupMenu popupMenu;
    FirebaseFirestore db;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String reviewId, reviewRating, reviewUri, reviewPublisher, reviewCreateAt, reviewName, review;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public ReviewAdapter(Context context, ArrayList<WriteReviewInfo> myDataset) {
        this.reviewDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder(cardView);
        review_item_imageView = cardView.findViewById(R.id.review_item_imageView);


        db = FirebaseFirestore.getInstance();
        // 수정 및 삭제
        menu_option = cardView.findViewById(R.id.review_menu_option);
        menu_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(context.getApplicationContext(), cardView);

                int pos = viewHolder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    reviewPublisher = reviewDataset.get(pos).getPublisher();
                    reviewId = reviewDataset.get(pos).getReviewId();

                    if (firebaseUser.getUid().equals(reviewPublisher)) {
                        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.feed_edit:
                                        reviewId = reviewDataset.get(pos).getReviewId();
                                        reviewPublisher = reviewDataset.get(pos).getPublisher();
                                        reviewUri = reviewDataset.get(pos).getImagePath1();
                                        reviewName = reviewDataset.get(pos).getName();
                                        review = reviewDataset.get(pos).getReview();
                                        reviewCreateAt = String.valueOf(reviewDataset.get(pos).getCreatedAt());

                                        Intent intent = new Intent(context, EditReviewActivity.class);
                                        intent.putExtra("EditReviewId", reviewId);
                                        intent.putExtra("EditReview", review);
                                        intent.putExtra("EditReviewRatingBar",reviewRating);
                                        intent.putExtra("EditCreateAt", reviewCreateAt);
                                        intent.putExtra("EditReviewPublisher", reviewPublisher);
                                        context.startActivity(intent);

                                        return true;
                                    case R.id.feed_delete:
                                        db.collection("review").document(reviewId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popupMenu.show();
                    }
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<UserInfo> postUserList = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                postUserList.add(new UserInfo(
                                        documentSnapshot.getData().get("userID").toString(),
                                        documentSnapshot.getData().get("userEmail").toString(),
                                        documentSnapshot.getData().get("userPassword").toString()));

                                TextView publisherTextView = cardView.findViewById(R.id.review_item_publisher);
                                String reviewPublisher = reviewDataset.get(position).getPublisher();
                                if (documentSnapshot.getId().equals(reviewPublisher)) {
                                    publisherTextView.setText(documentSnapshot.getData().get("userID").toString());
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        TextView review_item_contents = cardView.findViewById(R.id.review_item_contents);
        review_item_contents.setText(reviewDataset.get(position).getReview());

        TextView review_item_createdAt = cardView.findViewById(R.id.review_item_createdAt);
        review_item_createdAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(reviewDataset.get(position).getCreatedAt()));

        RatingBar review_ratingbar = cardView.findViewById(R.id.review_ratingbar);
        reviewRating = reviewDataset.get(position).getRating();
        float review_rating = Float.parseFloat(reviewRating);
        review_ratingbar.setRating(review_rating);

        reviewUri = reviewDataset.get(position).getImagePath1();

        Glide.with(cardView)
                .load(reviewUri)
                .override(500, 500)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(review_item_imageView);

    }

    @Override
    public int getItemCount() {
        return reviewDataset.size();
    }

}
