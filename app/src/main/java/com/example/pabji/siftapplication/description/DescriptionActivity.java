package com.example.pabji.siftapplication.description;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pabji.siftapplication.R;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        if (savedInstanceState == null) {
            WebviewFragment webFragmentFragment = WebviewFragment.newInstance((String) getIntent().getExtras().get("description"));
            getFragmentManager().beginTransaction().add(android.R.id.content, webFragmentFragment).commit();
        }
    }
}
