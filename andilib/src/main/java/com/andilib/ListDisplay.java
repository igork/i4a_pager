package com.andilib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myswipe.lib.BlankFragment;

import java.util.Calendar;
import java.util.Date;

public class ListDisplay extends AppCompatActivity {
    // Array of strings...
    String[] mobileArray = {
            "Android11111111111111111111111111111111133333333333333333333333333333333333333333333333333333333", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X"};

    ListView listView;


    class ListItem {
        public String key;
        public String value;

        public ListItem(String key,String value){
            this.key = key;
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        //on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //String selItem = (String) listView.getSelectedItem(); //WRONG
                //String value= selItem.getTheValue(); //getter method

                String tmp = (String) adapterView.getItemAtPosition(position);

                loadFragment(view,tmp,tmp);

            }
        });

        //on select
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String tmp = (String) adapterView.getItemAtPosition(position);


                //loadFragment(view,tmp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // your stuff
            }

        });


        //https://stackoverflow.com/questions/42458460/listview-in-a-pop-up-window

        //custom dialog
        /*
        Button clickButton = (Button) findViewById(R.id.clickButton);
        clickButton.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(YourActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Title...");
                myNames= (ListView) dialog.findViewById(R.id.List);
                adapter = new Adapter(YourActivity.this,R.layout.names_view, Current.Names);
                myNames.setAdapter(adapter);
                dialog.show();

            }
        });
        */
        /*
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

           <ListView
                android:id="@+id/List"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" >
           </ListView>

        </LinearLayout>
        */
        ///

        //alert dialog
        /*
         <?xml version="1.0" encoding="utf-8"?>
        <ListView xmlns:android="http://schemas.android.com/apk/res/android"
                android:id="@+id/listView1"
                android:layout_width="fill_parent"
                android:layout_height="fill_parent" >

        </ListView>

                activity

        String names[] ={"A","B","C","D"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.List);
        Adapter<String> adapter = new Adapter(this,R.layout.names_view, Current.Names);
        lv.setAdapter(adapter);
        alertDialog.show();
        */

    }


    private static final String BLANK_FRAGMENT_TAG = "FRAGMENT_TAG";
    public void loadFragment(View view, String title, String text) {
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.setText(text);
        blankFragment.setTitle(title);
        blankFragment.show(getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }

    public void showDialog() {
        /*
        final Dialog dialog = new Dialog(ListDisplay.this, String text);
                            dialog.setContentView(R.layout.custom_dialog);
                            dialog.setTitle("Title...");
        myNames=(ListView)dialog.findViewById(R.id.List);
        Adapter adapter =new Adapter(ListDisplay.this, R.layout.names_view, Current.Names);
                    myNames.setAdapter(adapter);
                    dialog.show();
                    */
    }

    public String getTimeDiff(Date date){
        String result = "";


        if (date!=null) {
            Date now = new Date();
            long sec = (now.getTime() - date.getTime()) / 1000;

            if (sec >= 60) {

            } else {
                result = sec + " sec";
            }
        }
        return result;

    }

    public static String getFriendlyTime(Date dateTime) {

        StringBuffer sb = new StringBuffer();
        Date current = Calendar.getInstance().getTime();
        long diffInSeconds = (current.getTime() - dateTime.getTime()) / 1000;

    /*long diff[] = new long[]{0, 0, 0, 0};
    /* sec *  diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
    /* min *  diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours *  diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days * diff[0] = (diffInSeconds = (diffInSeconds / 24));
     */
        long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
        long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
        long years = (diffInSeconds = (diffInSeconds / 12));

        if (years > 0) {
            if (years == 1) {
                sb.append("a year");
            } else {
                sb.append(years + " years");
            }
            if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(" and a month");
                } else {
                    sb.append(" and " + months + " months");
                }
            }
        } else if (months > 0) {
            if (months == 1) {
                sb.append("a month");
            } else {
                sb.append(months + " months");
            }
            if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(" and a day");
                } else {
                    sb.append(" and " + days + " days");
                }
            }
        } else if (days > 0) {
            if (days == 1) {
                sb.append("a day");
            } else {
                sb.append(days + " days");
            }
            if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(" and an hour");
                } else {
                    sb.append(" and " + hrs + " hours");
                }
            }
        } else if (hrs > 0) {
            if (hrs == 1) {
                sb.append("an hour");
            } else {
                sb.append(hrs + " hours");
            }
            if (min > 1) {
                sb.append(" and " + min + " minutes");
            }
        } else if (min > 0) {
            if (min == 1) {
                sb.append("a minute");
            } else {
                sb.append(min + " minutes");
            }
            if (sec > 1) {
                sb.append(" and " + sec + " seconds");
            }
        } else {
            if (sec <= 1) {
                sb.append("about a second");
            } else {
                sb.append("about " + sec + " seconds");
            }
        }

        sb.append(" ago");


    /*String result = new String(String.format(
    "%d day%s, %d hour%s, %d minute%s, %d second%s ago",
    diff[0],
    diff[0] > 1 ? "s" : "",
    diff[1],
    diff[1] > 1 ? "s" : "",
    diff[2],
    diff[2] > 1 ? "s" : "",
    diff[3],
    diff[3] > 1 ? "s" : ""));*/
        return sb.toString();
    }
}
