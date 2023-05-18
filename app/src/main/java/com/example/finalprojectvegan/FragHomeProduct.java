package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Model.ProductData;
import com.example.finalprojectvegan.Model.ProductItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragHomeProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragHomeProduct extends Fragment {
    ProductAdapter adapter;
    ProductItem pList;
    List<ProductData> pInfo;
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragHomeProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragHomeProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static FragHomeProduct newInstance(String param1, String param2) {
        FragHomeProduct fragment = new FragHomeProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static FragHomeProduct newInstance() {
        return new FragHomeProduct();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_product, container, false);
//        pInfo = new ArrayList<>();
//        recyclerView = view.findViewById(R.id.product_recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        ProductApiInterface apiInterface = NaverMapRequest.getClient().create(ProductApiInterface.class);
//        Call<ProductItem> call = apiInterface.getProductData();
//        call.enqueue(new Callback<ProductItem>(){
//            @Override
//            public void onResponse(Call<ProductItem> call, Response<ProductItem> response) {
//                pList = response.body();
//                pInfo = pList.PRODUCT;
//
//                adapter = new ProductAdapter(getContext(), pInfo);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<ProductItem> call, Throwable t) {
//                Log.d("FragHomeProduct", t.toString());
//            }
//        });
        return view;
    }
}