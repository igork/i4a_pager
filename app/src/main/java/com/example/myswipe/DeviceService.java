package com.example.myswipe;

//UTN
//This solution needs to request for android.permission.READ_PHONE_STATE

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.content.Context;

//Secure
import android.provider.Settings.Secure;

//UUI
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.util.Log;

import com.example.myswipe.lib.CustomProperties;
import com.example.myswipe.lib.Log;
import com.location.aravind.getlocation.GeoLocator;

public class DeviceService {

    private static final String TAG = DeviceService.class.getName();

    public enum deviceInfo {
        address,
        latitude,
        longitude,
        androidId,
        //appId, //uui,
        utn,
        OS,
        network,
        model
    }


    // https://medium.com/@ssaurel/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb

    //Unique Telephony Number (IMEI, MEID, ESN, IMSI)
    //This solution needs to request for android.permission.READ_PHONE_STATE
    // <uses-permission android:name="android.permission.READ_PHONE_STATE" />

    public synchronized static String getUTN(Context context) {

        try {
            TelephonyManager telephonyManager;

            telephonyManager = (TelephonyManager) context.getSystemService(Context.
                    TELEPHONY_SERVICE);

            // getDeviceId() returns the unique device ID.
            // For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            String deviceId = telephonyManager.getDeviceId();

            //getSubscriberId() returns the unique subscriber ID,
            //For example, the IMSI for a GSM phone.
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            String subscriberId = telephonyManager.getSubscriberId();

            return deviceId + " - " + subscriberId;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    //require <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    public synchronized static String getPhoneNumber(Context context){
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        //String phoneNumber = tMgr.getLine1Number();
        return null;
    }

    //Secure Android ID
    public synchronized static String getAndroidId(Context context){

        //String androidId = Settings.Secure.getString(getContentResolver(),
        //       Settings.Secure.ANDROID_ID);

        return  Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    //get UUI
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getUUI(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply(); // //editor.commit(); - immideatelly
            }
        }

        return uniqueID;
    }

    public synchronized static String getAndroidVersion(Context context){

        //https://stackoverflow.com/questions/10547818/how-to-find-the-android-version-name-programmatically

        StringBuilder builder = new StringBuilder();
        builder.append("").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                    builder.append(" ").append(fieldName).append(" ");
                    //builder.append("sdk: ").append(fieldValue);
                } else {
                    try {
                        fieldValue = field.getInt(new Object());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (fieldValue == Build.VERSION.SDK_INT) {
                        builder.append(" ").append(fieldName).append(" ");
                        builder.append("sdk: ").append(fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        /*
        try {
            if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                codeName = field.getName();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        */
        return builder.toString();
    }

    public synchronized static String getModel(Context context){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }
    /*
    public synchronized static Properties getHeader(Context context) {
        Properties prop = new Properties();

        prop.setProperty("uui",getUUI(context));
        prop.setProperty("androidId",getAndroidId(context));
        prop.setProperty("utn",getUTN(context));

        return prop;
    }
    */
    public static CustomProperties getInfo(Context context, Activity activity) {

        CustomProperties prop = new CustomProperties();
        String unknown = "unknown";
        Location location = new Location(unknown);

        try {
            GeoLocator geoLocator = new GeoLocator(context, activity);
            //update location
            String address = geoLocator.getAddress();
            //refresh static
            if (address != null && !address.isEmpty()) {
                location.setProvider(address);
            }
            location.setLatitude(geoLocator.getLattitude());
            location.setLongitude(geoLocator.getLongitude());

            adjustLocation(location);

            //update prop
            prop.clear();
            prop.add(deviceInfo.latitude, "" + location.getLatitude());
            prop.add(deviceInfo.longitude, "" + location.getLongitude());
            if (!location.getProvider().equalsIgnoreCase(unknown))
                prop.add(deviceInfo.address, location.getProvider());
        } catch (Exception e) {
            /*
            e.printStackTrace();
            //DEBUG
            location.setLatitude(15.15+Math.random()*0.5);
            location.setLongitude(18.18+Math.random()*1.5);
            location.setProvider("debug addr");

            //update prop
            prop.clear();
            prop.add(deviceInfo.latitude, "" + location.getLatitude());
            prop.add(deviceInfo.longitude, "" + location.getLongitude());
            if (!location.getProvider().equalsIgnoreCase(unknown))
                prop.add(deviceInfo.address, location.getProvider());
                */

        } finally {


        }

        try {
            prop.add(deviceInfo.network, networkConnection(context, activity));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            prop.add(deviceInfo.OS, DeviceService.getAndroidVersion(context));
            prop.add(deviceInfo.model, DeviceService.getModel(context));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            String androidId = DeviceService.getAndroidId(activity);
            String uui = DeviceService.getUUI(activity);
            String utn = DeviceService.getUTN(activity);

            String log = "adnroid=" + androidId + " uui=" + uui + " utn=" + utn;
            Log.log(log);

            prop.add(deviceInfo.androidId,androidId);
            //prop.add(deviceInfo.appId,uui);
            prop.add(deviceInfo.utn,utn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;
    }

    public static void adjustLocation(Location location){

        class Loc{
            public double lati;
            public double longi;
            public String addr;
            public Loc( double lati, double longi, String addr){
                this.lati = lati;
                this.longi = longi;
                this.addr = addr;
            }
            public boolean isCloseTo(Location location){
                //TO DO: change to distance
                return Math.abs(location.getLatitude() - this.lati) < 0.003 &&
                       Math.abs(location.getLongitude() - this.longi) < 0.003;
            }

        }
        Loc[] black = {
                new Loc(37.2288315,-121.836275,"700 Colleen Dr, San Jose, CA 95123, USA")
        };

        for (int i=0; i<black.length; i++) {
            if (black[i].isCloseTo(location)) {

                location.setLatitude(black[i].lati);
                location.setLongitude(black[i].longi);
                location.setProvider(black[i].addr);

            }
        }
    }

    //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    public static String networkConnection(Context context, Activity activity) {
        String connected = "no network";
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) &&
                (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            connected = "mobile, wi-fi";
        } else {

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {

                connected = "mobile";
            }
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                connected = "wi-fi";
            }
        }
        return connected;
    }

}
