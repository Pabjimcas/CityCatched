package com.pabji.citycatched.presentation.fragments.newBuilding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingStep1Presenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep1View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

public class NewBuildingStep1Fragment extends Fragment implements NewBuildingStep1View {

    private Unbinder unbind;

    @BindView(R.id.et_building_name)
    EditText buildingName;

    @Inject
    NewBuildingStep1Presenter presenter;

    public static NewBuildingStep1Fragment newInstance() {
        NewBuildingStep1Fragment frag = new NewBuildingStep1Fragment();
        return frag;
    }

    @Inject
    public NewBuildingStep1Fragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_building_step1,container,false);
        unbind = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NewBuildingActivity)getActivity()).getComponent().inject(this);
        presenter.init(getActivity());
    }

    @Override
    public void showError(int error) {

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.setBuildingName(buildingName.getText().toString());
    }
}
