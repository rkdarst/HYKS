package com.aware.plugin.hyks;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Scheduler_Provider;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_TTS;
import com.aware.utils.Scheduler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static java.lang.Math.round;

/**
 * Created by niels on 07/22/2016.
 */

public class HYKS extends AppCompatActivity {

    static Context appContext;

    private TextView device_id;
    private TextView version_id;
    private Button join_study, set_settings, sync_data, set_schedule, uninstall;

    private ArrayList<String> REQUIRED_PERMISSIONS = new ArrayList<>();

    static String SERVICE_SETTINGS_RUNNER = "HYKS_SETTINGS_RUNNER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();

        setContentView(R.layout.main_ui);

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
            checkAppStatus();

            device_id = (TextView) findViewById(R.id.device_id);
            device_id.setText("UUID: " + Aware.getSetting(this, Aware_Preferences.DEVICE_ID));

            String version = "";
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            version_id = (TextView) findViewById(R.id.version_id);
            version_id.setText("app version: " + version);

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
                    //String url = "https://api.awareframework.com/index.php/webservice/index/123/123456789";

                    EditText edittext_study_url = (EditText) findViewById(R.id.edittext_study_url);
                    String url = String.valueOf(edittext_study_url.getText());
                    if (url.length() < 20) {
                        if (!url.matches("[0-9a-fA-F]{16}")) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    R.string.core_secret_id_error,
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        if (!deviceidChecksumValid(url)) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    R.string.core_secret_id_checksum_error,
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        url = "https://aware.koota.zgib.net/index.php/aware/v1/" + url.toLowerCase() + "?crt_sha256=436669a4920ac08623357aaca77c935bbc3bf3906a9c962eb822edff021fcc42&crt_url=https%3A%2F%2Fdata.koota.cs.aalto.fi%2Fstatic%2Fserver-aware.crt";
                    }
                    Aware.joinStudy(getApplicationContext(), url);

                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);
                    //Aware.setSetting(getApplicationContext(), Aware_Preferences.WEBSERVICE_WIFI_ONLY, true);

                    // Probes
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_COMMUNICATION_EVENTS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_CALLS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_MESSAGES, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_INSTALLATIONS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NOTIFICATIONS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_GPS, 600);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, true);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_LOCATION_NETWORK, 600);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.MIN_LOCATION_GPS_ACCURACY, 0);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.LOCATION_EXPIRATION_TIME, 0);
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.LOCATION_SAVE_ALL, true);

                    // Clear (local) data after it has been synced.
                    Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_CLEAN_OLD_DATA, 4);

                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.STATUS_PLUGIN_AMBIENT_NOISE, true);
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.PLUGIN_AMBIENT_NOISE_NO_RAW, true);
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.FREQUENCY_PLUGIN_AMBIENT_NOISE, 30); // in minutes
                    Aware.setSetting(getApplicationContext(), com.aware.plugin.ambient_noise.Settings.PLUGIN_AMBIENT_NOISE_SAMPLE_SIZE, 20); // in seconds
                    Aware.startPlugin(getApplicationContext(), "com.aware.plugin.ambient_noise");

                    Aware.startPlugin(getApplicationContext(), "com.aware.plugin.hyks");

                    Toast.makeText(getApplicationContext(), R.string.core_thanks_for_joining, Toast.LENGTH_SHORT).show();
                }
            });

            sync_data = (Button) findViewById(R.id.sync_data);
            sync_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sync = new Intent(Aware.ACTION_AWARE_SYNC_DATA);
                    sendBroadcast(sync);

                    Toast.makeText(getApplicationContext(), R.string.core_syncing_data, Toast.LENGTH_SHORT).show();
                }
            });

            set_schedule = (Button) findViewById(R.id.set_schedule);
            set_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSchedule(appContext);
                    Toast.makeText(getApplicationContext(), R.string.core_starting_schedule, Toast.LENGTH_SHORT).show();
                }
            });

            uninstall = (Button) findViewById(R.id.uninstall);
            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri packageURI = Uri.parse("package:"+getPackageName());
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                    startActivity(uninstallIntent);
                }
            });

        }
    }

    /**
     * This method checks all the standard things to make sure that the app is still running.
     */
    public void checkAppStatus() {
        Applications.isAccessibilityServiceActive(getApplicationContext());
        sendBroadcast(new Intent(Aware.ACTION_AWARE_PRIORITY_FOREGROUND));
        Aware.isBatteryOptimizationIgnored(getApplicationContext(), getPackageName());
    }

    @Override
    public void onStart() {
        super.onStart();
        checkAppStatus();
    }

    static int jsonMin(JSONArray array) {
        int min = 99;
            for (int i = 0; i < array.length(); i++) {
                try {
                    min = Math.min(min, array.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return min;
    }
    static int jsonMax(JSONArray array) {
        int max = -1;
        for (int i = 0; i < array.length(); i++) {
            try {
                max = Math.max(max, array.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return max;
    }

    /*
     * Compare a schedule to the expected parameters.  If it is not set up correctly, return false.
     * This is used to check if a schedule needs recreating.
     */
    static boolean isScheduleCorrect(Context context, String schedule_id, Integer startHour, Integer endHour) {
        try {
            Scheduler.Schedule schedule = Scheduler.getSchedule(context, schedule_id);
            if (schedule == null
                    || (startHour != null && jsonMin(schedule.getHours()) != startHour)
                    || (endHour   != null && jsonMax(schedule.getHours()) != endHour)
                    ) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static class HYKS_Settings_Runner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("HYKS", "schedule runner: received intent");
            if (!intent.getAction().equals(HYKS.SERVICE_SETTINGS_RUNNER)) {
                return;
            }
            Log.d("HYKS", "schedule runner: running");
            // The following doesn't work because this is a static context and setSchedule is non-static.
            //setSchedule();
            if (HYKS.appContext != null) {
                setSchedule(HYKS.appContext);
            } else {
                Log.e("HYKS", "HYKS_Settings_Runner was called and appContext was null.  Aborting.");
            }
        }
    }

    public static class HYKS_Settings_Runner2 extends IntentService {
        public HYKS_Settings_Runner2() {
            super("HYKS schedule runner2");
        }

        protected void onHandleIntent(@Nullable Intent intent) {
            Log.d("HYKS", "settings runner 2: onHandleIntent");
            setSchedule(getApplicationContext());
        }
    }


    /**
     * This is an idempotent method which processes current schedule data.
     *
     * @param context the app context
     */
    private static void setSchedule(Context context) {
        if (context == null) {
            Log.e("HYKS", "setSchedule called with null context");
            return;
        }
        Log.d("HYKS", "setSchedule: running");
        //Context context = getApplicationContext();
        //Context context = HYKS.appContext;
        //Context context = getApplication().getApplicationContext();

        int startHour = context.getResources().getInteger(R.integer.default_start_time);
        int endHour   = context.getResources().getInteger(R.integer.default_end_time);
        String startHourStr = Aware.getSetting(context, Settings.START_HOUR);
        String endHourStr   = Aware.getSetting(context, Settings.END_HOUR);
        if (startHourStr.length() > 0)  startHour = Integer.parseInt(startHourStr);
        if (endHourStr.length()   > 0)  endHour   = Integer.parseInt(endHourStr);
        // Adjust end hour.  If stop time is 20:00, the last full hour is 19, etc.
        endHour = endHour - 1;


        // Background schedule checker
        Scheduler.Schedule schedule_runner = Scheduler.getSchedule(context, "schedule_settings");
        if (schedule_runner == null) {
            Log.d("HYKS", "setting schedule runner");
            try {
                schedule_runner = new Scheduler.Schedule("schedule_settings");
                schedule_runner
                        .setInterval(30)
                        .setActionType(Scheduler.ACTION_TYPE_SERVICE)
                        .setActionClass(context.getPackageName()+"/"+HYKS.HYKS_Settings_Runner2.class.getName());
                Scheduler.saveSchedule(context, schedule_runner);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Morning schedule
        if (Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")) {
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_morning%'", null);
            Log.d("HYKS", "Morning schedule: Deleting");
        }
        if (! Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")
                && ! isScheduleCorrect(context, "schedule_morning%", startHour, startHour+1)) {
            Log.d("HYKS", "Morning schedule: Deleting and setting");
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_morning%'", null);
            try {
                Scheduler.Schedule schedule_morning = new Scheduler.Schedule("schedule_morning");
                schedule_morning
                        .random(1, 30)
                        .addHour(startHour)
                        .addHour(startHour + 1)
                        .setActionType(Scheduler.ACTION_TYPE_BROADCAST)
                        .setActionIntentAction("ESM_MORNING_TRIGGERED");
                Scheduler.saveSchedule(context, schedule_morning);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        // Evening schedule
        if (Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")) {
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_evening%'", null);
            Log.d("HYKS", "Evening schedule: Deleting");
        }
        if (! Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")
                && ! isScheduleCorrect(context, "schedule_evening%", endHour-1, endHour)) {
            Log.d("HYKS", "Evening schedule: Deleting and setting");
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_evening%'", null);
            try {
                Scheduler.Schedule schedule_evening = new Scheduler.Schedule("schedule_evening");
                schedule_evening
                        .random(1, 30)
                        .addHour(endHour-1)
                        .addHour(endHour)
                        .setActionType(Scheduler.ACTION_TYPE_BROADCAST)
                        .setActionIntentAction("ESM_EVENING_TRIGGERED");

                Scheduler.saveSchedule(context, schedule_evening);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_olo%'", null);
//        try {
//            Scheduler.Schedule schedule_random1 = new Scheduler.Schedule("schedule_olo_fixed");
//            schedule_random1.setInterval(1).setActionType(Scheduler.ACTION_TYPE_BROADCAST).setActionIntentAction("ESM_RANDOM_TRIGGERED");
//            Scheduler.saveSchedule(context, schedule_random1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        // Random schedule
        int olo1_start = startHour + 2;
        int olo3_end = endHour - 2;
        int olo2_start = (int) round(olo1_start + (olo3_end - olo1_start) / 3.);
        int olo3_start = (int) round(olo1_start + 2 * (olo3_end - olo1_start) / 3.);
        int olo1_end = olo2_start - 1;
        int olo2_end = olo3_start - 1;
        if (Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")) {
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_olo%'", null);
            Log.d("HYKS", "Olo schedules: Deleting");
        }
        if (! Aware.getSetting(context, Settings.DAILY_QUESTIONS).equals("false")
                && ! isScheduleCorrect(context, "schedule_olo1%", olo1_start, olo1_end)) {
            Log.d("HYKS", "Olo schedules: Deleting and setting");
            // Delete already existing random schedules
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_olo%'", null);
            // Set new schedule
            try {
                Scheduler.Schedule schedule_random1 = new Scheduler.Schedule("schedule_olo1");
                Scheduler.Schedule schedule_random2 = new Scheduler.Schedule("schedule_olo2");
                Scheduler.Schedule schedule_random3 = new Scheduler.Schedule("schedule_olo3");
                schedule_random1.random(1, 30).addHour(olo1_start).addHour(olo1_end).setActionType(Scheduler.ACTION_TYPE_BROADCAST).setActionIntentAction("ESM_RANDOM_TRIGGERED");
                schedule_random2.random(1, 30).addHour(olo2_start).addHour(olo2_end).setActionType(Scheduler.ACTION_TYPE_BROADCAST).setActionIntentAction("ESM_RANDOM_TRIGGERED");
                schedule_random3.random(1, 30).addHour(olo3_start).addHour(olo3_end).setActionType(Scheduler.ACTION_TYPE_BROADCAST).setActionIntentAction("ESM_RANDOM_TRIGGERED");
                Scheduler.saveSchedule(context, schedule_random1);
                Scheduler.saveSchedule(context, schedule_random2);
                Scheduler.saveSchedule(context, schedule_random3);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        // PHQ9 schedule
        if (Aware.getSetting(context, Settings.MONTHLY_QUESTIONS).equals("false")) {
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_biweekly%'", null);
            Log.d("HYKS", "Biweekly schedules: Deleting");
        }
        if (! Aware.getSetting(context, Settings.MONTHLY_QUESTIONS).equals("false")
                && ! isScheduleCorrect(context, "schedule_biweekly%", startHour, endHour)) {
            Log.d("HYKS", "Biweekly schedule: Deleting and setting");
            context.getContentResolver().delete(Scheduler_Provider.Scheduler_Data.CONTENT_URI, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " LIKE 'schedule_biweekly%'", null);
            try {
                Scheduler.Schedule schedule_biweekly = new Scheduler.Schedule("schedule_biweekly");
                schedule_biweekly
                        .setInterval(40320)
                        .setActionType(Scheduler.ACTION_TYPE_BROADCAST)
                        .setActionIntentAction("ESM_PHQ_TRIGGERED");
                for (int hour = startHour; hour <= endHour; hour++) {
                    schedule_biweekly.addHour(hour);
                }
                Scheduler.saveSchedule(context, schedule_biweekly);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        //checkAppStatus();
    }

    /*
     * The Koota checksuming algorithm.  This is a two-digit base16 lund algorithm, basically.
     */
    static public boolean deviceidChecksumValid(String device_id) {
        int factor = 2;
        int sum = 0;     // Running checksum
        int base = 16;
        int digits = 2;  // number of checksum digits
        int base2 = (int)Math.pow(base, digits);

        sum = Integer.parseInt(device_id.substring(device_id.length()-digits), base);
        device_id = device_id.substring(0, device_id.length()-digits);
        for (int i = device_id.length()-1 ; i >= 0 ; i--) {
            int addend = factor * Integer.parseInt(device_id.substring(i, i+1), base);
            factor = 2 - factor + 1;
            addend = (addend / base2) + (addend % base2);
            sum += addend;
        }
        int remainder = sum % base2;
        return remainder == 0;
    }
}
