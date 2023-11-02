package com.cchaegog.chaegog.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.cchaegog.chaegog.MapInfoActivity;
import com.cchaegog.chaegog.Model.MapData;
import com.cchaegog.chaegog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapSearchAdapter extends RecyclerView.Adapter<MapSearchAdapter.ViewHolder>{
    private ArrayList<MapData> listData = new ArrayList<>();
    private FirebaseAuth mAuth;
    public int position;

    @NonNull
    @Override
    public MapSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        return new MapSearchAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MapSearchAdapter.ViewHolder holder, int i) {
        holder.onBind(listData.get(i));
        position = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                int i = holder.getAdapterPosition();
                Intent intent = new Intent(context, MapInfoActivity.class);
                intent.putExtra("name", listData.get(i).getName());
                intent.putExtra("addr", listData.get(i).getAddress());
                intent.putExtra("time", listData.get(i).getTime());
                intent.putExtra("dayOff", listData.get(i).getTime());
                intent.putExtra("image", listData.get(i).getImageUrl());
                intent.putExtra("category", listData.get(i).getCategory());
                intent.putExtra("menu", listData.get(i).getMenu());
                intent.putExtra("phone", listData.get(i).getPhone());
                intent.putExtra("key", listData.get(i).getItemKeyList());
                context.startActivity(intent);
            }
        });

        String name = listData.get(i).getName();
        String addr = listData.get(i).getAddress();
        String category = listData.get(i).getCategory();
        String image = listData.get(i).getImageUrl();
        String itemKey = listData.get(i).getItemKeyList();
        ArrayList<String> bookmarkIdList = listData.get(i).getBookmarkIdList();
        Log.d("MapSearchRVA", "itemKey : " + itemKey);
        Log.d("MapSearchRVA", "bookmarkList : " + bookmarkIdList.toString());

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
                if (bookmarkIdList.contains(itemKey)) {
                    bookmarkRef
                            .child(mAuth.getCurrentUser().getUid())
                            .child("map_bookmark")
                            .child(itemKey)
                            .setValue(null);
                    Toast.makeText(view.getContext(), "북마크 삭제", Toast.LENGTH_SHORT).show();
                } else {
                    bookmarkRef
                            .child(mAuth.getCurrentUser().getUid())
                            .child("map_bookmark")
                            .child(itemKey)
                            .setValue(new MapData(name, addr, category, image));
                    Toast.makeText(view.getContext(), "북마크 저장", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return listData.size();
    }
    public void removeItem() {
        listData.clear();
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
            thumbnail.setImageResource(R.drawable.chaegog_restaurant);
            if(data.getCategory().equals("까페") || data.getCategory().equals("카페")) {
                thumbnail.setImageResource(R.drawable.chaegog_cafe);
            }
            if(data.getCategory().equals("베이커리")) {
                thumbnail.setImageResource(R.drawable.chaegog_bakery);
            }
//            Glide.with(itemView.getContext()).load(data.getImageUrl()).into(thumbnail);
        }
    }

}