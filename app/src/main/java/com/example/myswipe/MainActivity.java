package com.example.myswipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myswipe.lib.CustomizedProperties;
import com.example.myswipe.lib.WebService;

public class MainActivity extends AppCompatActivity {
    String pageData[];            //Stores the text to swipe.
    LayoutInflater inflater;    //Used to create individual pages

    ViewPager vp;                //Reference to class to swipe views
    MyPagesAdapter adapter;


    boolean isTimerOn;
    //deviceInfo



    private static CustomizedProperties deviceInfoProps = new CustomizedProperties();


    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CustomizedProperties prop = DeviceService.getInfo(getApplicationContext(), MainActivity.this);

        //Get the data to be swiped through
        pageData = getResources().getStringArray(R.array.desserts);

        pageData = new String[]{"one","two","three"};

        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        vp = (ViewPager) findViewById(R.id.viewPager);

        //https://stackoverflow.com/questions/12539961/destroyitem-being-called-when-position-2-why-android-pageradapter
        vp.setOffscreenPageLimit(2);

        //set the adapter that will create the individual pages
        adapter = new MyPagesAdapter();
        vp.setAdapter(adapter);
    }

	/*
	https://stackoverflow.com/questions/8258759/getting-the-current-position-of-a-viewpager

	* Get the current view position from the ViewPager by
	* extending SimpleOnPageChangeListener class and adding your method

	public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			currentPage = position;
		}

		public final int getCurrentPage() {
			return currentPage;
		}
	}
	
	//or mViewPager.getCurrentItem()
	
	https://stackoverflow.com/questions/12854783/android-viewpager-get-the-current-view
	*/

    //Implement PagerAdapter Class to handle individual page creation
    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return pageData.length;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = inflater.inflate(R.layout.page, null);

            //((TextView)page.findViewById(R.id.textMessage)).setText(getPageData(position)); //pageData[position]);
            TextView tv = (TextView) page.findViewById(R.id.textMessage);

            Log("position: " + position);

            //Log("getItemPosition: " + this.getItemPosition());
            Object tag =  page.getTag();
            if (tag==null){
                page.setTag("" + position);
            }
            Log("tag: " + page.getTag());

            switch (position){

                case 1:
                    showProgress(MainActivity.this, tv,deviceInfoProps);
                    break;
                case 2:
                    tv.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL | Gravity.LEFT);
                    //https://alvinalexander.com/android/how-to-set-font-size-style-textview-programmatically
                    tv.setTextAppearance(MainActivity.this, R.style.fontForReport);
                    showReport(MainActivity.this,tv);

                    break;
                case 0:
                default:
                    //tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                    //tv.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL | Gravity.LEFT);
                    tv.setText(getPageData(position));
                    break;
            }
            //if (position == 1) {
           //    showProgress(MainActivity.this, tv,deviceInfoProps,new Location(""));
            //} else {
            //    tv.setText(getPageData(position));
            //}

            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }

        //simple way to reinstatiete all views if
        //mAdapter.notifyDataSetChanged() called
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public String getPageData(int position) {
        switch (position) {
            case 0:
                return (deviceInfoProps=DeviceService.getInfo(getApplicationContext(), MainActivity.this)).toString();
            case 1:
                //return showProgress(MainActivity.this);
            default:
                return pageData[position];
        }

    }
