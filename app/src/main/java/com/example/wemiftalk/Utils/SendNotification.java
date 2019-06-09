package com.example.wemiftalk.Utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification (String message, String heading, String notificationKey){

        notificationKey = "5af1c589-b96c-4d3f-8d2a-a4ecd0539671"; // id mojego nr do wyslania probnego powiadomienia
        try {

            JSONObject notificationContent = new JSONObject("{'contents' :{'en' : '" + message + "'}," +
                    "'include_player_ids':['" + notificationKey + "']." +
                    "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent, null);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
