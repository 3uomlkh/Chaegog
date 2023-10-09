package com.cchaegog.chaegog;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchaegog.chaegog.Adapter.RecipeAdapter;
import com.cchaegog.chaegog.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.SAXException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class FragHomeRecipe extends Fragment {
    private ArrayList<String> listTitle = new ArrayList<>();
    private ArrayList<String> listThumb = new ArrayList<>();
    private ArrayList<String> clickUrl = new ArrayList<>();
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    RecipeAdapter adapter;
    Document doc;

    public static FragHomeRecipe newInstance(String param1, String param2) throws IOException, ParserConfigurationException, SAXException {
        FragHomeRecipe fragment = new FragHomeRecipe();

        return fragment;
    }
    public static FragHomeRecipe newInstance() {
        return new FragHomeRecipe();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_recipe, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recipe_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecipeAdapter();

        getBookmark();
        getRecipeData();
        //getData();

        return view;
    }

    private void getRecipeData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("recipe");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listTitle.add(snapshot.child("title").getValue().toString());
                    listThumb.add(snapshot.child("imageUrl").getValue().toString());
                    clickUrl.add(snapshot.child("clickUrl").getValue().toString());
                }

                for (int k = 0; k < listTitle.size(); k++) {

                    RecipeData data = new RecipeData(listThumb.get(k), listTitle.get(k), clickUrl.get(k));
                    data.setBookmarkIdList(bookmarkIdList);

                    adapter.addItem(data);
                }

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getBookmark() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                }

                Log.d("bookmarkIdList", bookmarkIdList.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("recipe_bookmark").addValueEventListener(postListener);
    }

    private void getData() {
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for(int i=1; i<9; i++) {
                    String urlString = "https://www.10000recipe.com/recipe/list.html?q=비건&order=reco&page=";
                    String page = String.valueOf(i);
                    urlString += page;
                    doc = Jsoup.connect(urlString).get();

                    final Elements title = doc.select("div div[class=common_sp_caption_tit line2]");
                    final Elements thumb = doc.select("div[class=common_sp_thumb] a > img");
                    final Elements click = doc.select("div[class=common_sp_thumb] a");
                    Handler handler = new Handler(Looper.getMainLooper());
                    int finalI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (Element element : title) {
                                listTitle.add(element.text());
                            }
                            for (Element element : thumb) {
                                String thumbUrl = element.attr("src");
                                listThumb.add(thumbUrl);
                            }
                            for (Element element : click) {
                                String detailUrl = element.attr("href");
                                clickUrl.add(detailUrl);
                            }
                            for (int k = 0; k < listTitle.size(); k++) {
                                // 모든 레시피 firebase db에 저장, 최초에 1번 실행
//                                if(finalI == 8) {
//                                    mDatabase = FirebaseDatabase.getInstance().getReference("recipe");
//                                    mDatabase
//                                            .push()
//                                            .setValue(new RecipeData(listThumb.get(k), listTitle.get(k), clickUrl.get(k)));
//                                }

                                RecipeData data = new RecipeData();

                                data.setTitle(listTitle.get(k));
                                data.setImageUrl(listThumb.get(k));
                                data.setClickUrl(clickUrl.get(k));
                                data.setBookmarkIdList(bookmarkIdList);

                                adapter.addItem(data);
                            }

                            Log.d("recipeDataSize", "listTitle : " + listTitle.size() + "");
                            Log.d("recipeDataSize", "listThumb : " + listThumb.size() + "");
                            Log.d("recipeDataSize", "bookmarkIdList : " + bookmarkIdList + "");

                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}