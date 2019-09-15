package com.example.myswipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myswipe.lib.CustomAsyncTask;
import com.example.myswipe.lib.CustomProperties;
import com.example.myswipe.lib.Log;
import com.example.myswipe.lib.WebService;


import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //String pageData[];            //Stores the text to swipe.
    LayoutInflater inflater;    //Used to create individual pages

    ViewPager vp;                //Reference to class to swipe views
    MyPagesAdapter adapter;


    boolean isTimerOn;
    //deviceInfo



    private static CustomProperties deviceInfoProps = new CustomProperties();

    //private CustomProperties reportProps = new CustomProperties();

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CustomProperties prop = DeviceService.getInfo(getApplicationContext(), MainActivity.this);

        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        vp = findViewById(R.id.viewPager);
        //set the adapter that will create the individual pages
        adapter = new MyPagesAdapter();
        vp.setOffscreenPageLimit(adapter.getCount());
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

        String[] pageData = new String[]{"one","two","three","four","five","six"};

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
            TextView tv = page.findViewById(R.id.textMessage);

            Log.log("position: " + position);

            //Log("getItemPosition: " + this.getItemPosition());
            /*
            Object tag =  page.getTag();
            if (tag==null){
                page.setTag("" + position);
            }
            Log("tag: " + page.getTag());
            */

            switch (position){

                case 0:
                    deviceInfoProps = DeviceService.getInfo(getApplicationContext(),MainActivity.this);
                    tv.setText(deviceInfoProps.toString());

                    break;

                case 1:
                    showProgress(MainActivity.this, tv,deviceInfoProps);
                    break;

                case 2:
                    page = inflater.inflate(R.layout.page4list, null);
                    showList4(MainActivity.this,page);
                    break;

                case 3:
                    tv.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL | Gravity.START);
                    //https://alvinalexander.com/android/how-to-set-font-size-style-textview-programmatically
                    tv.setTextAppearance(MainActivity.this, R.style.fontForReport);
                    showReport(MainActivity.this,tv);
                    break;

                case 4:
                    tv.setClickable(true);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(showAbout());
                    break;
                    //WebView wv = (WebView) page.findViewById(R.id.webview);
                    //String web = "https://www.journaldev.com";
                   // WebPage.getWeb(wv,web);
                    //break;

                case 5:
                    /*
                    ImageView iv = (ImageView) page.findViewById(R.id.imageView);
                    String image = "https://ic.pics.livejournal.com/lena-miro.ru/25587933/3467608/3467608_original.jpg";
                    new ImagePage().getImage(iv); //.getImage2(iv,image);
                    break;
                    */


                default:
                    //tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                    //tv.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL | Gravity.LEFT);
                    tv.setText("No data");
                    break;
            }
            //if (position == 1) {
           //    showProgress(MainActivity.this, tv,deviceInfoProps,new Location(""));
            //} else {
            //    tv.setText(getPageData(position));
            //}

            //Add the page to the front of the queue
            container.addView(page, 0);
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //simple way to reinstatiete all views if
        //mAdapter.notifyDataSetChanged() called
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    private String formattedHtml(String string){
        // Work around for API Versions lower than N, Html.fromHtml does not render list items correctly
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return string.replace("<ul>", "")
                    .replace("</ul>", "")
                    .replace("<li>", "<p>• ")
                    .replace("</li>", "</p><br />");
        } else {
            return string;
        }
    }
    @NotNull
    public Spanned showAbout() {
        String about = "";

        //about += "<ul style=\"list-style-type:circle;\">";

        Map<String,String> link = new LinkedHashMap<String,String>() {
            {
                put("Synchronous request","http://igorkourski.000webhostapp.com/sandbox/one.php?ig=kr&kr=ig");
                put("Asynchronous request","http://igorkourski.000webhostapp.com/sandbox/two.php?ig=kr&kr=ig");
                put("Python","http://igoryk.pythonanywhere.com");
                //put("Pubython","http://igoryk.pythonanywhere.com");
                put("About","http://igorkourski.000webhostapp.com/About/");
                put("Github","http://github.com/igork");
                put("Bitbucket","http://bitbucket.org/igorkourski/");
            }
        };

        for (String key: link.keySet()){

            about += "<li><a href=\"" + link.get(key) + "\">" + key + "</a></li>";

        }
        //about += "</ul>";

        return Html.fromHtml( formattedHtml("<p>" + about + "</p>"));//,Html.FROM_HTML_MODE_COMPACT);
    }
    public void test(TextView htmlTextView){
        String htmlString = "<img src='ic_launcher'><i>Welcome to<i> <b><a href='https://stackoverflow.com/'>Stack Overflow</a></b>";

            htmlTextView.setText(Html.fromHtml(htmlString, new Html.ImageGetter(){

                @Override
                public Drawable getDrawable(String source) {
                    Drawable drawable;
                    int dourceId =
                            getApplicationContext()
                                    .getResources()
                                    .getIdentifier(source, "drawable", getPackageName());

                    drawable =
                            getApplicationContext()
                                    .getResources()
                                    .getDrawable(dourceId);

                    drawable.setBounds(
                            0,
                            0,
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());

                    return drawable;
                }

            }, null));

            htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    /*
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
    */
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

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

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
            //invalidateOptionsMenu();
            if (isTimerOn) {
                menu.findItem(R.id.timeroff).setVisible(false);
                menu.findItem(R.id.timeron).setVisible(true);
            } else {
                menu.findItem(R.id.timeroff).setVisible(true);
                menu.findItem(R.id.timeron).setVisible(false);
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


    public void showProgress(final Activity activity, TextView tv, CustomProperties deviceInfoProps) {

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


    public void getReport(final Activity activity, TextView tv){
        //http://igorkourski.000webhostapp.com/sandbox/report.php?ig=kr&kr=ig
        //{"data":[{"number":1,"id":"205","ip":"10.0.0.130","time":"2019-06-16 00:00:14"},{"number":2,"id":"204","ip":"24.4.171.103","time":"2019-06-15 23:11:31"},

        ReportService ws = new ReportService(activity,deviceInfoProps);
        //String per = ws.getResponse();
        //tv.setText(per);
        new AsyncSilent(activity, ws, tv).execute();
    }

    public void showReport(final Activity activity, TextView tv){
        //http://igorkourski.000webhostapp.com/sandbox/report.php?ig=kr&kr=ig
        //{"data":[{"number":1,"id":"205","ip":"10.0.0.130","time":"2019-06-16 00:00:14"},{"number":2,"id":"204","ip":"24.4.171.103","time":"2019-06-15 23:11:31"},

        ReportService ws = new ReportService(activity,deviceInfoProps);
        //String per = ws.getResponse();
        //tv.setText(per);
        AsyncSilent task = new AsyncSilent(activity, ws, tv);
        task.execute();
        //reportProps = task.responseProps;//ws.getResponseProps();
    }

    public class AsyncSilent extends AsyncTask {

        Activity activity;
        TextView tv;
        ListView listView;

        String output;
        WebService ws;


        public AsyncSilent(Activity activity, WebService ws, TextView tv){
            this.activity = activity;
            this.tv = tv;
            this.ws = ws;
        }

        public AsyncSilent(Activity activity, WebService ws, ListView listView){
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


    }



/*
    public class CustomAsyncTask extends AsyncTask {

        Activity activity;
        TextView tv;

        ListView listView;
        ArrayList<ListItem> item;

        String output;
        WebService ws;



        public CustomAsyncTask(Activity activity, WebService ws, ListView listView){
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

            output = getWebServiceResponseData(activity,ws);
            Log("Output: " + output);
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

                    //props to array
                    BaseAdapter adapter;
                    if( ws instanceof ReportService ){
                        adapter = new CustomAdapter2(getApplicationContext(),((ReportService) ws).toArrayList(props));
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
                            if (tmp!=null) {
                                loadFragment(view, "position: " + position + " " + tmp, "");
                            }

                        }
                    });

                }
            }
        }
    }
*/

    public class GetServerData extends AsyncTask {
        public String path;
        public Activity activity;
        public TextView tv;

        //public CustomProperties props;

        public String output = "running";

        public WebService ws;

        public ProgressDialog progressDialog;

        /*
        public GetServerData(Activity activity, TextView tv, String path, CustomProperties header) {
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
            Log.log("Output: " + output);
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

    public synchronized String getWebServiceResponseData(Activity activity, WebService ws){
        String response;
        try {
            response = ws.getResponse();
        } catch (Exception e) {
            response = e.getMessage();
        }
        Log.log("Response: " + response);
        return response;
    }
/*
    public ListView showList2(final Activity activity,View page){

        ListView listView = (ListView) page.findViewById(R.id.mobile_list);

        ReportService ws = new ReportService(activity,deviceInfoProps);
            //String per = ws.getResponse();
            //tv.setText(per);
        AsyncSilent task = new AsyncSilent(activity, ws, listView);
        task.execute();

        return listView;

    }

    public ListView showList3(final Activity activity,View page){


        ListView listView = (ListView) page.findViewById(R.id.mobile_list);
        ArrayList<ListItem> item = new ArrayList<ListItem>(
                Arrays.asList(
                        new ListItem("one", new String[]{"0000", "1"}),
                        new ListItem("two", new String[]{"a", "b"}),
                        new ListItem("one", new String[]{"c", "dddddddddddddddddddd\nddddddddddddddddddddddddd\n\nddddddddddddddddd"}),
                        new ListItem("two", new String[]{"e", "f","g"}),
                        new ListItem("two",null),
                        new ListItem("last",null)

                )

        );

        CustomListAdapter adapter = new CustomListAdapter(this,item);

        //listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        //on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //String tmp = (String) adapterView.getItemAtPosition(position);
                //loadFragment(view,tmp,tmp);

                ListItem tmp0 = (ListItem) adapterView.getItemAtPosition(position);
                if (tmp0.value!=null) {

                    loadFragment(view, tmp0.key, tmp0.value);
                }

            }
        });

        return listView;

    }
*/
    public ListView showList4(final AppCompatActivity activity,View page){
        ListView listView = page.findViewById(R.id.mobile_list);

        ReportService ws = new ReportService(activity,deviceInfoProps);
        //String per = ws.getResponse();
        //tv.setText(per);
        CustomAsyncTask task = new CustomAsyncTask(activity, ws, listView);
        task.execute();

        return listView;
    }

/*
    public ListView showList(View page){

        CustomProperties reportProps = new CustomProperties();

        ListView listView = (ListView) page.findViewById(R.id.mobile_list);

        if( reportProps==null){
            return listView;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview,
                reportProps.getStringValues());//mobileArray);

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
                Object obj = ((ListView)view).getAdapter().getItem(position);
                loadFragment(view,(String) adapterView.getItemAtPosition(position),"id: " + position);

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

        return listView;
    }
*/

/*
    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

3) Get the support FragmentManager like this:

    FragmentManager fm = context.getSupportFragmentManager();

    public void loadFragment(View view, String title, String text) {
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.setText(text);
        blankFragment.setTitle(title);
        blankFragment.show(getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }

    public void loadFragment(View view, String title, String[] list) {
        BlankFragment blankFragment = new BlankFragment();

        List<String> slist = Arrays.asList(list);
        blankFragment.setList(slist);

        blankFragment.setTitle(title);
        blankFragment.show(getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }
    public void loadFragment(View view, String title, List<String> list) {
        BlankFragment blankFragment = new BlankFragment();
        blankFragment.setList(list);

        blankFragment.setTitle(title);
        blankFragment.show(getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }
    */
}
