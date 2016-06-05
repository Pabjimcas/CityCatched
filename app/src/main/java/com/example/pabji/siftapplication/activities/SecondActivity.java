package com.example.pabji.siftapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.fragments.SecondFragment;

public class SecondActivity extends AppCompatActivity {

    private LinearLayout contentFrame;
    private Button fbInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle b = getIntent().getExtras();
        final String description = b.getString("description");
        String latitude = b.getString("latitude");
        String longitude = b.getString("longitude");
        String url_image = b.getString("url_image");
        String name = b.getString("name");
        String intro = b.getString("intro");
        fbInfo = (Button) findViewById(R.id.fb_info);

        fbInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,WebActivity.class);
                intent.putExtra("url", description);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {

            SecondFragment secondFragment = SecondFragment.newInstance(name, description, latitude, longitude, url_image,intro);
            getFragmentManager().beginTransaction().replace(R.id.content, secondFragment).commit();
        }
    }

}