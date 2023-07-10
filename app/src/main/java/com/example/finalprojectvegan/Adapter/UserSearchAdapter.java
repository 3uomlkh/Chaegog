package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectvegan.Model.UserProfile;
import com.example.finalprojectvegan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<UserProfile> mUsers = new ArrayList<>();;
    @NonNull
    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchAdapter.ViewHolder holder, int i) {
        holder.onBind(mUsers.get(i));

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "유저 클릭됨", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return mUsers.size();
    }

    public void addItem(UserProfile data) {
        mUsers.add(data);
    }

    public void removeItem() {
        mUsers.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView userEmail;
        public CircleImageView userProfile;
        public Button followBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            userEmail = itemView.findViewById(R.id.useremail);
            userProfile = itemView.findViewById(R.id.image_profile);
            //followBtn = itemView.findViewById(R.id.btn_follow);
        }
        void onBind(UserProfile data) {
            userName.setText(data.getUserId());
            userEmail.setText(data.getUserEmail());
        }
    }
}

