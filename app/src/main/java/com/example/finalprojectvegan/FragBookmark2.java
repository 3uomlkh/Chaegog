package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// 제품 북마크
public class FragBookmark2 extends Fragment {
    private View view;
    public FragBookmark2() {

    }

    public static FragBookmark2 newInstance(String param1, String param2) {
        FragBookmark2 fragment = new FragBookmark2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_bookmark2, container, false);

        return view;
    }
}