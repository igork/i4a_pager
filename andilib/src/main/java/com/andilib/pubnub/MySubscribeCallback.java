package com.andilib.pubnub;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;

public class MySubscribeCallback extends SubscribeCallback {

    final String channelName;

    public MySubscribeCallback(String name){
        channelName = name;
    }

    @Override
    public void signal(PubNub pubnub, PNSignalResult result){
        System.out.println("SIGNAL: category:" + result.getMessage());
    }


    @Override
    public void status(PubNub pubnub, PNStatus status) {
        if (status.getCategory() == PNStatusCategory.PNUnknownCategory) {
            System.out.println(status.getErrorData());
        }
        System.out.println("STATUS: category:" + status.getCategory().name());


/*
        JsonObject messageJsonObject = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        messageJsonObject.addProperty("igor", " some data " + 777);


        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            // This event happens when radio / connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

            // Connect event. You can do stuff like publish, and know you'll get it.
            // Or just use the connected event to confirm you are subscribed for
            // UI / internal notifications, etc

            if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // Check whether request successfully completed or not.
                        if (!status.isError()) {

                            // Message successfully published to specified channel.
                        }
                        // Request processing failed.
                        else {

                            // Handle message publish error. Check 'category' property to find out possible issue
                            // because of which request did fail.
                            //
                            // Request can be resent using: [status retry];
                        }
                    }
                });
            }
        }
        else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

            // Happens as part of our regular operation. This event happens when
            // radio / connectivity is lost, then regained.
        }
        else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

            // Handle messsage decryption error. Probably client configured to
            // encrypt messages and on live data feed it received plain text.
        }
        */
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        JsonElement msg = message.getMessage();
        System.out.println("MESSAGE: " + msg);

        /*

        // Handle new message stored in message.message
        if (message.getChannel() != null) {
            // Message has been received on channel group stored in
            // message.getChannel()
        }
        else {
            // Message has been received on channel stored in
            // message.getSubscription()
        }

        JsonElement receivedMessageObject = message.getMessage();
        System.out.println("Received message content: " + receivedMessageObject.toString());
        // extract desired parts of the payload, using Gson
        String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
        System.out.println("msg content: " + msg);

        */

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        System.out.println("PRESENCE: channel:" + presence.getChannel() + " event:" + presence.getEvent());
    }

}
