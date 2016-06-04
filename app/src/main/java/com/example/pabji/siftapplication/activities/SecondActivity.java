package com.example.pabji.siftapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.fragments.SecondFragment;

public class SecondActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        contentFrame = (LinearLayout) findViewById(R.id.content_frame_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle b = getIntent().getExtras();
        String description = b.getString("description");
        String latitude = b.getString("latitude");
        String longitude = b.getString("longitude");
        String url_image = b.getString("url_image");
        String name = b.getString("name");

        setSupportActionBar(mToolbar);
        setTitle(name);
        if (savedInstanceState == null) {

            SecondFragment secondFragment = SecondFragment.newInstance(name, description, latitude, longitude, url_image);
            getFragmentManager().beginTransaction().replace(R.id.content_frame_detail, secondFragment).commit();
        }
    }

}