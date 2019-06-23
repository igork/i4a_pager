package com.example.myswipe.lib;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.location.Location;

import com.example.myswipe.DeviceService;

import javax.net.ssl.HttpsURLConnection;

public class WebService {

        Activity activity;
        Location location;
        CustomizedProperties header;
        String apiUrl;


        private static final String TAG = WebService.class.getName();

        private static CustomizedProperties req = new CustomizedProperties();
        enum responseInfo {
            API,
            UserAgent,
            XServerName,
            XServerAdmin,
            ServerIP,
            RemoteIP,
            TimeStamp
        };

        public WebService(Activity activity, String apiUrl, Location location){

            this.activity = activity;
            this.apiUrl = apiUrl;
            this.location = location;

        }

        public WebService(Activity activity, String apiUrl, CustomizedProperties header){

            this.activity = activity;
            this.apiUrl = apiUrl;
            this.header = header;

        }

        protected void setHeader(HttpURLConnection myURLConnection){
            //String userCredentials = "username:password";
            //String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            //myURLConnection.setRequestProperty ("Authorization", basicAuth);
            String androidId = DeviceService.getAndroidId(activity);
            String uni = DeviceService.getUUI(activity);
            String utn = DeviceService.getUTN(activity);

            String log = "adnroid=" + androidId + " uni=" + uni + " utn=" + utn;
            Log.i(TAG,log);

            myURLConnection.setRequestProperty("androidId",androidId);
            myURLConnection.setRequestProperty("uui",uni);
            myURLConnection.setRequestProperty("utn",utn);

            /*
            Location location = mumayankAir.getLocation(activity,airLocation);
            if (location!=null) {
                Log.i(TAG, " LOCATION latitude=" + location.getLatitude() + "   longtitude=" + location.getLongitude());
                myURLConnection.setRequestProperty("latitude", "" + location.getLatitude());
                myURLConnection.setRequestProperty("longitude", "" + location.getLongitude());
            } else {
                Log.i(TAG, " LOCATION location == null");
            }
            */
            if (location!=null) {
                myURLConnection.setRequestProperty(CustomizedHeader.latitude.getName(), "" + location.getLatitude());
                myURLConnection.setRequestProperty(CustomizedHeader.longitude.getName(), "" + location.getLongitude());
                myURLConnection.setRequestProperty(CustomizedHeader.address.getName(), "" + location.getProvider());
            }

            //myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //myURLConnection.setRequestProperty("Content-Language", "en-US");
            myURLConnection.setUseCaches(false);
            myURLConnection.setDoInput(true);
            myURLConnection.setDoOutput(true);

            return;
        }

    // input: getResponse for <syncCall> & <headerd>
    // output: put in output
/*
    protected static String getWebServiceResponse(String path) {

        String result = null;
        String response = getResponse(path);

        //parse
        if (response != null) {
            try {
                result = parseResponse(output);
                Log.i(TAG, result);

            } catch (Exception e) {
                Log.i(TAG, "Parsing exeption");
                e.printStackTrace();
            }
        }
        return result;
    }
    */
        public String getResponse() {

            StringBuffer response = null;
            String output = null;

            try {

                URL url = new URL(apiUrl);
                Log.i(TAG, apiUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                setHeader(conn);

                int responseCode = conn.getResponseCode();

                Log.i(TAG, "" + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String line;
                    response = new StringBuffer();

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                }
            } catch(Exception e){
                e.printStackTrace();
                Log.i(TAG,e.getMessage());
            }

            if (response!=null) {
                //output = response.toString();
                //Call ServerData() method to getResponse webservice and store result in response
                //  response = service.ServerData(syncCall, postDataParams);

                Log.i(TAG, response.toString());

                //output = parseResponse(output);
                output = parseResponseProp(response.toString()).toString();

                Log.i(TAG, output);
            }

            /*
            try {
                JSONArray jsonarray = new JSONArray(output);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    int getUUI = jsonobject.getInt("getUUI");
                    String country = jsonobject.getString("countryName");
                    Log.d(TAG, "getUUI:" + getUUI);
                    Log.d(TAG, "country:" + country);
                    Country countryObj=new Country(getUUI,country);
                    countries.add(countryObj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
            return output;
        }

        public CustomizedProperties parseResponseProp(String resp){

            //String result = "API:" + syncCall + "\n";
            CustomizedProperties req= new CustomizedProperties();
            req.add(responseInfo.API,apiUrl);

            try {
                JSONObject obj = new JSONObject(resp);
                JSONObject headers = obj.getJSONObject("request_headers");

                req.add(responseInfo.UserAgent,headers.getString("User-Agent"));
                req.add(responseInfo.XServerName,headers.getString("X-Server-Name"));
                req.add(responseInfo.XServerAdmin,headers.getString("X-Server-Admin"));

                //result += getValue(headers,CustomizedHeader.latitude);
                //result += getValue(headers,CustomizedHeader.longitude);
                //result += getValue(headers,CustomizedHeader.address);

                req.add(responseInfo.ServerIP,getLocation( (String)obj.get("server_ipecho")));
                req.add(responseInfo.RemoteIP,getLocation( (String)obj.get("remote_ip")));
                req.add(responseInfo.TimeStamp,(String)obj.get("date_completed"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return req;
        }

        private static String getValue(JSONObject headers, String key){
            String value = "Unknown";
            try {
                value = headers.getString(key);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            return value;
        }

        private static String getValue(JSONObject headers, CustomizedHeader header){
            String value = null;
            try {
                value = headers.getString(header.getName());
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            return value!=null?(header.getName() + value + "\n"):"";
        }

        //input:    http://api.ipstack.com/24.4.171.103?access_key=7682f6b026f812bf69229df86390d990
        //output    ip=....  <city>  <region>
        protected String getLocation(String ip){
            String result = ip;

            if (ip!=null && !ip.equalsIgnoreCase("Unknown")) {
                String path = "http://api.ipstack.com/" + ip + "?access_key=7682f6b026f812bf69229df86390d990";
                String response = getResponse(path);

                //parse
                if (response != null) {
                    try {
                        JSONObject obj = new JSONObject(response);

                        result += "   " + (String) obj.get("city");
                        result += ", " + (String) obj.get("region_name");

                    } catch (Exception e) {
                        Log.d(TAG, "Parsing exeption"); // e.printStackTrace());
                    }
                }
            }
            return result;
        }
        protected static String getResponse(String path){

            URL url = null;
            StringBuffer response = null;

            try {

                url = new URL(path);
                Log.i(TAG, "Server: " + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                Log.i(TAG, "Response code: " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();

                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    in.close();
                }
                if( response!=null ){

                }
            } catch(Exception e){
                e.printStackTrace();
            }

            String result = response!=null?response.toString():null;
            Log.i(TAG, "Response: " + result);

            return result;
        }

}
