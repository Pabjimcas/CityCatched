package com.pabji.citycatched.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.models.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pablo Jiménez Casado on 06/08/2016.
 */

public class BuildingCommentsAdapter extends RecyclerView.Adapter<BuildingCommentsAdapter.BuildingCommentHolder> {

    private List<Comment> listItem = new ArrayList<>();

    @Override
    public BuildingCommentsAdapter.BuildingCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new BuildingCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(BuildingCommentsAdapter.BuildingCommentHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }


    public void setData(List<Comment> commentList) {
        this.listItem = commentList;
        this.notifyDataSetChanged();
    }

    public class BuildingCommentHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_comment)
        TextView tvComment;

        @BindView(R.id.tv_username)
        TextView tvUsername;

        @BindView(R.id.tv_mark)
        TextView tvMark;

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.iv_userimage)
        ImageView ivUserImage;

        public BuildingCommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(Comment comment) {
            tvComment.setText(comment.getDescription());
            tvUsername.setText(comment.getUsername());
            tvDate.setText(comment.getDateFormated());
            tvMark.setText(String.valueOf(comment.getMark()));
            Glide.with(itemView.getContext()).load(comment.getUserImage()).into(ivUserImage);
        }
    }

}
