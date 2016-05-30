package com.example.pabji.siftapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.fragments.DescriptionFragment;
import com.example.pabji.siftapplication.fragments.WebviewFragment;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Bundle b = getIntent().getExtras();

        String name = b.getString("description");
        String latitude = b.getString("latitude");
        String longitude = b.getString("longitude");
        String id = b.getString("id");
        String url_image = b.getString("url_image");

        if (savedInstanceState == null) {
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(name, id,latitude, longitude,url_image);
            getSupportFragmentManager().beginTransaction().replace(R.id.flRecipe,descriptionFragment).commit();
        }
    }
}
