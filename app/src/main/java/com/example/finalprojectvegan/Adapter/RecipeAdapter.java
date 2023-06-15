package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private ArrayList<RecipeData> listData = new ArrayList<>();
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeAdapter.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int i) {
        holder.onBind(listData.get(i));
        String defaultUrl = "https://www.10000recipe.com";
        String url = listData.get(i).getClickUrl();
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(defaultUrl + url));
                context.startActivity(intent);
            }
        });

        String title = listData.get(i).getTitle();
        String image = listData.get(i).getImageUrl();
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
                if(bookmarkIdList.contains(itemKey)) {
                    bookmarkRef
                            .child(mAuth.getCurrentUser().getUid())
                            .child("recipe_bookmark")
                            .child(itemKey)
                            .setValue(null);
                    Toast.makeText(view.getContext(), "북마크 삭제", Toast.LENGTH_SHORT).show();
                } else {
                    bookmarkRef
                            .child(mAuth.getCurrentUser().getUid())
                            .child("recipe_bookmark")
                            .child(itemKey)
                            .setValue(new RecipeData(image, title, url));
                    Toast.makeText(view.getContext(), "북마크 저장", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return listData.size();
    }

    public void addItem(RecipeData data) {
        listData.add(data);
    }

    public void removeItem() {
        listData.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView thumbnail;
        private ImageView saveImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            thumbnail = itemView.findViewById(R.id.recipe_image);
            saveImage = itemView.findViewById(R.id.recipe_save_image);
        }
        void onBind(RecipeData data) {
            title.setText(data.getTitle());
            Glide.with(itemView.getContext()).load(data.getImageUrl()).into(thumbnail);
        }
    }
}
