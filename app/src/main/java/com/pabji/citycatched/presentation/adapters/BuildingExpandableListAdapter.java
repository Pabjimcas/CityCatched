package com.pabji.citycatched.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.mvp.models.Building;
import com.pabji.citycatched.presentation.utils.StringFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class BuildingExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private Map<String,List<Building>> mapBuilding = new HashMap<>();
    List<String> keyList = new ArrayList<>();

    public OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClickBuilding(String key,String buildingId);
    }

    public BuildingExpandableListAdapter(Context context,OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mapBuilding.get(keyList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return mapBuilding.get(keyList.get(groupPosition)).size();
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Building building = (Building) getChild(groupPosition,childPosition);

        final String keyParent = keyList.get(groupPosition);

        convertView = inflater.inflate(R.layout.item_building_list, null);

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.item_building);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickBuilding(keyParent,building.getId());
            }
        });

        ImageView buildingImage = (ImageView) convertView.findViewById(R.id.iv_buildingImage);
        Glide.with(convertView.getContext()).load(building.getMainImage()).into(buildingImage);

        TextView buildingTv = (TextView) convertView.findViewById(R.id.tv_buildingName);
        buildingTv.setText(building.getName());

        TextView buildingDistanceTv = (TextView) convertView.findViewById(R.id.tv_buildingDistance);
        String distance = StringFormat.truncate2Decimals(building.getDistance());
        buildingDistanceTv.setText("A "+distance+" metros");

        return convertView;
    }



    public void setValues(Map<String,List<Building>> mapBuilding) {
        this.mapBuilding = mapBuilding;
        keyList.clear();
        keyList.addAll(mapBuilding.keySet());
        notifyDataSetChanged();
    }

    public Object getGroup(int groupPosition) {
        return mapBuilding.get(keyList.get(groupPosition));
    }

    public int getGroupCount() {
        return mapBuilding.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.header_expandable_city, null);
        }

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);

        TextView city = (TextView) convertView.findViewById(R.id.tv_buildingCity);
        city.setText(keyList.get(groupPosition));

        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
