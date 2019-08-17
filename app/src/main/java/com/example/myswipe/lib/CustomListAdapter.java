package com.example.myswipe.lib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myswipe.R;

import java.util.ArrayList;

/**
 * Created by tutlane on 23-08-2017.
 */
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<ListItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<ListItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {

        if (listData.get(position).value==null) {
            return false;
        } else {
            return true;
        }
    }
    /*
        public View getView(int position, View v, ViewGroup vg) {
            if (v==null) {
                v = layoutInflater.inflate(R.layout.list_row, null);
            }
            return v;
        }
    */
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.uName = (TextView) v.findViewById(R.id.name);
            holder.uDesignation = (TextView) v.findViewById(R.id.designation);
            holder.uLocation = (TextView) v.findViewById(R.id.location);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uName.setText(position+""); //listData.get(position)+"");
        holder.uDesignation.setText(listData.get(position).key);
        holder.uLocation.setText(listData.get(position).getValues());

        holder.uName.setTextColor(Color.BLACK);
        holder.uDesignation.setTextColor(Color.BLUE);
        holder.uLocation.setTextColor(Color.RED);

        //holder.uName.setText(listData.get(position).getName());
        //holder.uDesignation.setText(listData.get(position).getDesignation());
        //holder.uLocation.setText(listData.get(position).getLocation());
        return v;
    }
    static class ViewHolder {
        TextView uName;
        TextView uDesignation;
        TextView uLocation;
    }
}
