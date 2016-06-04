package com.example.pabji.siftapplication.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.activities.DescriptionActivity;
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
    private FloatingActionButton fbInfo;
    private String url_image;
    private String description;
    private ProgressBar loading;

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(String name, String description, String latitude, String longitude, String url_image) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        args.putString("url_image", url_image);
        secondFragment.setArguments(args);
        return secondFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        getUIControls(view);
        loading.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            description = (String) getArguments().get("description");
            latitude = (String) getArguments().get("latitude");
            longitude = (String) getArguments().get("longitude");
            name = (String) getArguments().get("name");
            url_image = (String) getArguments().get("url_image");

        }

        if (url_image != null) {
            Picasso.with(getActivity()).load(url_image).into(ivBuild);
            loading.setVisibility(View.GONE);
        }

        fbInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DescriptionActivity.class);
                intent.putExtra("description", description);
                startActivity(intent);
            }
        });

        fbMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q=search/monumentos+de+salamanca"));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getUIControls(View view) {
        fbMap = (FloatingActionButton) view.findViewById(R.id.fb_map);
        fbInfo = (FloatingActionButton) view.findViewById(R.id.fb_info);
        loading = (ProgressBar) view.findViewById(R.id.pb_second);
        ivBuild = (ImageView) view.findViewById(R.id.iv_second);
    }
}
