package com.example.myswipe.lib;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myswipe.R;

public class CustomAsyncTaskSilent extends AsyncTask {

    Activity activity;
    TextView tv;
    ListView listView;

    String output;
    WebService ws;


    public CustomAsyncTaskSilent(Activity activity, WebService ws, TextView tv){
        this.activity = activity;
        this.tv = tv;
        this.ws = ws;
    }

    public CustomAsyncTaskSilent(Activity activity, WebService ws, ListView listView){
        this.activity = activity;
        this.listView = listView;
        this.ws = ws;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        //wait for 5 secs
        //(new Handler()).postDelayed(this::yourMethod, 5000);

            /*
            private static int TIME_OUT = 1000 * 20;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Do something here
                        progress.dismiss();
                    }
                }, TIME_OUT);
                */

            /*
            //This will get called when response is not received in 20 sec. **call this where you are hitting your server.*
            handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //hide Progressbar here or close your activity.
                        //if(progressbar!=null && progressbar.isShowing())
                        //{
                        //    progressbar.dismiss();
                        //}

                    }
                },20*1000);
            //**on Response**  remove the callback from that handler so that your app will not get crashed if service respond within 20 sec.
            handler.removeCallbacksAndMessages(null);
            */

        output = getWebServiceResponseData(activity,ws);
        Log.log("Output: " + output);
        return output; //getWebServiceResponseData();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (tv!=null) {
            tv.setText(output);
        }
        if (listView!=null) {
            CustomProperties props = ws.getResponseProps();

            if( props!=null ) {
                ArrayAdapter adapter = new ArrayAdapter<>(activity,
                        R.layout.activity_listview,
                        props.getStringValues());//mobileArray);

                //ArrayAdapter adapterList = new ArrayAdapter<reportItem>(this,
                //        R.layout.activity_listview,
                //        mobileArray);

                //ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);

                //on click
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        //String selItem = (String) listView.getSelectedItem(); //WRONG
                        //String value= selItem.getTheValue(); //getter method

                        //String tmp = (String) adapterView.getItemAtPosition(position) + " pos=" + position + " id=" + id;

                        //loadFragment(view,(String) adapterView.getItemAtPosition(position),"id: " + position);

                    }
                });
            }

        }

    }
    public synchronized String getWebServiceResponseData(Activity activity, WebService ws){
        String response;
        try {
            response = ws.getResponse();
        } catch (Exception e) {
            response = e.getMessage();
        }
        com.example.myswipe.lib.Log.log("Response: " + response);
        return response;
    }

}

