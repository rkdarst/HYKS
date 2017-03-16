package com.aware.plugin.hyks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_TTS;
import com.aware.utils.Scheduler;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by niels on 07/22/2016.
 */

public class HYKS extends AppCompatActivity {

    private TextView device_id;
    private Button join_study, set_settings, sync_data, set_schedule;

    private ArrayList<String> REQUIRED_PERMISSIONS = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_ui);

        Intent aware = new Intent(this, Aware.class);
        startService(aware);

        boolean permissions_ok = true;
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                permissions_ok = false;
                break;
            }
        }

        if (!permissions_ok) {
            Intent permissions = new Intent(this, PermissionsHandler.class);
            permissions.putExtra(PermissionsHandler.EXTRA_REQUIRED_PERMISSIONS, REQUIRED_PERMISSIONS);
            permissions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            permissions.putExtra(PermissionsHandler.EXTRA_REDIRECT_ACTIVITY,
                    getPackageName() + "/" + HYKS.class.getName());

            startActivity(permissions);
            finish();
        } else {
            Applications.isAccessibilityServiceActive(getApplicationContext());

            device_id = (TextView) findViewById(R.id.device_id);
            device_id.setText("UUID: " + Aware.getSetting(this, Aware_Preferences.DEVICE_ID));

            set_settings = (Button) findViewById(R.id.set_settings);
            set_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent settings = new Intent(getApplicationContext(), Settings.class);
                    startActivity(settings);
                }
            });

            join_study = (Button) findViewById(R.id.join_study);
            join_study.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : URL
                    //String url = "https://api.awareframework.com/index.php/webservice/index/123/123456789";

                    EditText edittext_study_url = (EditText) findViewById(R.id.edittext_study_url);
                    String url = String.valueOf(edittext_study_url.getText());
                    Aware.joinStudy(getApplicationContext(), url);

                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);

                    // Clear (local) data after it has been synced.
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_CLEAN_OLD_DATA, 4);

                    // TODO: configure ambient noise
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.STATUS_PLUGIN_AMBIENT_NOISE, true);
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.FREQUENCY_PLUGIN_AMBIENT_NOISE, 30); // in minutes
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.PLUGIN_AMBIENT_NOISE_SAMPLE_SIZE, 20); // in seconds
                    Aware.startPlugin(getApplicationContext(), "com.aware.plugin.ambient_noise");

                    Aware.startPlugin(getApplicationContext(), "com.aware.plugin.hyks");

                    Toast.makeText(getApplicationContext(), "Thanks for joining!", Toast.LENGTH_SHORT).show();
                }
            });

            sync_data = (Button) findViewById(R.id.sync_data);
            sync_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sync = new Intent(Aware.ACTION_AWARE_SYNC_DATA);
                    sendBroadcast(sync);

                    Toast.makeText(getApplicationContext(), "Syncing data...", Toast.LENGTH_SHORT).show();
                }
            });

            set_schedule = (Button) findViewById(R.id.set_schedule);
            set_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSchedule();
                    Toast.makeText(getApplicationContext(), "Starting schedule", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setSchedule() {
        try{
            Scheduler.Schedule schedule_day = new Scheduler.Schedule("schedule_day");
            schedule_day
                    .addHour(8)
                    .addHour(10)
                    .addHour(12)
                    .addHour(14)
                    .addHour(16)
                    .addHour(18)
                    .setInterval(60)
                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST)
                    .setActionIntentAction("ESM_TRIGGERED");

            Scheduler.saveSchedule(this, schedule_day);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        device_id.setText("UUID: " + Aware.getSetting(this, Aware_Preferences.DEVICE_ID));
        if (Aware.isStudy(getApplicationContext())) {
            join_study.setEnabled(false);
            set_settings.setEnabled(false);
        } else {
            sync_data.setVisibility(View.INVISIBLE);
        }
    }
}
