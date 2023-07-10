package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.Model.FeedInfo;
import com.example.finalprojectvegan.MypageActivity;
import com.example.finalprojectvegan.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder>{

    private Context context;
    private ArrayList<FeedInfo> MyFeedDataset;
    private String MyFeedId, Uid, User;
    private Button Btn_MyFeedComment;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    private ImageView Iv_MyFeed_Item, Iv_MyFeed_Profile;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;

            Btn_MyFeedComment = view.findViewById(R.id.Btn_item_MyFeedComment);
            Btn_MyFeedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 클릭된 view 파악
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        MyFeedId = MyFeedDataset.get(pos).getPostId();
                        Log.d("MyFeedDOCUMENTID_Send", MyFeedId);
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("MyPOSTSDocumentId", MyFeedId);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public MyFeedAdapter(Context context, ArrayList<FeedInfo> myDataset) {
        MyFeedDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public MyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.myfeed_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(cardView);
//        Iv_MyFeed_Item = cardView.findViewById(R.id.mypage_item_imageView);
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        return viewHolder;
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Iv_MyFeed_Item = cardView.findViewById(R.id.Iv_MyFeed_item_Image);
        Iv_MyFeed_Profile = cardView.findViewById(R.id.Iv_MyFeed_Profile);


        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (firebaseUser != null) {

                                Uid = firebaseUser.getUid();
                                User = MyFeedDataset.get(holder.getAbsoluteAdapterPosition()).getPublisher();

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("MYFEEDADAPTER SUCCESS", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                    // document ID가 피드 작성자와 같고, 현재 로그인한 유저와도 같은 경우
                                    if (documentSnapshot.getId().equals(User)) {
                                        if ( documentSnapshot.getId().equals(Uid)) {

                                            cardView.setVisibility(View.VISIBLE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                            cardView.setLayoutParams(params);

                                            TextView titleTextView = cardView.findViewById(R.id.Tv_MyFeed_item_Title);
                                            titleTextView.setText(MyFeedDataset.get(holder.getAdapterPosition()).getTitle());

                                            TextView createdAtTextView = cardView.findViewById(R.id.Tv_MyFeed_item_CreatedAt);
                                            createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(MyFeedDataset.get(holder.getAdapterPosition()).getCreatedAt()));

                                            TextView contentsTextView =cardView.findViewById(R.id.Tv_MyFeed_item_Contents);
                                            contentsTextView.setText(MyFeedDataset.get(holder.getAdapterPosition()).getContent());

                                            String url = MyFeedDataset.get(holder.getAdapterPosition()).getUri();

                                            Glide.with(cardView).load(url).override(800, 800).into(Iv_MyFeed_Item);

                                        } else {
                                            cardView.setVisibility(View.GONE);
                                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                                            params.height = 0;
                                            params.width = 0;
                                            cardView.setLayoutParams(params);
                                        }
                                    }
                                }
                            }

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return MyFeedDataset.size();
    }
}
