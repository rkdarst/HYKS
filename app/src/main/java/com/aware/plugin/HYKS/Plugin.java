package com.aware.plugin.HYKS;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Aware_Provider;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Plugin extends Aware_Plugin {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Niels", "onCreate called");

        TAG = "AWARE::" + getResources().getString(R.string.app_name);

        //Any active plugin/sensor shares its overall context using broadcasts
        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
                //Broadcast your context here
            }
        };

        REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_WIFI_STATE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.BLUETOOTH);
        REQUIRED_PERMISSIONS.add(Manifest.permission.BLUETOOTH_ADMIN);
        REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_FINE_LOCATION);
        REQUIRED_PERMISSIONS.add(Manifest.permission.READ_PHONE_STATE);

        // TODO.. add required permissions

        DATABASE_TABLES = Provider.DATABASE_TABLES;
        TABLES_FIELDS = Provider.TABLES_FIELDS;
        CONTEXT_URIS = new Uri[]{Provider.HYKS_data.CONTENT_URI};

        //Activate plugin -- do this ALWAYS as the last thing (this will restart your own plugin and apply the settings)
        Aware.startPlugin(this, "com.aware.plugin.hyks");
    }

    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Niels", "onStartCommand called");

        boolean permissions_ok = true;
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                permissions_ok = false;
                break;
            }
        }

        if (permissions_ok) {
            //Check if the user has toggled the debug messages
            DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

            //Initialize our plugin's settings
            Aware.setSetting(this, Settings.STATUS_PLUGIN_HYKS, true);
        } else {
            Intent permissions = new Intent(this, PermissionsHandler.class);
            permissions.putExtra(PermissionsHandler.EXTRA_REQUIRED_PERMISSIONS, REQUIRED_PERMISSIONS);
            permissions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(permissions);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Stop AWARE
        Aware.stopAWARE();
    }
}
