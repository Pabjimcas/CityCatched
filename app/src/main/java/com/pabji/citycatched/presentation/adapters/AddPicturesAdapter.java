package com.pabji.citycatched.presentation.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class AddPicturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPTY_VIEW = 1;
    private static final int PICTURE_VIEW = 2;

    private OnItemClickListener listener;
    private List<Uri> pictures = new ArrayList<>();

    public interface OnItemClickListener {
        void addPicture();
        void onItemClickListener(View view, int position);
    }

    @Inject
    public AddPicturesAdapter(){}

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return EMPTY_VIEW;
        }else{
            return PICTURE_VIEW;
        }
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == EMPTY_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_picture, parent, false);
            return new EmptyPictureViewHolder(view,listener);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new PictureViewHolder(view,listener);
        }
    }

    public void setData(List<Uri> pictures){
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > 0) {
            ((PictureViewHolder)holder).bindView(pictures.get(position-1), position-1);
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size() + 1;
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {

        private final OnItemClickListener listener;

        @BindView(R.id.iv_picture)
        ImageView imageView;
        private int position;

        public PictureViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        public void bindView(Uri uri, int position){
            this.position = position;
            Glide.with(itemView.getContext()).load(uri).centerCrop().into(imageView);
        }

        @OnClick(R.id.iv_picture)
        public void onClickItem(){
            this.listener.onItemClickListener(itemView,position);
        }
    }

    public class EmptyPictureViewHolder extends RecyclerView.ViewHolder {

        private final OnItemClickListener listener;

        public EmptyPictureViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @OnClick(R.id.new_picture)
        public void onClickItem(){
            this.listener.addPicture();
        }

    }
}
