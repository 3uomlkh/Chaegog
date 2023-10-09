package com.cchaegog.chaegog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BookmarkActivity extends AppCompatActivity {
    String[] items = {"식당", "제품", "레시피"};
    Fragment bookmark1;
    Fragment bookmark2;
    Fragment bookmark3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmark1 = new FragBookmark1();
        bookmark2 = new FragBookmark2();
        bookmark3 = new FragBookmark3();

        Spinner spinner = findViewById(R.id.bookmarkSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (items[i]) {
                    case "식당":
                        getSupportFragmentManager().beginTransaction() .replace(R.id.bookmark_layout,bookmark1).commitAllowingStateLoss();
                        return;
                    case "제품":
                        getSupportFragmentManager().beginTransaction().replace(R.id.bookmark_layout, bookmark2).commitAllowingStateLoss();
                        return;
                    case "레시피":
                        getSupportFragmentManager().beginTransaction().replace(R.id.bookmark_layout, bookmark3).commitAllowingStateLoss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, bookmark1).commitAllowingStateLoss();
            }
        });
    }
}