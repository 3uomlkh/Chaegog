package com.cchaegog.chaegog.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cchaegog.chaegog.Model.WriteReviewInfo;
import com.cchaegog.chaegog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewWithoutImageAdapter extends RecyclerView.Adapter<ReviewWithoutImageAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WriteReviewInfo> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public ReviewWithoutImageAdapter(Context context, ArrayList<WriteReviewInfo> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewWithoutImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_without_image_item, parent, false);
        ReviewWithoutImageAdapter.ViewHolder viewHolder = new ReviewWithoutImageAdapter.ViewHolder(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewWithoutImageAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                TextView publisherTextView = cardView.findViewById(R.id.review_item_publisher2);
                                String user = mDataset.get(holder.getAdapterPosition()).getPublisher();
                                if (documentSnapshot.getId().equals(user)) {
                                    publisherTextView.setText(documentSnapshot.getData().get("userID").toString());
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

        TextView review_item_contents = cardView.findViewById(R.id.review_item_contents2);
        review_item_contents.setText(mDataset.get(position).getReview());

        TextView review_item_createdAt = cardView.findViewById(R.id.review_item_createdAt2);
        review_item_createdAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        RatingBar review_ratingbar = cardView.findViewById(R.id.review_ratingbar2);
        String rating = mDataset.get(position).getRating();
        Log.d("rating_bar", rating);
        float review_rating = Float.parseFloat(rating);
        review_ratingbar.setRating(review_rating);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
