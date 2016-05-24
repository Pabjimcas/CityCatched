package com.example.pabji.siftapplication.description;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.description.WebviewFragment;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        if (savedInstanceState == null) {
            WebviewFragment webFragmentFragment = WebviewFragment.newInstance((String) getIntent().getExtras().get("url"));
            getFragmentManager().beginTransaction().add(android.R.id.content, webFragmentFragment).commit();
        }
    }
}
