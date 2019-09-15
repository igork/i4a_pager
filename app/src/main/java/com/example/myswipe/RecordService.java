package com.example.myswipe;

import android.app.Activity;

import com.example.myswipe.lib.CustomProperties;
import com.example.myswipe.lib.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordService extends WebService {

    String id;
    enum responseInfo {
        a,
        b,
        c,
        d,
        e
    };

    //{
    // "data":[{"name":"igork1","number":1},{"name":"igork2","number":2},{"name":"igork3","number":3}],
    // "http_x_forwarded":"2601:646:9601:1868:75b8:7503:1642:e68e","remote_addr":"2601:646:9601:1868:75b8:7503:1642:e68e",
    // "date_started":"Sunday, June 16 19 07:12:40.000",
    // "request_headers":{
    //      "Connection":"Keep-Alive","Proxy-Connection":"Keep-Alive",
    //      "HOST":"igorkourski.000webhostapp.com",
    //      "X-Forwarded-Proto":"http","X-Real-IP":"2601:646:9601:1868:75b8:7503:1642:e68e","X-Forwarded-For":"2601:646:9601:1868:75b8:7503:1642:e68e","X-Document-Root":"\/storage\/ssd4\/429\/8455429\/public_html","X-Server-Admin":"webmaster@000webhost.io","X-Server-Name":"igorkourski.000webhostapp.com","User-Agent":"Mozilla\/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko\/20100101 Firefox\/67.0","Accept":"text\/html,application\/xhtml+xml,application\/xml;q=0.9,*\/*;q=0.8","Accept-Language":"en-US,en;q=0.5","Accept-Encoding":"gzip, deflate","Upgrade-Insecure-Requests":"1"},"method":"GET","date_completed":"Sunday, June 16 19 07:12:45.000","server_addr":"2a02:4780:bad:24::397","remote_ip":"2601:646:9601:1868:75b8:7503:1642:e68e","server_ipecho":"153.92.0.23","elapsed_ms":5167,"params":{"ig":"kr","kr":"ig"},"id":207}
    static public String path = "http://igorkourski.000webhostapp.com/sandbox/record.php";

    public RecordService(Activity activity, CustomProperties headers, String path){


        //String path2 = path + "?" + "id=" + id + "&headers=1";


        super(activity,path,headers);

        this.id = id;

    }

    //{"data":[
    // {"number":1,"id":"207","ip":"2601:646:9601:1868:75b8:7503:1642:e68e","time":"2019-06-16 07:12:46"},
    // {"number":2,"id":"206","ip":"2601:646:9601:1868:75b8:7503:1642:e68e","time":"2019-06-16 07:12:39"},
    // {"number":3,"id":"205","ip":"10.0.0.130","time":"2019-06-16 00:00:14"},
    // {"number":4,"id":"204","ip":"24.4.171.103","time":"2019-06-15 23:11:31"},
    // {"number":5,"id":"203","ip":"2a02:4780:bad:8:fced:1ff:fe08:105","time":"2019-06-15 00:00:14"},
    // {"number":6,"id":"202","ip":"10.0.0.130","time":"2019-06-14 00:00:54"},
    // {"number":7,"id":"201","ip":"2a02:4780:bad:8:fced:1ff:fe08:105","time":"2019-06-12 00:00:52"},
    // {"number":8,"id":"200","ip":"2a02:4780:bad:8:fced:1ff:fe08:105","time":"2019-06-08 00:00:15"},{"number":9,"id":"199","ip":"2a02:4780:bad:8:fced:1ff:fe08:105","time":"2019-06-07 00:00:13"},{"number":10,"id":"198","ip":"2a02:4780:bad:8:fced:1ff:fe08:105","time":"2019-06-06 00:00:18"}]}

    //@Override
    public CustomProperties parseResponseProp0(String resp) {

        CustomProperties result = new CustomProperties();

        try {
            JSONObject obj = new JSONObject(resp);
            JSONArray list = obj.getJSONArray("data");

            for (int i = 0; i < list.length(); ++i) {
                JSONObject rec = list.getJSONObject(i);
                //int number = rec.getInt("number");
                int id = rec.getInt("id");
                String ip = rec.getString("ip");
                String time = rec.getString("time");

                // ...
                //result.put("" + (i + 1) + ".", "  ip:" + ip + "  time:" + time);
                result.put(String.format ("%02d", i+1),"ip:" + ip + " time:" + time + " id:" + id);

                //java8
                // list.f.forEach(arrayElement -> System.out.println(arrayElement.get("a")));
            }


        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public enum item{
        number,
        ip,
        time,
        id,
        headers
    }

    @Override
    public CustomProperties parseResponseProp(String resp) {

        CustomProperties result = new CustomProperties();

        try {
            JSONObject obj = new JSONObject(resp);
            JSONArray list = obj.getJSONArray("data");

            for (int i = 0; i < list.length(); ++i) {
                JSONObject rec = list.getJSONObject(i);
                //int number = rec.getInt("number");
                int id = rec.getInt("id");
                String ip = rec.getString("ip");
                String time = rec.getString("time");
                String headers = rec.getString("headers");

                // ...
                //result.put("" + (i + 1) + ".", "  ip:" + ip + "  time:" + time);
                CustomProperties element = new CustomProperties();
                //element.add(item.number,"" + number);
                element.add(item.ip,ip);
                element.add(item.id,"" +id);
                element.add(item.time,time);
                element.add(item.headers,headers);
                result.put(String.format ("%02d", i+1),element);

                //java8
                // list.f.forEach(arrayElement -> System.out.println(arrayElement.get("a")));
            }


        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<CustomProperties> toArrayList(CustomProperties source) {

        ArrayList<CustomProperties> result = new ArrayList<CustomProperties>();

        if (source==null || source.isEmpty()) {
            return result;
        }

        //reverse parsing
        for (int i = 0; i < source.size(); ++i) {

            Object var = source.get(String.format ("%02d", i+1));
            if (var!=null && var instanceof CustomProperties){
                result.add((CustomProperties)var);
            }

        }

        return result;
    }

    @Override
    public Object[] parseResponseArray(String resp) {

        Object[] result = new Object[0];

        try {
            JSONObject obj = new JSONObject(resp);
            JSONArray list = obj.getJSONArray("data");

            result = new Object[list.length()];

            for (int i = 0; i < list.length(); ++i) {
                JSONObject rec = list.getJSONObject(i);
                //int number = rec.getInt("number");
                int id = rec.getInt("id");
                String ip = rec.getString("ip");
                String time = rec.getString("time");

                // ...
                result[i] = "ip:" + ip + "  time:" + time + "  id:" + id;

                //java8
                // list.f.forEach(arrayElement -> System.out.println(arrayElement.get("a")));
            }


        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
