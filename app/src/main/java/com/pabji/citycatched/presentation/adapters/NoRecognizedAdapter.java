package com.pabji.citycatched.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.mvp.models.Descriptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class NoRecognizedAdapter extends RecyclerView.Adapter<NoRecognizedAdapter.NoRecognizedHolder> {

    private List<Building> listItem = new ArrayList<>();
    private OnItemClickListener listener;

    @Override
    public NoRecognizedAdapter.NoRecognizedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_not_recognized, parent, false);
        return new NoRecognizedHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(NoRecognizedAdapter.NoRecognizedHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, String id);
    }

    public void setDataAndListener(List<Building> buildingList, OnItemClickListener listener) {
        this.listItem = buildingList;
        this.listener = listener;
        this.notifyDataSetChanged();
    }

    public class NoRecognizedHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_building)
        ImageView imageView;

        public Building building;
        public OnItemClickListener listener;


        public NoRecognizedHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindItem(Building building) {
            this.building = building;
            Glide.with(itemView.getContext()).load(building.getMainImage()).into(imageView);
        }

        @OnClick(R.id.item_building)
        public void onClickItem() {
            this.listener.onItemClickListener(itemView,building.getId());
        }
    }

}
