package com.example.myswipe.lib;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myswipe.DeviceService;
import com.example.myswipe.R;
import com.example.myswipe.ReportService;

import java.util.ArrayList;

public class CustomAdapter2 extends BaseAdapter {

    private ArrayList<CustomProperties> listData;
    private LayoutInflater layoutInflater;
    CustomProperties deviceInfo;

    public CustomAdapter2(Context aContext, ArrayList<CustomProperties> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public CustomAdapter2(Context aContext, ArrayList<CustomProperties> listData, CustomProperties deviceInfo) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.deviceInfo = deviceInfo;
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

        if (listData.get(position).get("headers")==null) {
            return false;
        } else {
            return true;
        }
        //return true;
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

        holder.uName.setTextColor(Color.BLACK);         //show time
        holder.uDesignation.setTextColor(Color.BLUE);   //show location
        holder.uLocation.setTextColor(Color.RED);       //show ip

        //holder.uName.setText(listData.get(position).getName());
        //holder.uDesignation.setText(listData.get(position).getDesignation());
        //holder.uLocation.setText(listData.get(position).getLocation());

        String headers = (String)listData.get(position).get("headers");
        int distance = (int)getDistanceBetweenTwoPoints(deviceInfo, headers);
        if (distance > 5) {
            holder.uDesignation.setText("" + distance);
        } else {
            if (distance > 0) {
                holder.uDesignation.setText("same place");
            } else {

            }
        }

        return v;
    }
    private float getDistanceBetweenTwoPoints(CustomProperties device, String headers) {

        try {
            String currentLatitude = device.getProperty(DeviceService.deviceInfo.latitude.name());
            String currentLongitude = device.getProperty(DeviceService.deviceInfo.longitude.name());

            Pair<String, String> loc = ReportService.extractLatitude(headers);

            String itemLatitude = loc.first;
            String itemLongitude = loc.second;

            return getDistanceBetweenTwoPoints(currentLatitude, currentLongitude, itemLatitude, itemLongitude);
        } catch (Exception e){
            //nothing
        }
        return (float)(-1.0);
    }
    private float getDistanceBetweenTwoPoints(String lat1,String lon1,String lat2,String lon2) {
        double la1 = Double.parseDouble(lat1);
        double lo1 = Double.parseDouble(lon1);
        double la2 = Double.parseDouble(lat2);
        double lo2 = Double.parseDouble(lon2);

        return getDistanceBetweenTwoPoints(la1,lo2,la2,lo2);

    }
    private float getDistanceBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        float[] distance = new float[2];

        //in meters
        Location.distanceBetween( lat1, lon1,
                lat2, lon2, distance);

        return distance[0];
    }
    static class ViewHolder {
        TextView uName;
        TextView uDesignation;
        TextView uLocation;
    }

}
