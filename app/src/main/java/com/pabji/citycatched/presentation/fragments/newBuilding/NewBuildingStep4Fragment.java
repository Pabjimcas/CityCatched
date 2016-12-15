package com.pabji.citycatched.presentation.fragments.newBuilding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep1View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

public class NewBuildingStep4Fragment extends Fragment implements NewBuildingStep1View {

    private Unbinder unbind;

    public static NewBuildingStep4Fragment newInstance() {
        NewBuildingStep4Fragment frag = new NewBuildingStep4Fragment();
        return frag;
    }

    @Inject
    public NewBuildingStep4Fragment() {
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_building_step4,container,false);
        unbind = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showError(int error) {

    }
}
