package com.pabji.citycatched.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.adapters.ScreenSlidePagerAdapter;
import com.pabji.citycatched.presentation.utils.ZoomOutPageTransformer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuildingGalleryActivity extends FragmentActivity{


    private ScreenSlidePagerAdapter mPagerAdapter;

    @BindView(R.id.pager)
    ViewPager viewPager;

    private Unbinder unbind;
    private List<String> pictures;
    private int position;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BuildingGalleryActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_gallery);
        unbind = ButterKnife.bind(this);
        pictures = getIntent().getStringArrayListExtra("pictures");
        position = getIntent().getIntExtra("position",0);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),pictures);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }
}