package com.example.myswipe.lib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myswipe.R;
import com.example.myswipe.ReportService;

import java.util.ArrayList;

public class CustomAdapter2 extends BaseAdapter {

    private ArrayList<CustomizedProperties> listData;
    private LayoutInflater layoutInflater;

    public CustomAdapter2(Context aContext, ArrayList<CustomizedProperties> listData) {
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

        /*
        if (listData.get(position).get("details")==null) {
            return false;
        } else {
            return true;
        }
        */
        return true;
    }
    /*
        public View getView(int position, View v, ViewGroup vg) {
            if (v==null) {
                v = layoutInflater.inflate(R.layout.list_row, null);
            }
            return v;
        }
    */
    /*
    public View getView(int position, View v, ViewGroup vg) {
        CustomAdapter2.ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row, null);// v = layoutInflater.inflate(R.layout.list_prop_row, null);
            holder = new CustomAdapter2.ViewHolder();
            holder.time = (TextView) v.findViewById(R.id.name);  //.time);
            //holder.distance = (TextView) v.findViewById(R.id.distance);
            //holder.ip = (TextView) v.findViewById(R.id.ip);
            v.setTag(holder);
        } else {
            holder = (CustomAdapter2.ViewHolder) v.getTag();
        }
        holder.time.setText((String)listData.get(position).get(ReportService.item.time)); //setText(position+""); //listData.get(position)+"");
        holder.distance.setText(""); //listData.get(position).get); //setText(listData.get(position).key);
        holder.ip.setText((String)listData.get(position).get(ReportService.item.ip));  //listData.get(position).getValues());

        holder.time.setTextColor(Color.BLACK);
        holder.distance.setTextColor(Color.BLUE);
        holder.ip.setTextColor(Color.RED);

        //holder.uName.setText(listData.get(position).getName());
        //holder.uDesignation.setText(listData.get(position).getDesignation());
        //holder.uLocation.setText(listData.get(position).getLocation());
        return v;
    }
        static class ViewHolder {
        TextView time;
        TextView distance;
        TextView ip;
    }
    */
    public View getView(int position, View v, ViewGroup vg) {
        CustomAdapter2.ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row, null);
            holder = new CustomAdapter2.ViewHolder();
            holder.uName = (TextView) v.findViewById(R.id.name);
            holder.uDesignation = (TextView) v.findViewById(R.id.designation);
            holder.uLocation = (TextView) v.findViewById(R.id.location);
            v.setTag(holder);
        } else {
            holder = (CustomAdapter2.ViewHolder) v.getTag();
        }
        holder.uName.setText((String)listData.get(position).get("time")); //listData.get(position)+"");

        //holder.uDesignation.setText(listData.get(position).key);
        //holder.uLocation.setText(listData.get(position).getValues());

        holder.uDesignation.setText((String)listData.get(position).get(""));
        holder.uLocation.setText((String)listData.get(position).get("ip"));

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
