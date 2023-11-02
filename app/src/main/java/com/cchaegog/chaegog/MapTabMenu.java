package com.cchaegog.chaegog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchaegog.chaegog.Adapter.MenuAdapter;

public class MapTabMenu extends Fragment {
    String menu;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MenuAdapter adapter;
    public MapTabMenu() {
        // Required empty public constructor
    }

    public static MapTabMenu newInstance(String param1, String param2) {
        MapTabMenu fragment = new MapTabMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_tab_menu, container, false);
        recyclerView = view.findViewById(R.id.menu_recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        menu = bundle.getString("menu");

        String[] menuArr = menu.split(",");

        adapter = new MenuAdapter(menuArr);
        recyclerView.setAdapter(adapter);

        return view;
    }
}