package com.example.finalprojectvegan;

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

import com.example.finalprojectvegan.Adapter.ProductAdapter;
import com.example.finalprojectvegan.Model.ProductData;
import com.example.finalprojectvegan.Model.RecipeData;
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


        getBookmark();
        getproductData();
//        getData();


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
                    itemKeyList.add(snapshot.getKey());
                }

                for (int k = 0; k < productName.size(); k++) {

                    ProductData data = new ProductData(productName.get(k), productCompany.get(k), itemKeyList.get(k));
                    data.setBookmarkIdList(bookmarkIdList);

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
                String urlString = "https://lovinghut.co.kr/product/list.html?cate_no=43";
                doc = Jsoup.connect(urlString).get();

                final Elements title = doc.select("div div[class=box] a > span[style$=;]");
                final Elements image = doc.select("div div[class=box] p[class=prdImg] a img[src$=jpg]"); // 왜 안되죠???

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (Element element : title) {
                            productName.add(element.text());
                            productCompany.add("러빙헛");
                            productImg.add("");
                        }

//                        for (Element element : image) {
//                            productImg.add(element.text());
//                        }

                        for (int i=0; i<productName.size(); i++) {
                            Log.d("ProductName", "[리빙헛] " + productImg.get(i));

                            mDatabase = FirebaseDatabase.getInstance().getReference("product");
                            mDatabase
                                    .push()
                                    .setValue(new ProductData(productName.get(i), productCompany.get(i), productImg.get(i)));


                            ProductData data = new ProductData(productName.get(i), productCompany.get(i), "");

                            adapter.addItem(data);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}