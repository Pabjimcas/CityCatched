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
import com.pabji.citycatched.presentation.adapters.BuildingPagerAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class PictureBuildingFragment extends Fragment {

    private Unbinder unbind;

    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    private BuildingPagerAdapter.OnItemClickListener listener;

    public static PictureBuildingFragment newInstance(String picture, int position, BuildingPagerAdapter.OnItemClickListener listener) {
        PictureBuildingFragment frag = new PictureBuildingFragment();
        Bundle extras = new Bundle();
        extras.putString("picture",picture);
        extras.putInt("position",position);
        frag.setArguments(extras);

        frag.setListener(listener);

        return frag;
    }

    private void setListener(BuildingPagerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public PictureBuildingFragment() {
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

        Glide.with(getActivity()).load(getArguments().getString("picture")).centerCrop().into(ivPicture);
    }

    @OnClick(R.id.iv_picture)
    public void onClickPicture(){
        listener.onItemClickListener(getArguments().getInt("position"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}


