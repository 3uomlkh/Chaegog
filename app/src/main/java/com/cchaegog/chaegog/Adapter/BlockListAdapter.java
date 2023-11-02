package com.cchaegog.chaegog.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cchaegog.chaegog.Model.BlockUserData;
import com.cchaegog.chaegog.Model.RecipeData;
import com.cchaegog.chaegog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference blockRef;
    private DatabaseReference blockedRef;
    private ArrayList<BlockUserData> userData = new ArrayList<>();
    public int position;

    @NonNull
    @Override
    public BlockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_item, parent, false);
        return new BlockListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockListAdapter.ViewHolder holder, int i) {
        holder.onBind(userData.get(i));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        blockRef = database.getReference("block_user");
        blockedRef = database.getReference("blocked_user");

        holder.blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = holder.getAbsoluteAdapterPosition();
                blockRef
                        .child(mAuth.getCurrentUser().getUid())
                        .child(userData.get(position).getId())
                        .setValue(null);
                blockedRef
                        .child("blocked_user")
                        .child(userData.get(position).getId())
                        .child(mAuth.getCurrentUser().getUid())
                        .setValue(null);
                Toast.makeText(view.getContext(), "차단 해제", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
    public void addItem(BlockUserData data) {
        userData.add(data);
    }
    public void removeItem() {
        userData.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView blockUserName;
        private TextView blockBtn;
        private ImageView blockUserImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blockUserName = itemView.findViewById(R.id.block_user_name);
            blockUserImage = itemView.findViewById(R.id.block_user_profile);
            blockBtn = itemView.findViewById(R.id.block);
        }
        void onBind(BlockUserData data) {
            blockUserName.setText(data.getName());
            Glide.with(itemView.getContext()).load(data.getProfile()).into(blockUserImage);
        }
    }
}
