package com.pabji.citycatched.presentation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.components.NoRecognizedComponent;
import com.pabji.citycatched.presentation.activities.NoRecognizedActivity;
import com.pabji.citycatched.presentation.adapters.NoRecognizedAdapter;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.presenters.NoRecognizedPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 04/08/2016.
 */

public class NotRecognizeListFragment extends Fragment implements NoRecognizedAdapter.OnItemClickListener {


    @BindView(R.id.rv_building_list)
    RecyclerView rvBuildingList;

    @Inject
    NoRecognizedAdapter noRecognizedAdapter;

    private Unbinder unbind;
    private View afterSelectedItem;
    private NoRecognizedComponent component;
    private NoRecognizedPresenter presenter;


    public static NotRecognizeListFragment newInstance() {
        NotRecognizeListFragment frag = new NotRecognizeListFragment();
        return frag;
    }

    @Inject
    public NotRecognizeListFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_recognized_list,container,false);
        unbind = ButterKnife.bind(this,view);

        this.presenter = ((NoRecognizedActivity) getActivity()).getPresenter();
        this.component = ((NoRecognizedActivity)getActivity()).getComponent();
        this.component.inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBuildingList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rvBuildingList.setAdapter(noRecognizedAdapter);
        noRecognizedAdapter.setDataAndListener(presenter.getNearBuildingList(),this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public void onItemClickListener(View view, String id) {
        if(afterSelectedItem != null) {
            afterSelectedItem.setBackgroundColor(Color.WHITE);
        }
        ((NoRecognizedActivity) getActivity()).getPresenter().selectBuilding(id);
        view.setBackgroundColor(Color.BLUE);
        afterSelectedItem = view;
    }
}


