package com.pabji.citycatched.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.pabji.citycatched.presentation.fragments.PictureBuildingFragment;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/12/2016.
 */

public class BuildingPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> pictures;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public BuildingPagerAdapter(FragmentManager fm, List<String> pictures, OnItemClickListener listener) {
        super(fm);
        this.pictures = pictures;
        this.listener = listener;
    }


    @Override
    public Fragment getItem(int position) {
        return PictureBuildingFragment.newInstance(pictures.get(position),position,listener);
    }

    @Override
    public int getCount() {
        return pictures.size();
    }
}
