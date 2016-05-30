package com.example.pabji.siftapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pabji.siftapplication.R;
import com.example.pabji.siftapplication.holders.DescriptionListHolder;
import com.example.pabji.siftapplication.models.Description;

import java.util.List;

public class DescriptionListAdapter extends RecyclerView.Adapter<DescriptionListHolder>{

    private List<Description> listItem;

    public DescriptionListAdapter(List<Description> items) {
        listItem = items;
    }

    @Override
    public DescriptionListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description_list,parent,false);
        return new DescriptionListHolder(view);
    }

    @Override
    public void onBindViewHolder(DescriptionListHolder holder, int position) {
        holder.bindItem(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

}
