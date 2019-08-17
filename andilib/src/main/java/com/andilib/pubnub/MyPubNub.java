package com.andilib.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.google.gson.JsonObject;

import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class MyPubNub {

        PubNub pubnub;
        final String channelName = "demo_tutorial";

        public PubNub getSevice(){
            PNConfiguration pnConfiguration = new PNConfiguration();
            pnConfiguration.setSubscribeKey("sub-c-93dfd156-c05e-11e9-be0f-1ea63a606bf6");
            pnConfiguration.setPublishKey("pub-c-3557824b-f648-404b-a331-b150b7d80c3a");

            pubnub = new PubNub(pnConfiguration);
        }

        public void subscribe(){
            try {

                pubnub.addListener(new MySubscribeCallback(channelName));

                pubnub.subscribe()
                        .channels(Arrays.asList(channelName))
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void publish(){
            /* Publish a simple message to the demo_tutorial channel */
            JSONObject data = new JSONObject();

            try {
                data.put("color", "blue");
                data.put("time",getDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pubnub.publish()
                    .message(data)
                    .channel(channelName)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status)
                        {
                            System.out.println("On Response" + result.toString());
                        }
                    });
        }



    public String getDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);

    }

}
