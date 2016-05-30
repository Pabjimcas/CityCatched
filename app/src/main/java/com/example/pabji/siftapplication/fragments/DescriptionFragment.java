package com.example.pabji.siftapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.activities.DescriptionActivity;
import com.example.pabji.siftapplication.activities.MainActivity;
import com.example.pabji.siftapplication.adapters.DescriptionListAdapter;
import com.example.pabji.siftapplication.adapters.ItemListAdapter;
import com.example.pabji.siftapplication.models.Building;
import com.example.pabji.siftapplication.models.Description;
import com.example.pabji.siftapplication.utils.Utilities;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pabji on 30/05/2016.
 */
public class DescriptionFragment extends Fragment {

    @Bind(R.id.name)
    TextView buildingName;
    @Bind(R.id.photo)
    ImageView photo;
    @Bind(R.id.description_rv)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton map_button;
    private String name;
    private String id;
    private String latitude;
    private String longitude;
    private String urlImage;
    private DescriptionListAdapter adapter;

    public DescriptionFragment() {
    }

    public static DescriptionFragment newInstance(String name, String id, String latitude, String longitude,String urlImage) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("id", id);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        args.putString("url_image",urlImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        id = getArguments().getString("id");
        latitude = getArguments().getString("latitude");
        longitude = getArguments().getString("longitude");
        urlImage = getArguments().getString("url_image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        buildingName.setText(name);
        Picasso.with(getContext()).load(urlImage).into(photo);
        Firebase mref = new Firebase("https://city-catched.firebaseio.com/descriptions/"+id+"/description");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Description> listDescription = new ArrayList<Description>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String title = child.getKey();
                    String content = child.getValue(String.class);
                    listDescription.add(new Description(title,content));
                }
                adapter = new DescriptionListAdapter(listDescription);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
