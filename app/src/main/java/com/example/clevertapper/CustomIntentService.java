package com.example.clevertapper;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.sdk.CTNotificationIntentService;
import com.clevertap.android.sdk.Logger;

public class CustomIntentService extends IntentService {
    public final static String TYPE_BUTTON_CLICK = "com.clevertap.ACTION_BUTTON_CLICK";
    public CustomIntentService() { super("CTNotificationIntentService"); }
    @Override protected void onHandleIntent(Intent intent) { Bundle extras = intent.getExtras();
    if (extras == null) return; String type = extras.getString("ct_type");
    if (type != null && TYPE_BUTTON_CLICK.equals(type))
    { Logger.v("CTNotificationIntentService handling " + TYPE_BUTTON_CLICK); customActionButtonClickHandling(extras);
    } else { Logger.v("CTNotificationIntentService: unhandled intent "+intent.getAction()); } }
    private void customActionButtonClickHandling(Bundle extras) {
        Log.d("CTA",extras.toString());
        } }