package com.example.clevertapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.clevertap.android.sdk.CTExperimentsListener;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements DisplayUnitListener, CTInboxListener, CTExperimentsListener {

    private CleverTapAPI cleverTapDefaultInstance;
    Button app_inbox_button;
    public FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
    // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);




        super.onCreate(savedInstanceState);
        //Clevertap

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        cleverTapDefaultInstance.setDisplayUnitListener(this);


        Button button_fire;
        Button push_profile;
        final EditText eventiptxt;
        final EditText eventprop;
        final EditText eventpropval;
        Button getCTIDbutton;
        Button login;
        Button native_display;


        final EditText name;
        final EditText email;
        final EditText phone;
        final EditText id;


        setContentView(R.layout.activity_main);

        eventiptxt = (EditText) findViewById(R.id.eventiptxt);
        eventprop = (EditText) findViewById(R.id.eventprop);
        eventpropval = (EditText) findViewById(R.id.eventpropval);
        button_fire = (Button) findViewById(R.id.button_fire);
        native_display =(Button) findViewById(R.id.native_display);


        getCTIDbutton = (Button) findViewById(R.id.getctidbutton);
        final TextView ctid = (TextView) findViewById(R.id.ctidtext);

        login = (Button) findViewById(R.id.loginbutton);
        name = (EditText) findViewById(R.id.uname);
        email = (EditText) findViewById(R.id.emailtxt);
        phone = (EditText) findViewById(R.id.phonetxt);
        id = (EditText) findViewById(R.id.identity);

        app_inbox_button = findViewById(R.id.app_inbox_button);
        push_profile = findViewById(R.id.push_profile);




        //Firebase App_remove


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);   //Set Log level to DEBUG log warnings or other important messages

        //--------LOCATION---------
        Location location = cleverTapDefaultInstance.getLocation();
        cleverTapDefaultInstance.setLocation(location);

        cleverTapDefaultInstance.enableDeviceNetworkInfoReporting(true);


        cleverTapDefaultInstance.createNotificationChannel(getApplicationContext(), "112233", "test", "testchannel", NotificationManager.IMPORTANCE_MAX, true);

        cleverTapDefaultInstance.createNotificationChannel(getApplicationContext(), "123456", "123456", "Your Channel Description", NotificationManager.IMPORTANCE_MAX, true);
// Creating a Notification Channel With Sound Support
        cleverTapDefaultInstance.createNotificationChannel(getApplicationContext(), "got", "Game of Thrones", "Game Of Thrones", NotificationManager.IMPORTANCE_MAX, true, "gameofthrones.mp3");

// How to add a sound file to your app : https://developer.clevertap.com/docs/add-a-sound-file-to-your-android-app

        //APP inbox
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();


        button_fire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // event with properties
                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put(String.valueOf(eventprop.getText().toString()), eventpropval.getText().toString());

                cleverTapDefaultInstance.pushEvent(eventiptxt.getText().toString(), prodViewedAction);

            }
        });

        getCTIDbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // event with properties
                ctid.setText(cleverTapDefaultInstance.getCleverTapID());
                

            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // event with properties
                // each of the below mentioned fields are optional
// if set, these populate demographic information in the Dashboard
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Name", name.getText().toString());                  // String
                profileUpdate.put("Identity", id.getText().toString());                    // String or number
                profileUpdate.put("Email", email.getText().toString());               // Email address of the user
                profileUpdate.put("Phone", phone.getText().toString());// Phone (with the country code, starting with +)
                profileUpdate.put("CT_date_time", new Date());


                cleverTapDefaultInstance.onUserLogin(profileUpdate);


            }
        });

        push_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Name", name.getText().toString());                  // String
                profileUpdate.put("Identity", id.getText().toString());                    // String or number
                profileUpdate.put("Email", email.getText().toString());               // Email address of the user
                profileUpdate.put("Phone", phone.getText().toString());                 // Phone (with the country code, starting with +)



                cleverTapDefaultInstance.pushProfile(profileUpdate);
            }
        });

        native_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleverTapDefaultInstance.pushEvent("Native Display Event");

            }
        });



    }

    public void inboxDidInitialize() {

        app_inbox_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> tabs = new ArrayList<>();
                tabs.add("Promotions");
                tabs.add("Offers");
                //We support upto 2 tabs only. Additional tabs will be ignored

                CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
                styleConfig.setTabBackgroundColor("#a6a6a6");//provide Hex code in string ONLY
                styleConfig.setSelectedTabIndicatorColor("#00b300");
                styleConfig.setSelectedTabColor("#d1d1e0");
                styleConfig.setUnselectedTabColor("#FFFFFF");
                styleConfig.setBackButtonColor("#FFFFFF");
                styleConfig.setNavBarTitleColor("#FFFFFF");
                styleConfig.setNavBarTitle("MY INBOX");
                styleConfig.setNavBarColor("#000000");
                styleConfig.setInboxBackgroundColor("#bfbfbf");

                cleverTapDefaultInstance.showAppInbox(styleConfig); //Opens activity with Tabs

            }
        });
    }

    @Override
    public void inboxMessagesDidUpdate(){

    }

    public void CTExperimentsUpdated() {
      /*
        //your code here
        //AB TEST

        final TextView abtest = (TextView) findViewById(R.id.abtest);
        final String stringVar = cleverTapDefaultInstance.getStringVariable("stringVar","default");
        abtest.setText(stringVar);
        Log.d("ABTEST_log", "test");


    */
    }


    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        // you will get display units here
        for (int i = 0; i < units.size(); i++) {
            CleverTapDisplayUnit unit = units.get(i);
            Log.d("Unit ID", unit.getUnitID());

            showDialog(unit);

        }
    }


    private void showDialog(CleverTapDisplayUnit unit) {

        ArrayList<CleverTapDisplayUnitContent> tr = unit.getContents();
        String title = unit.getContents().get(0).getTitle();

        String message = unit.getContents().get(0).getMessage();
        final String update_url = unit.getCustomExtras().get("url");

        Log.d("Test Title", title);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setCancelable(false);
        builder.setPositiveButton(message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(update_url));
                MainActivity.this.startActivity(browserIntent);
            }
        });

        builder.create().show();
    }
}



