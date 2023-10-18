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

import com.cchaegog.chaegog.Adapter.ProductAdapter;
import com.cchaegog.chaegog.Adapter.ProductAdapter;
import com.cchaegog.chaegog.Model.ProductData;
import com.cchaegog.chaegog.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
public class FragHomeProduct extends Fragment {

    private ArrayList<String> productCompany = new ArrayList<>();
    private ArrayList<String> productName = new ArrayList<>();
    private ArrayList<String> productImg = new ArrayList<>();
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private Document doc;

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


//        getBookmark();
//        getproductData();
        getData();


        return view;
    }

    private void getproductData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    productName.add(snapshot.child("productName").getValue().toString());
                    productCompany.add(snapshot.child("productCompany").getValue().toString());
                    productImg.add(snapshot.child("productImg").getValue().toString());
                    itemKeyList.add(snapshot.getKey());
                }

                for (int k = 0; k < productName.size(); k++) {

                    ProductData data = new ProductData(productName.get(k), productCompany.get(k), productImg.get(k));
                    data.setBookmarkIdList(bookmarkIdList);
                    data.setItemKey(itemKeyList.get(k));

                    adapter.addItem(data);
                }

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ProductFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getBookmark() {
        mDatabase = FirebaseDatabase.getInstance().getReference("product");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                }

                Log.d("productIdList", bookmarkIdList.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ProductFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("product_bookmark").addValueEventListener(postListener);
    }

    private void getData() {
        FragHomeProduct.JsoupAsyncTask jsoupAsyncTask = new FragHomeProduct.JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String urlString = "https://altist.com/category/%EA%B3%A0%EA%B8%B0%EB%8C%80%EC%8B%A0/23/";
                doc = Jsoup.connect(urlString).get();

                final Elements title = doc.select("div ul li div[class=description] a > span[style$=;]");
                final Elements image = doc.select("div ul li div[class=thumbnail] div[class=prdImg] a > img");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        for (Element element : title) {
                            productName.add(element.text());
                            productCompany.add("고기대신");
                        }

                        for (Element element : image) {
                            String img = element.attr("src").substring(13);
                            productImg.add("https://altist.com/" + img);
                        }

                        for (int i=0; i<productName.size(); i++) {
                            Log.d("ProductName", "[제품명] " + productName.get(i));
                            Log.d("ProductImg", "[URL] " + productImg.get(i+1));
                            mDatabase = FirebaseDatabase.getInstance().getReference("product");
                            if(mDatabase.child("ProductCompany").equals("고기대신")) {
                                mDatabase.setValue(null);
                            }
//                            mDatabase
//                                    .push()
//                                    .setValue(new ProductData(productName.get(i), productCompany.get(i), productImg.get(i)));
//
//
//                            ProductData data = new ProductData(productName.get(i), productCompany.get(i), productImg.get(i));
//
//                            adapter.addItem(data);

                        }
//                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}