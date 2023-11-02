package com.cchaegog.chaegog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cchaegog.chaegog.Model.ProductData;
import com.bumptech.glide.Glide;
import com.cchaegog.chaegog.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductBookmarkAdapter extends RecyclerView.Adapter<ProductBookmarkAdapter.ViewHolder> {
    private ArrayList<ProductData> productData = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private FirebaseAuth mAuth;
    int position;

    @NonNull
    @Override
    public ProductBookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductBookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.onBind(productData.get(i));

        holder.saveImage.setImageResource(R.drawable.favorite_on);

        ArrayList<String> bookmarkIdList = productData.get(i).getBookmarkIdList();
        String itemKey = productData.get(i).getItemKey();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookmarkRef = database.getReference("bookmark");
        holder.saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = holder.getAdapterPosition();
                bookmarkRef
                        .child(mAuth.getCurrentUser().getUid())
                        .child("product_bookmark")
                        .child(itemKey)
                        .setValue(null);
                Toast.makeText(view.getContext(), "북마크 삭제", Toast.LENGTH_SHORT).show();
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
    public void removeItem() {
        productData.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productCompany;
        private ImageView saveImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.ProductNameTv);
            productCompany = itemView.findViewById(R.id.ProductCompanyTv);
            saveImage = itemView.findViewById(R.id.product_save_image);
        }
        void onBind(ProductData data) {
            productName.setText(data.getProductName());
            productCompany.setText(data.getProductCompany());
        }
    }
}
