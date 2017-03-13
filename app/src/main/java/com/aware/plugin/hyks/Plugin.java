package com.aware.plugin.hyks;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_Plugin;

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

        // TODO: check writing to external storage
        REQUIRED_PERMISSIONS.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        DATABASE_TABLES = Provider.DATABASE_TABLES;
        TABLES_FIELDS = Provider.TABLES_FIELDS;
        CONTEXT_URIS = new Uri[]{Provider.HYKS_data.CONTENT_URI};

        Aware.startPlugin(this, "com.aware.plugin.hyks");
    }

    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (PERMISSIONS_OK) {
            //Check if the user has toggled the debug messages
            DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

            // TODO: configure ambient noise
            Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.STATUS_PLUGIN_AMBIENT_NOISE, true);
            Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.FREQUENCY_PLUGIN_AMBIENT_NOISE, 30); // in minutes
            Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.PLUGIN_AMBIENT_NOISE_SAMPLE_SIZE, 20); // in seconds
            Aware.startPlugin(getApplicationContext(), "com.aware.plugin.ambient_noise");

            //Initialize our plugin's settings
            Aware.setSetting(this, Settings.STATUS_PLUGIN_HYKS, true);

            Aware.startAWARE(this);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Stop AWARE
        Aware.stopAWARE(this);
    }
}
