package com.example.pabji.siftapplication.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private String url = "";
    private String latitude = "";
    private String longitude = "";
    private String name;
    private ImageView ivBuild;
    private TextView tvName;
    private FloatingActionButton fbMap;
    private String url_image;
    private String description;
    private String intro;
    private ProgressBar loading;
    private Toolbar toolbar;
    private CoordinatorLayout appBarLayout;
    private TextView textIntro;

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(String name, String description, String latitude, String longitude, String url_image,String intro) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        args.putString("url_image", url_image);
        args.putString("intro",intro);
        secondFragment.setArguments(args);
        return secondFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUIControls(view);
        setSizeAppBarLayout();
        configureView();

        //loading.setVisibility(View.VISIBLE);

    }

    public void setSizeAppBarLayout(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            params.width = params.MATCH_PARENT;
            appBarLayout.setLayoutParams(params);
        }
    }


    private void configureView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments() != null) {
            description = (String) getArguments().get("description");
            latitude = (String) getArguments().get("latitude");
            longitude = (String) getArguments().get("longitude");
            name = (String) getArguments().get("name");
            url_image = (String) getArguments().get("url_image");
            intro = (String) getArguments().get("intro");
            getActivity().setTitle(name);
            textIntro.setText(intro);
            textIntro.setMovementMethod(new ScrollingMovementMethod());
        }

        if (url_image != null) {
            Picasso.with(getActivity()).load(url_image).into(ivBuild);
//            loading.setVisibility(View.GONE);
        }

        fbMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q=search/monumentos+de+salamanca"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUIControls(View view) {
        fbMap = (FloatingActionButton) view.findViewById(R.id.fb_map);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        textIntro = (TextView) view.findViewById(R.id.intro);
        //loading = (ProgressBar) view.findViewById(R.id.pb_second);
        ivBuild = (ImageView) view.findViewById(R.id.photo);
    }
}
