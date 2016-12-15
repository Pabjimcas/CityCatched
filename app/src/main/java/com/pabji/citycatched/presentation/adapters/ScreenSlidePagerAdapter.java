package com.pabji.citycatched.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pabji.citycatched.presentation.fragments.PictureGalleryFragment;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 03/12/2016.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> pictures;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<String> pictures) {
        super(fm);
        this.pictures = pictures;
    }


    @Override
    public Fragment getItem(int position) {
        return PictureGalleryFragment.newInstance(pictures.get(position));
    }

    @Override
    public int getCount() {
        return pictures.size();
    }
}
