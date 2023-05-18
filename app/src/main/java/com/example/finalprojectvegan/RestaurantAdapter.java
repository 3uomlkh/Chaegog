package com.example.finalprojectvegan;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.Model.MapData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>{
    private ArrayList<MapData> listData = new ArrayList<>();
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int i) {
        holder.onBind(listData.get(i));

        String name = listData.get(i).getName();
        String addr = listData.get(i).getAddress();
        String category = listData.get(i).getCategory();
        String image = listData.get(i).getImageUrl();

        String itemKey = listData.get(i).getItemKeyList();
        ArrayList<String> bookmarkIdList = listData.get(i).getBookmarkIdList();
        Log.d("RestRVA", "name : " + name);
        Log.d("RestRVA", "itemKey : " + itemKey);
        Log.d("RestRVA", "bookmarkList : " + bookmarkIdList.toString());


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

    public void addItem(MapData data) {
        listData.add(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView category;
        private TextView menu;
        private TextView addr;
        private TextView time;
        private TextView dayoff;
        private ImageView saveImage;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.map_info_detail_name);
            image = itemView.findViewById(R.id.map_info_detail_image);
            category = itemView.findViewById(R.id.map_info_detail_category);
            menu = itemView.findViewById(R.id.map_info_detail_menu);
            addr = itemView.findViewById(R.id.map_info_detail_addr);
            time = itemView.findViewById(R.id.map_info_detail_time);
            dayoff = itemView.findViewById(R.id.map_info_detail_dayoff);
            saveImage = itemView.findViewById(R.id.map_info_save_image);
        }
        void onBind(MapData data) {
            name.setText(data.getName());
            category.setText(data.getCategory());
            menu.setText(data.getMenu());
            addr.setText(data.getAddress());
            time.setText(data.getTime());
            dayoff.setText(data.getDayoff());
            Glide.with(itemView.getContext())
                    .load(data.getImageUrl())
                    .apply(new RequestOptions().transform(new CenterCrop(),
                            new RoundedCorners(10)))
                    .into(image);
        }
    }
}
