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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cchaegog.chaegog.Model.ProductData;
import com.cchaegog.chaegog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<ProductData> productData = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private FirebaseAuth mAuth;
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.onBind(productData.get(i));

        String name = productData.get(i).getProductName();
        String company = productData.get(i).getProductCompany();

        String itemKey = productData.get(i).getItemKey();
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
                            .child("product_bookmark")
                            .child(itemKey)
                            .setValue(null);
                    Toast.makeText(view.getContext(), "북마크 삭제", Toast.LENGTH_SHORT).show();
                } else {
                    bookmarkRef
                            .child(mAuth.getCurrentUser().getUid())
                            .child("product_bookmark")
                            .child(itemKey)
                            .setValue(new ProductData(name, company, itemKey));
                    Toast.makeText(view.getContext(), "북마크 저장", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }
    public void addItem(ProductData data) {
        productData.add(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productCompany;
        private ImageView saveImage;
        private ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.ProductNameTv);
            productCompany = itemView.findViewById(R.id.ProductCompanyTv);
            saveImage = itemView.findViewById(R.id.product_save_image);
            thumbnail = itemView.findViewById(R.id.ProductImageView);
        }
        void onBind(ProductData data) {
            productName.setText(data.getProductName());
            productCompany.setText(data.getProductCompany());
            Glide.with(itemView.getContext())
                    .load(data.getProductImg())
                    .override(200,200)
                    .into(thumbnail);
        }
    }
}
