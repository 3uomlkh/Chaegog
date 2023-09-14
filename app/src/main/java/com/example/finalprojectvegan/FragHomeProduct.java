package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Adapter.ProductAdapter;
import com.example.finalprojectvegan.Adapter.RecipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class FragHomeProduct extends Fragment {

    private ArrayList<String> productCompany = new ArrayList<>();
    private ArrayList<String> productName = new ArrayList<>();
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ProductAdapter adapter;

    public FragHomeProduct() {
    }

    public static FragHomeProduct newInstance(String param1, String param2) {
        FragHomeProduct fragment = new FragHomeProduct();
        return fragment;
    }
    public static FragHomeProduct newInstance() {
        return new FragHomeProduct();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_product, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.product_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

//        getBookmark();


        return view;
    }


    private void getBookmark() {
//        mDatabase = FirebaseDatabase.getInstance().getReference("product");
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                bookmarkIdList.clear();
//
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    bookmarkIdList.add(snapshot.getKey());
//                }
//
//                Log.d("productIdList", bookmarkIdList.toString());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("ProductFragment", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mDatabase.child(mAuth.getCurrentUser().getUid()).child("product_bookmark").addValueEventListener(postListener);
    }
}