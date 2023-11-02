package com.cchaegog.chaegog.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cchaegog.chaegog.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{
    private String[] menuName;
    MenuHolder menuHolder;

    public MenuAdapter(String[] menuName) {
        this.menuName = menuName;
    }

    public static class MenuHolder extends RecyclerView.ViewHolder{
        public TextView menuName;
        public MenuHolder(View view){
            super(view);
            this.menuName = view.findViewById(R.id.menu_name);
        }
    }
    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        menuHolder = new MenuHolder(holderView);

        return menuHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MenuHolder menuHolder, int i) {
        menuHolder.menuName.setText(this.menuName[i]);
    }

    @Override
    public int getItemCount() {

        return menuName.length;
    }
}
