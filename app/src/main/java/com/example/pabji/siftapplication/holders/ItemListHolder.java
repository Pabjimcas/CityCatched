package com.example.pabji.siftapplication.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.models.Building;
import com.squareup.picasso.Picasso;

public class ItemListHolder extends RecyclerView.ViewHolder {

    private final ImageView imageItem;
    private final TextView titleItem;
    private final Context mContext;

    public ItemListHolder(Context context, View itemView) {
        super(itemView);
        imageItem = (ImageView) itemView.findViewById(R.id.image_item);
        titleItem = (TextView) itemView.findViewById(R.id.title_item);
        mContext = context;
    }

    public void bindItem(Building item) {
        titleItem.setText(item.getName());

        if (item.getUrl_image() != "") {
            Picasso.with(mContext).load(item.getUrl_image()).fit().centerCrop().into(imageItem);
        } else {
            Picasso.with(mContext).load(R.drawable.build_failed).into(imageItem);
        }
    }
}
