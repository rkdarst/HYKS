package com.aware.plugin.hyks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.aware.Aware;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //Plugin settings in XML @xml/preferences
    public static final String STATUS_PLUGIN_HYKS = "status_plugin_hyks";
    public static final String START_HOUR = "setting_start_hour";
    public static final String END_HOUR = "setting_end_hour";

    //Plugin settings UI elements
    private static CheckBoxPreference status;
    private static EditTextPreference start_hour;
    private static EditTextPreference end_hour;


    /**
     * State of Google's Activity Recognition plugin
     */
    public static final String STATUS_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION = "status_plugin_google_activity_recognition";

    /**
     * Frequency of Google's Activity Recognition plugin in seconds<br/>
     * By default = 60 seconds
     */
    public static final String FREQUENCY_PLUGIN_GOOGLE_ACTIVITY_RECOGNITION = "frequency_plugin_google_activity_recognition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        int START_HOUR_DEFAULT = getResources().getInteger(R.integer.default_start_time);
        int END_HOUR_DEFAULT = getResources().getInteger(R.integer.default_end_time);

        super.onResume();

        status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_HYKS);
        if (Aware.getSetting(this, STATUS_PLUGIN_HYKS).length() == 0) {
            Aware.setSetting(this, STATUS_PLUGIN_HYKS, true); //by default, the setting is true on install
        }
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_HYKS).equals("true"));

        start_hour = (EditTextPreference) findPreference(START_HOUR);
        if (Aware.getSetting(this, START_HOUR).length() == 0) {
            Aware.setSetting(this, START_HOUR, START_HOUR_DEFAULT);
        }
        start_hour.setSummary(getString(R.string.config_usual_wakeup_hour) + Aware.getSetting(getApplicationContext(), START_HOUR));
        end_hour = (EditTextPreference) findPreference(END_HOUR);
        if (Aware.getSetting(this, END_HOUR).length() == 0) {
            Aware.setSetting(this, END_HOUR, END_HOUR_DEFAULT);
        }
        end_hour.setSummary(getString(R.string.config_usual_bedtime_hour) + Aware.getSetting(getApplicationContext(), END_HOUR));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        int START_HOUR_DEFAULT = getResources().getInteger(R.integer.default_start_time);
        int END_HOUR_DEFAULT = getResources().getInteger(R.integer.default_end_time);

        Preference setting = findPreference(key);
        if (setting.getKey().equals(STATUS_PLUGIN_HYKS)) {
            Aware.setSetting(this, key, sharedPreferences.getBoolean(key, false));
            status.setChecked(sharedPreferences.getBoolean(key, false));
        }
        if (Aware.getSetting(this, STATUS_PLUGIN_HYKS).equals("true")) {
            Aware.startPlugin(getApplicationContext(), "com.aware.plugin.hyks");
        } else {
            Aware.stopPlugin(getApplicationContext(), "com.aware.plugin.hyks");
        }
        if (setting.getKey().equals(START_HOUR)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, String.valueOf(START_HOUR_DEFAULT)));
            setting.setSummary(getString(R.string.config_usual_wakeup_hour) + Aware.getSetting(getApplicationContext(), START_HOUR));
        }
        if (setting.getKey().equals(END_HOUR)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, String.valueOf(END_HOUR_DEFAULT)));
            setting.setSummary(getString(R.string.config_usual_bedtime_hour) + Aware.getSetting(getApplicationContext(), END_HOUR));
        }
    }
}
