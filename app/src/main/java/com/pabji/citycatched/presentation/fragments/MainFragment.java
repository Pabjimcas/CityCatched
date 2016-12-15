package com.pabji.citycatched.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.MainActivityComponent;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.adapters.BuildingListAdapter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.MainActivityPresenter;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class MainFragment extends Fragment implements BuildingListAdapter.OnItemClickListener {


    private Unbinder unbind;
    private MainActivityPresenter presenter;
    private MainActivityComponent component;

    @BindView(R.id.tv_nearBuilding)
    TextView tvNearBuilding;

    @BindView(R.id.rv_nearBuilding)
    RecyclerView rvNearBuilding;

    @Inject
    BuildingListAdapter buildingListAdapter;

    public static MainFragment newInstance() {
        MainFragment frag = new MainFragment();
        return frag;
    }

    @Inject
    public MainFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        unbind = ButterKnife.bind(this,view);

        this.presenter = ((MainActivity) getActivity()).getPresenter();
        this.component = ((MainActivity)getActivity()).getComponent();
        this.component.inject(this);

        rvNearBuilding.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNearBuilding.setAdapter(buildingListAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    public void loadNearBuildings() {
        List<Building> buildingList = presenter.getNearBuildingList();
        buildingListAdapter.setDataAndListener(buildingList,this);
        tvNearBuilding.setText(buildingList.size()+" edificios cercanos");
    }

    @Override
    public void openBuildingDetail(String buildingId) {
        presenter.getBuilding(buildingId);
    }
}


