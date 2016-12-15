package com.pabji.citycatched.presentation.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.MainActivityComponent;
import com.pabji.citycatched.domain.features.GetBuildingListVisitedInteractor;
import com.pabji.citycatched.presentation.activities.MainActivity;
import com.pabji.citycatched.presentation.adapters.BuildingExpandableListAdapter;
import com.pabji.citycatched.presentation.adapters.BuildingListAdapter;
import com.pabji.citycatched.presentation.fragments.base.BaseMVPFragment;
import com.pabji.citycatched.presentation.mvp.presenters.BuildingListVisitedPresenter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.views.BuildingListVisitedView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuildingListVisitedFragment extends BaseMVPFragment<BuildingListVisitedPresenter,BuildingListVisitedView> implements BuildingListVisitedView, BuildingExpandableListAdapter.OnItemClickListener {
    private MainActivityComponent component;
    private Unbinder unbind;

    @BindView(R.id.ep_buildingvisited)
    ExpandableListView epBuildingList;

    private ProgressDialog progressDialog;
    private BuildingExpandableListAdapter mAdapter;


    @Inject
    public BuildingListVisitedFragment() {
        setRetainInstance(true);
    }

    public static BuildingListVisitedFragment newInstance() {
        BuildingListVisitedFragment fragment = new BuildingListVisitedFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.init(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        component = ((MainActivity)getActivity()).getComponent();
        component.inject(this);
        View view = inflater.inflate(R.layout.fragment_revision_list,container,false);
        unbind = ButterKnife.bind(this,view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        mAdapter = new BuildingExpandableListAdapter(getActivity(),this);
        epBuildingList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public BuildingListVisitedPresenter createPresenter() {
        return component.createPresenter();
    }

    @Override
    public void showBuildingVisited(Map<String, List<Building>> buildingMap) {
        mAdapter.setValues(buildingMap);
    }

    @Override
    public void showProgressDialog(int messageResource) {
        progressDialog.setMessage(getString(messageResource));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onClickBuilding(String key, String buildingId) {
        presenter.getBuilding(key,buildingId);
    }
}
