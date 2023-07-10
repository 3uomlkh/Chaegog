//package com.example.finalprojectvegan.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.finalprojectvegan.Model.RecipeData;
//import com.example.finalprojectvegan.Model.WriteReviewInfo;
//import com.example.finalprojectvegan.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {
//    private Context mContext;
//    private FirebaseAuth mAuth;
//    private FirebaseUser firebaseUser;
//    private ArrayList<User> mUsers = new ArrayList<>();;
//    @NonNull
//    @Override
//    public UserSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
//        return new UserSearchAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserSearchAdapter.ViewHolder holder, int i) {
//        holder.onBind(mUsers.get(i));
//
//        holder.userName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "유저 클릭됨", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return mUsers.size();
//    }
//
//    public void addItem(User data) {
//        mUsers.add(data);
//    }
//
//    public void removeItem() {
//        mUsers.clear();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView userName;
//        public TextView userEmail;
//        public CircleImageView userProfile;
//        public Button followBtn;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.username);
//            userEmail = itemView.findViewById(R.id.useremail);
//            userProfile = itemView.findViewById(R.id.image_profile);
//            //followBtn = itemView.findViewById(R.id.btn_follow);
//        }
//        void onBind(User data) {
//            userName.setText(data.getId());
//            userEmail.setText(data.getUserEmail());
//        }
//    }
//}
//
