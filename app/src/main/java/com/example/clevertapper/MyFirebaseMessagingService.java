package com.example.clevertapper;

import android.annotation.SuppressLint;
import android.app.Service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.NotificationInfo;
import com.clevertap.pushtemplates.Constants;
import com.clevertap.pushtemplates.TemplateRenderer;
import com.clevertap.pushtemplates.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Context context;
    CleverTapAPI instance;

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            context = getApplicationContext();
            if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }

                instance = CleverTapAPI.getDefaultInstance(getApplicationContext());

                boolean processCleverTapPN = Utils.isPNFromCleverTap(extras);

                if (processCleverTapPN) {
                    String pt_id = extras.getString(Constants.PT_ID);
                    if (pt_id == null || pt_id.isEmpty()) {
                        CleverTapAPI.createNotification(context, extras);
                    } else {
                        TemplateRenderer.setDebugLevel(2); //-1 for OFF, 0, for INFO, 2 for DEBUG, 3 for VERBOSE (errors)
                        TemplateRenderer.createNotification(context, extras);
                    }
                } else {
                    //Other providers
                }
            }
        } catch (Throwable throwable) {
            Log.d("Error parsing FCM payload", String.valueOf(throwable));
        }
    }
    @Override
    public void onNewToken(String token) {
        CleverTapAPI.getDefaultInstance(this).pushFcmRegistrationId(token,true);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
}

