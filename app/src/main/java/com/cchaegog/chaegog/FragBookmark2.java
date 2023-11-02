package com.cchaegog.chaegog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchaegog.chaegog.Model.ProductData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 제품 북마크
public class FragBookmark2 extends Fragment {
    private View view;
    private ArrayList<String> listName, listCompany,  bookmarkIdList, itemKey;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private ProductBookmarkAdapter adapter;
    public FragBookmark2() {

    }

    public static FragBookmark2 newInstance(String param1, String param2) {
        FragBookmark2 fragment = new FragBookmark2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkIdList = new ArrayList<>();
        listName = new ArrayList<>();
        listCompany = new ArrayList<>();
        itemKey = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_frag_bookmark2, container, false);

        recyclerView = view.findViewById(R.id.bookmark2_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductBookmarkAdapter();

        getBookmark();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getBookmark() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listName.clear();
                listCompany.clear();
                bookmarkIdList.clear();
                itemKey.clear();
                adapter.removeItem();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                    itemKey.add(snapshot.child("itemKey").getValue().toString());
                    listName.add(snapshot.child("productName").getValue().toString());
                    listCompany.add(snapshot.child("productCompany").getValue().toString());
                }

                for(int i=0; i<listName.size(); i++) {
                    ProductData data = new ProductData(listName.get(i), listCompany.get(i), itemKey.get(i));
                    data.setBookmarkIdList(bookmarkIdList);
                    adapter.addItem(data);
                }

                adapter.notifyItemRemoved(adapter.position);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("product_bookmark").addValueEventListener(postListener);
    }
}