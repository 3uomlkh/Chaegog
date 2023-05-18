package com.example.finalprojectvegan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.Model.MapData;
import com.example.finalprojectvegan.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapBookmarkAdapter extends RecyclerView.Adapter<MapBookmarkAdapter.ViewHolder>{

    private ArrayList<MapData> listData = new ArrayList<>();
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public MapBookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        return new MapBookmarkAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MapBookmarkAdapter.ViewHolder holder, int i) {
        holder.onBind(listData.get(i));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        String itemKey = listData.get(i).getItemKeyList();
        ArrayList<String> bookmarkIdList = listData.get(i).getBookmarkIdList();
        Log.d("MapBookmarkRVA", "itemKey : " + itemKey);
        Log.d("MapBookmarkRVA", "bookmarkList : " + bookmarkIdList.toString());


        if (bookmarkIdList.contains(itemKey)) {
            holder.saveImage.setImageResource(R.drawable.favorite_on);
        } else {
            holder.saveImage.setImageResource(R.drawable.favorite_off);
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookmarkRef = database.getReference("bookmark");
        holder.saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkRef
                        .child(mAuth.getCurrentUser().getUid())
                        .child("map_bookmark")
                        .child(itemKey)
                        .setValue(null);
                Toast.makeText(view.getContext(), "북마크 삭제", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public void addItem(MapData data) {
        listData.add(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView addr;
        private ImageView thumbnail;
        private ImageView saveImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookmark_title);
            addr = itemView.findViewById(R.id.bookmark_addr);
            thumbnail = itemView.findViewById(R.id.bookmark_image);
            saveImage = itemView.findViewById(R.id.saveImage);
        }
        void onBind(MapData data) {
            title.setText(data.getName());
            addr.setText(data.getAddress());
            Glide.with(itemView.getContext()).load(data.getImageUrl()).into(thumbnail);
        }
    }

}
