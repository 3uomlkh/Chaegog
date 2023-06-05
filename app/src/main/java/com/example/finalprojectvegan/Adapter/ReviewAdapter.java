package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.Model.UserInfo;
import com.example.finalprojectvegan.Model.WriteReviewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WriteReviewInfo> mDataset;
    ImageView review_item_imageView;
    private ArrayList<Uri> uriList;
    ViewPager2 viewPager2;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public ReviewAdapter(Context context, ArrayList<WriteReviewInfo> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder(cardView);
        review_item_imageView = cardView.findViewById(R.id.review_item_imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        TextView review_item_contents = cardView.findViewById(R.id.review_item_contents);
        review_item_contents.setText(mDataset.get(position).getReview());

        TextView review_item_createdAt = cardView.findViewById(R.id.review_item_createdAt);
        review_item_createdAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        RatingBar review_ratingbar = cardView.findViewById(R.id.review_ratingbar);
        String rating = mDataset.get(position).getRating();
        Log.d("rating_bar", rating);
        float review_rating = Float.parseFloat(rating);
        review_ratingbar.setRating(review_rating);

        String url = mDataset.get(position).getImagePath1();

        Glide.with(cardView)
                .load(url)
                .override(500, 500)
                .apply(new RequestOptions().transform(new CenterCrop(),
                        new RoundedCorners(10)))
                .into(review_item_imageView);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
