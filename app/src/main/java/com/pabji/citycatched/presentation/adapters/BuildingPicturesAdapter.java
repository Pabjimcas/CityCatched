package com.pabji.citycatched.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.models.Building;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class BuildingPicturesAdapter extends RecyclerView.Adapter<BuildingPicturesAdapter.BuildingPictureHolder> {

    private List<String> listItem = new ArrayList<>();
    private OnItemClickListener listener;

    @Override
    public BuildingPicturesAdapter.BuildingPictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new BuildingPictureHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(BuildingPicturesAdapter.BuildingPictureHolder holder, int position) {
        holder.bindItem(listItem.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public void setDataAndListener(List<String> pictureList, OnItemClickListener listener) {
        this.listItem = pictureList;
        this.listener = listener;
        this.notifyDataSetChanged();
    }

    public class BuildingPictureHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_picture)
        ImageView imageView;

        public int position;
        public OnItemClickListener listener;


        public BuildingPictureHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindItem(String picture, int position) {
            Glide.with(itemView.getContext()).load(picture).centerCrop().into(imageView);
            this.position = position;
        }

        @OnClick(R.id.iv_picture)
        public void onClickItem() {
            this.listener.onItemClickListener(itemView,position);
        }
    }

}
