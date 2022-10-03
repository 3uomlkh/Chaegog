package com.example.finalprojectvegan;

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

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder>{

    Fragment fragment;
    private Context context;
    private ArrayList<UserInfo> uDataset;

    ImageView homefeed_item_imageView;

    public UserInfoAdapter(Context context, ArrayList<UserInfo> userDataset) {
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
        homefeed_item_imageView = cardView.findViewById(R.id.homefeed_item_imageView);

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

        TextView publisherTextView = cardView.findViewById(R.id.homefeed_item_publisher);
        publisherTextView.setText(uDataset.get(position).getUserID());


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
