package com.example.finalprojectvegan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalprojectvegan.Model.PhotoActivity;
import com.example.finalprojectvegan.R;
import com.example.finalprojectvegan.Model.WriteReviewInfo;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WriteReviewInfo> mDataset;

    public GalleryAdapter(Context context, ArrayList<WriteReviewInfo> mDataset) {
        this.context = context;
        this.mDataset = mDataset;
    }
    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);


        return new GalleryAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {

        String imgURL = mDataset.get(position).getImagePath1();
        Glide.with(holder.itemView)
                .load(imgURL)
                .override(150,150)
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("imgUrl", imgURL);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.gallery_item_view);
        }
    }
}
