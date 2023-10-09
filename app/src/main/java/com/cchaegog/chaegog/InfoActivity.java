package com.cchaegog.chaegog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView Btn_info_provision, Btn_info_pim, Btn_info_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Btn_info_provision = findViewById(R.id.Btn_info_provision);
        Btn_info_pim = findViewById(R.id.Btn_info_pim);
        Btn_info_source = findViewById(R.id.Btn_info_source);

        Btn_info_provision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://picturesque-sing-211.notion.site/0a5554bb746e4cc2aeffc8238df82f59"));
                startActivity(intent);
            }
        });

        Btn_info_pim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://picturesque-sing-211.notion.site/38fff2620b3541ae8361683f8afdc223"));
                startActivity(intent);
            }
        });

        Btn_info_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}