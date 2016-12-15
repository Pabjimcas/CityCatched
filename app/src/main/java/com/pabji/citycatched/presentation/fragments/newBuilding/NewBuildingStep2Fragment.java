package com.pabji.citycatched.presentation.fragments.newBuilding;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabji.citycatched.R;
import com.pabji.citycatched.domain.constants.RequestConstants;
import com.pabji.citycatched.presentation.activities.NewBuildingActivity;
import com.pabji.citycatched.presentation.adapters.AddPicturesAdapter;
import com.pabji.citycatched.presentation.fragments.base.BaseMVPFragment;
import com.pabji.citycatched.presentation.mvp.presenters.newBuilding.NewBuildingStep2Presenter;
import com.pabji.citycatched.presentation.mvp.views.newBuilding.NewBuildingStep2View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Pablo Jiménez Casado on 07/08/2016.
 */

public class NewBuildingStep2Fragment extends BaseMVPFragment<NewBuildingStep2Presenter,NewBuildingStep2View> implements NewBuildingStep2View, AddPicturesAdapter.OnItemClickListener {

    private Unbinder unbind;

    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;

    @Inject
    AddPicturesAdapter addPicturesAdapter;

    public static NewBuildingStep2Fragment newInstance() {
        NewBuildingStep2Fragment frag = new NewBuildingStep2Fragment();
        return frag;
    }

    @Inject
    public NewBuildingStep2Fragment() {
        setRetainInstance(true);
    }

    @Override
    public NewBuildingStep2Presenter createPresenter() {
        return ((NewBuildingActivity)getActivity()).getComponent().newBuildingStep2Presenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_building_step2,container,false);
        unbind = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NewBuildingActivity)getActivity()).getComponent().inject(this);
        presenter.init(this);
        rvPhotos.setLayoutManager(new GridLayoutManager(getActivity(),3));
        addPicturesAdapter.setListener(this);
        rvPhotos.setAdapter(addPicturesAdapter);
    }

    @Override
    public void showError(int error) {

    }

    @Override
    public void addPicture() {
        presenter.showPictureOptions();
    }

    @Override
    public void onItemClickListener(View view, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestConstants.SELECT_IMAGE_GALLERY_NEWBUILDING_STEP2){
                presenter.addImageFromGallery(data.getData());
            }else if(requestCode == RequestConstants.CAPTURE_IMAGE_NEWBUILDING_STEP2){
                presenter.addImageFromCamera();
            }
        }
    }

    @Override
    public void showPhotoList(List<Uri> imagesPath) {
        addPicturesAdapter.setData(imagesPath);
    }
}
