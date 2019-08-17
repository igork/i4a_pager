package com.andilib.pubnub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.google.gson.JsonObject;

import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;

public class MainActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        MyPubNub pn = new MyPubNub();
        ////////////////////////

        //000000000
        /*
        for( int i=0; i<5; i++) {
            publish("" + i);
        }
        */

        /*
        https://admin.pubnub.com/#/user/517801/account/517759/app/35313219/key/664247/block/51030/editor/49832

        function react
         */


        pn.subscribe();

        for (int i=0; i<5; i++) {
            pn.publish();
        }



    }




    public void publish000(String iper){

        JsonObject position = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        position.addProperty("igor", getDate() + " some data " + iper);

        pubnub.publish()
                .message(position)
                .channel("pubnub_onboarding_channel")
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle response
                    }
                });

    }



}