/*
    private int currentPage;

    private static class PageListener extends SimpleOnPageChangeListener{
        public void onPageSelected(int position) {
            Log.i(TAG, "page selected " + position);
            currentPage = position;
        }
    }
*/
    /*
    clipart:
    https://material.io/tools/icons/?icon=refresh&style=baseline

    create:
    https://romannurik.github.io/AndroidAssetStudio/icons-actionbar.html#source.type=clipart&source.clipart=forward_30&source.space.trim=0&source.space.pad=0&name=ic_action_forward_30&theme=light&color=rgba(33%2C%20150%2C%20243%2C%200.6)

    code:
    https://en.proft.me/2016/07/22/how-create-actionbartoolbar-and-menu-android/
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.menu_help:
                Toast.makeText(this, "This is teh option help", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        */
        switch (item.getItemId()) {
            case R.id.timeron:
                Toast.makeText(this, "Timer Off", Toast.LENGTH_LONG).show();
                isTimerOn = false;
                onTimerOff();
                invalidateOptionsMenu();
                break;
            case R.id.timeroff:
                Toast.makeText(this, "Timer On", Toast.LENGTH_LONG).show();
                isTimerOn = true;
                onTimerOn();
                invalidateOptionsMenu();
                break;
            case R.id.menu_main_refresh:
                Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();
                onRefresh();
                break;
            default:
                Toast.makeText(this, "This is the option " + item.getItemId(), Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Build.VERSION.SDK_INT > 11) {
            //invalidateOptionsMenu();
            if (isTimerOn) {
                menu.findItem(R.id.timeroff).setVisible(false);
                menu.findItem(R.id.timeron).setVisible(true);
            } else {
                menu.findItem(R.id.timeroff).setVisible(true);
                menu.findItem(R.id.timeron).setVisible(false);
            }
        }

        //menu.getItem(100).setEnabled(false);
        //menu.getItem(200).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public void onRefresh(){
        adapter.notifyDataSetChanged();
    }

    public void onTimerOn(){

    }
    public void onTimerOff(){

    }
    /*

     */
    private Button btnSubmit;
    //static StringBuffer response;
    //static URL url;
    //static Activity activity;


    public void showProgress(final Activity activity, TextView tv, CustomizedProperties deviceInfoProps) {

        //btnSubmit = findViewById(R.id.btnSubmit);
        //btnSubmit.setOnClickListener(
        //    new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //tvOutput = "";
        //tv.setText("");
        //Call WebService
        //new GetServerData(syncCall,DeviceService.getHeader(activity.getApplicationContext())).getResponse();

        //String path = "http://igorkourski.000webhostapp.com/sandbox/one.php?ig=kr&kr=ig";;
        //WebService ws = new WebService(activity,path,deviceInfoProps);
        SyncService ws = new SyncService(activity,deviceInfoProps);
        new GetServerData(activity, tv, ws).execute();

        //tv.setText("two");
        //  }
        //}
        //)


    }

    public void showReport(final Activity activity, TextView tv){
        //http://igorkourski.000webhostapp.com/sandbox/report.php?ig=kr&kr=ig
        //{"data":[{"number":1,"id":"205","ip":"10.0.0.130","time":"2019-06-16 00:00:14"},{"number":2,"id":"204","ip":"24.4.171.103","time":"2019-06-15 23:11:31"},

        ReportService ws = new ReportService(activity,deviceInfoProps);
        //String per = ws.getResponse();
        //tv.setText(per);
        new AsyncSilent(activity, tv, ws).execute();
    }

    public class AsyncSilent extends AsyncTask {

        Activity activity;
        TextView tv;
        //CustomizedProperties deviceInfo;
        String output;
        WebService ws;

        public AsyncSilent(Activity activity, TextView tv, WebService ws){
            this.activity = activity;
            this.tv = tv;
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
            Log("Output: " + output);
            return output; //getWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tv.setText(output);
        }


    }

    public class GetServerData extends AsyncTask {
        public String path;
        public Activity activity;
        public TextView tv;

        //public CustomizedProperties props;

        public String output = "running";

        public WebService ws;

        public ProgressDialog progressDialog;

        /*
        public GetServerData(Activity activity, TextView tv, String path, CustomizedProperties header) {
            this.activity = activity;
            this.tv = tv;
            this.path = path;
            this.header = header;
        }
        */
        public GetServerData(Activity activity, TextView tv, WebService ws) {
            this.activity = activity;
            this.tv = tv;
            this.path = path;
            //this.header = header;
            this.ws = ws;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Fetching request info");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            //wait for 5 secs
            //(new Handler()).postDelayed(this::yourMethod, 5000);


            output = getWebServiceResponseData(activity,ws);
            Log("Output: " + output);
            return output; //getWebServiceResponseData();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Dismiss the progress dialog
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            // For populating list data
            //tv.setText(tvOutput);

            tv.setText(output);

            /*
            CustomCountryList customCountryList = new CustomCountryList(activity, countries);
            listView.setAdapter(customCountryList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Country country = (Country)countries.get(position);
                    Toast.makeText(getApplicationContext(),"You Selected "+country.getCountryName()+ " as Country",Toast.LENGTH_SHORT).show();        }
            });
            */
        }

    }

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

}
