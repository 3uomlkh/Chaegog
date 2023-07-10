package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectvegan.Model.UserProfile;
import com.example.finalprojectvegan.R;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder>{

    Fragment fragment;
    private Context context;
    private ArrayList<UserProfile> uDataset;

    ImageView homefeed_item_imageView;

    public UserInfoAdapter(Context context, ArrayList<UserProfile> userDataset) {
        uDataset = userDataset;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }
//
//    public HomefeedAdapter(Context context, ArrayList<WritePostInfo> myDataset, ArrayList<UserInfo> userDataset) {
//        mDataset = myDataset;
//        uDataset = userDataset;
//        this.context = context;
//    }

    @NonNull
    @Override
    public UserInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.homefeed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        homefeed_item_imageView = cardView.findViewById(R.id.Iv_HomeFeed_Image);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView publisherTextView = cardView.findViewById(R.id.Tv_HomeFeed_Publisher);
        publisherTextView.setText(uDataset.get(position).getUserId());


    }

    @Override
    public int getItemCount() {
        return uDataset.size();
    }

//    public void loadImage() {
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("posts");
//        if (pathReference == null) {
//
//        } else {
//            StorageReference submitProfile = storageReference.child("posts/" + firebaseUser.getUid() + "/postImage" + System.currentTimeMillis() + ".jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).into(homefeed_item_imageView);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }

}
