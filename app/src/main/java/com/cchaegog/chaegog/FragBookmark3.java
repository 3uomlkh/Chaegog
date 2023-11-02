package com.cchaegog.chaegog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchaegog.chaegog.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 레시피 북마크
public class FragBookmark3 extends Fragment {
    private View view;
    private ArrayList<String> listTitle, listThumb, clickUrl, bookmarkIdList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private RecipeBookmarkAdapter adapter;
    public FragBookmark3() {

    }

    public static FragBookmark3 newInstance(String param1, String param2) {
        FragBookmark3 fragment = new FragBookmark3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkIdList = new ArrayList<>();
        listTitle = new ArrayList<>();
        listThumb = new ArrayList<>();
        clickUrl = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_frag_bookmark3, container, false);

        recyclerView = view.findViewById(R.id.bookmark3_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecipeBookmarkAdapter();

        getBookmark();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getBookmark() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();
                listTitle.clear();
                listThumb.clear();
                clickUrl.clear();
                adapter.removeItem();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) { // 북마크 된 레시피의 제목, 썸네일, url, key를 불러와 각각의 ArrayList에 저장
                    bookmarkIdList.add(snapshot.getKey());
                    listTitle.add(snapshot.child("title").getValue().toString());
                    listThumb.add(snapshot.child("imageUrl").getValue().toString());
                    clickUrl.add(snapshot.child("clickUrl").getValue().toString());
                }

                for(int i=0; i<listTitle.size(); i++) {
                    RecipeData data = new RecipeData(listThumb.get(i), listTitle.get(i), clickUrl.get(i));
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
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("recipe_bookmark").addValueEventListener(postListener);
    }
}