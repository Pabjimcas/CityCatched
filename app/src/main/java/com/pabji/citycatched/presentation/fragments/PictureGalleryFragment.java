package com.pabji.citycatched.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class PictureGalleryFragment extends Fragment {

    private Unbinder unbind;

    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    public static PictureGalleryFragment newInstance(String picture) {
        PictureGalleryFragment frag = new PictureGalleryFragment();
        Bundle extras = new Bundle();
        extras.putString("picture",picture);
        frag.setArguments(extras);
        return frag;
    }

    @Inject
    public PictureGalleryFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_picture_gallery, container, false);
        unbind = ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getActivity()).load(getArguments().getString("picture")).into(ivPicture);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}


