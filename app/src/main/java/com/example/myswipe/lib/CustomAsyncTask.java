package com.example.myswipe.lib;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myswipe.R;
import com.example.myswipe.RecordService;
import com.example.myswipe.ReportService;

import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAsyncTask extends AsyncTask {

    AppCompatActivity activity;
    TextView tv;

    ListView listView;
    ArrayList<ListItem> item;

    String output;
    WebService ws;
    CustomProperties deviceInfo;

    private static final String TAG = CustomAsyncTask.class.getName();

    public CustomAsyncTask(AppCompatActivity activity, WebService ws, ListView listView){
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

            if( props!=null) {
                    /*
                    ArrayAdapter adapter = new ArrayAdapter<String>(activity,
                            R.layout.activity_listview,
                            props.getStringValues());//mobileArray);

                    //ListView listView = (ListView) findViewById(R.id.mobile_list);
                    listView.setAdapter(adapter);

                    //on click
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            //String selItem = (String) listView.getSelectedItem(); //WRONG
                            //String value= selItem.getTheValue(); //getter method

                            //String tmp = (String) adapterView.getItemAtPosition(position) + " pos=" + position + " id=" + id;

                            loadFragment(view,(String) adapterView.getItemAtPosition(position),"id: " + position);

                        }
                    });
                    */

                    /*
                    CustomProperties props
                    convert to
                    ArrayList<ListItem> item
                    */

                /*
                ArrayList<ListItem> item = new ArrayList<ListItem>(
                        Arrays.asList(
                                new ListItem("time", new String[]{"reserved", "ip"}),
                                new ListItem("two", new String[]{"a", "bbbbbbbbbb"}),
                                new ListItem("one", new String[]{"c", "dddddddddddddddddddd\nddddddddddddddddddddddddd\n\nddddddddddddddddd"}),
                                new ListItem("two", new String[]{"e", "f","g"}),
                                new ListItem("two",null),
                                new ListItem("last",null)

                        )

                );
                */

                //props to array
                BaseAdapter adapter;

                //TODO: remove business logic from here
                if( ws instanceof ReportService){
                    adapter = new CustomAdapter2(activity/*getApplicationContext()*/,((ReportService) ws).toArrayList(props),ws.getHeaders());
                } else {
                    adapter = new ArrayAdapter<String>(activity,
                            R.layout.activity_listview,
                            props.getStringValues());//mobileArray);
                }

                //listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);

                //on click
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Object tmp = adapterView.getItemAtPosition(position);

                        if (tmp==null){
                            return;
                        }

                        //TODO: remove business logic from here
                        //lazyCall(tmp);

                        //directCall
                        String title;
                        String text = "";
                        if (tmp instanceof CustomProperties){
                            title = "id: " + ((CustomProperties)tmp).get("id");

                            String temp2 = (String)((CustomProperties)tmp).get("headers");
                            if (temp2!=null) {

                                try{
                                    JSONObject jsonObject = (new JSONObject(temp2)).getJSONObject("");
                                    text = jsonObject.toString(2);
                                } catch (Exception e){
                                    text = temp2;
                                }

                            }

                        } else {
                            title = "" + tmp;
                        }

                        //TODO: get record info to text or list

                        if (tmp!=null) {
                            new BlankFragment().loadFragment(activity, title, text);
                        }

                    }
                });

            }
        }
    }

    public void lazyCall(Object tmp) {
        //TODO: everything into cache

        String title = "" + tmp;
        String text = "";

        BlankFragment fragment = new BlankFragment();
        ///FragmentManager fm = activity.getSupportFragmentManager();

        //fragment.setText(text);
        //fragment.setTitle(title);

        if (tmp instanceof CustomProperties){

            String recordId = "" + ((CustomProperties)tmp).get("id");
            title = "id: " + recordId;
            String headers = "" + ((CustomProperties)tmp).get("headers");

            //show pretty json


            text = "headers: " + headers;


            if (recordId!=null && !recordId.isEmpty()) {

                String path2 = RecordService.path + "?" + "id=" + recordId + "&headers=1";

                //TODO: get record info into text
                RecordService ws = new RecordService(activity, null,path2);
                //String per = ws.getResponse();
                //tv.setText(per);

                TextView tv = fragment.getTextView();
                CustomAsyncTaskSilent task = new CustomAsyncTaskSilent(activity, ws, tv);
                task.execute();

                //TO DO remove
                //final String BLANK_FRAGMENT_TAG = "FRAGMENT_TAG";
                //fragment.show(fm, BLANK_FRAGMENT_TAG);
            }

            //TODO: or into list
        }

        fragment.loadFragment(activity, title, text);
    }

    /*
        protected static void Log(String text) {
            Log.i(TAG,text);
            System.out.println(text);
        }

        public synchronized String getWebServiceResponseData(Activity activity, WebService ws){
            try {
                String response = ws.getResponse();
                Log("Response: " + response);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "No Response";
        }
        */
    public synchronized String getWebServiceResponseData(Activity activity, WebService ws){
        String response = null;
        try {
            response = ws.getResponse();
        } catch (Exception e) {
            response = e.getMessage();
        }
        com.example.myswipe.lib.Log.log("Response: " + response);
        return response;
    }

}

