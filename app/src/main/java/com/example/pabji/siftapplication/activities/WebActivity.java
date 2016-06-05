package com.example.pabji.siftapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.fragments.WebFragment;

public class WebActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            url = intent.getStringExtra("url");
            loadFragment();
        }
    }

    private void loadFragment() {
        WebFragment webFragment = WebFragment.newInstance(url);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_web,webFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
