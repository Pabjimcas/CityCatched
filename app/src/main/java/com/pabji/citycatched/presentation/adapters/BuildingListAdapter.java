package com.pabji.citycatched.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;
import com.pabji.citycatched.presentation.utils.StringFormat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.BuildingListViewHolder> {

    private List<Building> listItem = new ArrayList<>();
    private OnItemClickListener listener;

    @Inject
    public BuildingListAdapter(){}

    @Override
    public BuildingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building_list, parent, false);
        return new BuildingListViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(BuildingListViewHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }

    public interface OnItemClickListener {
        void openBuildingDetail(String buildingId);
    }

    public void setDataAndListener(List<Building> buildingList, OnItemClickListener listener) {
        this.listItem = buildingList;
        this.listener = listener;
        this.notifyDataSetChanged();
    }

    public class BuildingListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_buildingImage)
        ImageView buildingImage;

        @BindView(R.id.tv_buildingName)
        TextView buildingName;

        @BindView(R.id.tv_buildingDistance)
        TextView buildingDistance;

        public OnItemClickListener listener;
        private Building building;


        public BuildingListViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindItem(Building building) {
            this.building = building;
            buildingName.setText(building.getName());
            String distance = StringFormat.truncate2Decimals(building.getDistance());
            buildingDistance.setText("A "+distance+" metros");
            Glide.with(itemView.getContext()).load(building.getMainImage()).centerCrop().into(buildingImage);
        }

        @OnClick(R.id.item_building)
        public void openBuilding(){
            listener.openBuildingDetail(building.getId());
        }

    }

}
