package com.cchaegog.chaegog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cchaegog.chaegog.Model.MapData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>{
    private ArrayList<MapData> listData = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String name, addr, category, image, phone;
    public int position;

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int i) {
        holder.onBind(listData.get(i));

        position = holder.getAdapterPosition();

        name = listData.get(i).getName();
        addr = listData.get(i).getAddress();
        category = listData.get(i).getCategory();
        image = listData.get(i).getImageUrl();
        phone = listData.get(i).getPhone().replaceAll("-", "");

        String itemKey = listData.get(i).getItemKeyList();
        ArrayList<String> bookmarkIdList = listData.get(i).getBookmarkIdList();

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

        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent intent = new Intent(context, MapButtonActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("addr", addr);
                intent.putExtra("key", itemKey);
                context.startActivity(intent);
            }
        });

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse( "tel:" + phone));
                context.startActivity(dialIntent);
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
        private Button mapBtn, callBtn;
        private TextView name, category, menu, addr, time, dayoff, dayoffTv;
        private ImageView saveImage, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mapBtn = itemView.findViewById(R.id.map_btn);
            callBtn = itemView.findViewById(R.id.call_btn);
            name = itemView.findViewById(R.id.map_info_detail_name);
            image = itemView.findViewById(R.id.map_info_detail_image);
            category = itemView.findViewById(R.id.map_info_detail_category);
            menu = itemView.findViewById(R.id.map_info_detail_menu);
            addr = itemView.findViewById(R.id.map_info_detail_addr);
            time = itemView.findViewById(R.id.map_info_detail_time);
            dayoff = itemView.findViewById(R.id.map_info_detail_dayoff);
            dayoffTv =itemView.findViewById(R.id.map_info_detail_dayoff_tv);
            saveImage = itemView.findViewById(R.id.map_info_save_image);
        }
        void onBind(MapData data) {
            name.setText(data.getName());
            category.setText(data.getCategory());
            menu.setText(data.getMenu());
            addr.setText(data.getAddress());
            time.setText(data.getTime());

            if(data.getDayoff().trim().isEmpty()) {
                dayoffTv.setVisibility(View.GONE);
            } else {
                dayoffTv.setVisibility(View.VISIBLE);
            }
            dayoff.setText(data.getDayoff());

//            Glide.with(itemView.getContext())
//                    .load(data.getImageUrl())
//                    .apply(new RequestOptions().transform(new CenterCrop(),
//                            new RoundedCorners(10)))
//                    .into(image);
            image.setImageResource(R.drawable.chaegog_restaurant);
            if(data.getCategory().equals("까페") || data.getCategory().equals("카페")) {
                image.setImageResource(R.drawable.chaegog_cafe);
            }
            if(data.getCategory().equals("베이커리")) {
                image.setImageResource(R.drawable.chaegog_bakery);
            }

        }
    }
}
