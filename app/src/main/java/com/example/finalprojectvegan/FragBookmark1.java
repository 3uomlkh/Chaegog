package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragBookmark1 extends Fragment {
    private View view;
    public FragBookmark1() {

    }

    public static FragBookmark1 newInstance(String param1, String param2) {
        FragBookmark1 fragment = new FragBookmark1();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_bookmark1, container, false);

        return view;
    }
}