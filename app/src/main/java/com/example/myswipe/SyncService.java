package com.example.myswipe;

import android.app.Activity;

import com.example.myswipe.lib.CustomProperties;
import com.example.myswipe.lib.WebService;

import org.json.JSONObject;

public class SyncService extends WebService {

    static final String path = "http://igorkourski.000webhostapp.com/sandbox/one.php?ig=kr&kr=ig";

    enum responseInfo {
        API,
        UserAgent,
        XServerName,
        XServerAdmin,
        ServerIP,
        RemoteIP,
        TimeStamp
    }


    public SyncService(Activity activity, CustomProperties headers){
        super(activity,path,headers);
    }

    //{
    // "data":[{"name":"igork1","number":1},{"name":"igork2","number":2},{"name":"igork3","number":3}],
    // "http_x_forwarded":"2601:646:9601:1868:75b8:7503:1642:e68e","remote_addr":"2601:646:9601:1868:75b8:7503:1642:e68e",
    // "date_started":"Sunday, June 16 19 07:12:40.000",
    // "request_headers":{
    //      "Connection":"Keep-Alive","Proxy-Connection":"Keep-Alive",
    //      "HOST":"igorkourski.000webhostapp.com",
    //      "X-Forwarded-Proto":"http","X-Real-IP":"2601:646:9601:1868:75b8:7503:1642:e68e",
    //      "X-Forwarded-For":"2601:646:9601:1868:75b8:7503:1642:e68e",
    //      "X-Document-Root":"\/storage\/ssd4\/429\/8455429\/public_html",
    //      "X-Server-Admin":"webmaster@000webhost.io",
    //      "X-Server-Name":"igorkourski.000webhostapp.com",
    //      "User-Agent":"Mozilla\/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko\/20100101 Firefox\/67.0",
    //      "Accept":"text\/html,application\/xhtml+xml,application\/xml;q=0.9,*\/*;q=0.8",
    //      "Accept-Language":"en-US,en;q=0.5","Accept-Encoding":"gzip, deflate","Upgrade-Insecure-Requests":"1"},
    //      "method":"GET",
    //      "date_completed":"Sunday, June 16 19 07:12:45.000","server_addr":"2a02:4780:bad:24::397","remote_ip":"2601:646:9601:1868:75b8:7503:1642:e68e","server_ipecho":"153.92.0.23","elapsed_ms":5167,"params":{"ig":"kr","kr":"ig"},"id":207}


    @Override
    public CustomProperties parseResponseProp(String resp){

        CustomProperties req= new CustomProperties();
        req.add(responseInfo.API,path);

        try {
            JSONObject obj = new JSONObject(resp);
            JSONObject headers = obj.getJSONObject("request_headers");

            req.add(responseInfo.UserAgent,headers.getString("User-Agent"));
            req.add(responseInfo.XServerName,headers.getString("X-Server-Name"));
            req.add(responseInfo.XServerAdmin,headers.getString("X-Server-Admin"));

            req.add(responseInfo.ServerIP,getLocation( (String)obj.get("server_ipecho")));
            req.add(responseInfo.RemoteIP,getLocation( (String)obj.get("remote_ip")));
            req.add(responseInfo.TimeStamp,(String)obj.get("date_completed"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return req;
    }

}
