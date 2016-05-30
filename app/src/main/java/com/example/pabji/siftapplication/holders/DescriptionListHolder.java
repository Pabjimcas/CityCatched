package com.example.pabji.siftapplication.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.models.Building;
import com.example.pabji.siftapplication.models.Description;
import com.squareup.picasso.Picasso;

public class DescriptionListHolder extends RecyclerView.ViewHolder {

    private final TextView contentItem;
    private final TextView titleItem;

    public DescriptionListHolder(View itemView) {
        super(itemView);
        contentItem = (TextView) itemView.findViewById(R.id.description);
        titleItem = (TextView)itemView.findViewById(R.id.title_item);
    }

    public void bindItem(Description item) {
        titleItem.setText(item.getTitle());
        contentItem.setText(item.getContent());
    }
}
