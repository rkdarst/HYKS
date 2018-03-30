package com.aware.plugin.hyks;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aware.Aware;
import com.aware.Aware_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.aware.Aware.TAG;

public class phq9 extends Activity {

    long start_time;
    JSONObject answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start_time = System.currentTimeMillis();
        Log.d("Niels", "start time : " + start_time);
        Log.d("Niels", "start time String : " + String.valueOf(start_time));

        prepare_phq9();
    }

    private void prepare_phq9() {
        Log.d("Niels", "Preparing PHQ9!");
        setContentView(R.layout.phq9);

        final RadioGroup phq1_radio_group = (RadioGroup) findViewById(R.id.phq1_radio_group);
        final RadioGroup phq2_radio_group = (RadioGroup) findViewById(R.id.phq2_radio_group);
        final RadioGroup phq3_radio_group = (RadioGroup) findViewById(R.id.phq3_radio_group);
        final RadioGroup phq4_radio_group = (RadioGroup) findViewById(R.id.phq4_radio_group);
        final RadioGroup phq5_radio_group = (RadioGroup) findViewById(R.id.phq5_radio_group);
        final RadioGroup phq6_radio_group = (RadioGroup) findViewById(R.id.phq6_radio_group);
        final RadioGroup phq7_radio_group = (RadioGroup) findViewById(R.id.phq7_radio_group);
        final RadioGroup phq8_radio_group = (RadioGroup) findViewById(R.id.phq8_radio_group);
        final RadioGroup phq9_radio_group = (RadioGroup) findViewById(R.id.phq9_radio_group);

        final Button phq_next = (Button) findViewById(R.id.phq_next);
        phq_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Niels", "Next clicked");

                int radio_button_1 = phq1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_2 = phq2_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_3 = phq3_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_4 = phq4_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_5 = phq5_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_6 = phq6_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_7 = phq7_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_8 = phq8_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_9 = phq9_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                if (radio_button_1 != -1 && radio_button_2 != -1 && radio_button_3 != -1 && radio_button_4 != -1 && radio_button_5 != -1 &&
                        radio_button_6 != -1 && radio_button_7 != -1 && radio_button_8 != -1 && radio_button_9 != -1) {

                    String radio_button_1_answer = String.valueOf(((RadioButton) findViewById(radio_button_1)).getText());
                    String radio_button_2_answer = String.valueOf(((RadioButton) findViewById(radio_button_2)).getText());
                    String radio_button_3_answer = String.valueOf(((RadioButton) findViewById(radio_button_3)).getText());
                    String radio_button_4_answer = String.valueOf(((RadioButton) findViewById(radio_button_4)).getText());
                    String radio_button_5_answer = String.valueOf(((RadioButton) findViewById(radio_button_5)).getText());
                    String radio_button_6_answer = String.valueOf(((RadioButton) findViewById(radio_button_6)).getText());
                    String radio_button_7_answer = String.valueOf(((RadioButton) findViewById(radio_button_7)).getText());
                    String radio_button_8_answer = String.valueOf(((RadioButton) findViewById(radio_button_8)).getText());
                    String radio_button_9_answer = String.valueOf(((RadioButton) findViewById(radio_button_9)).getText());

                    // put answer to var
                    answers = new JSONObject();
                    try {
                        answers.put("PHQ1", radio_button_1_answer);
                        answers.put("PHQ2", radio_button_2_answer);
                        answers.put("PHQ3", radio_button_3_answer);
                        answers.put("PHQ4", radio_button_4_answer);
                        answers.put("PHQ5", radio_button_5_answer);
                        answers.put("PHQ6", radio_button_6_answer);
                        answers.put("PHQ7", radio_button_7_answer);
                        answers.put("PHQ8", radio_button_8_answer);
                        answers.put("PHQ9", radio_button_9_answer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    insert_db();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void insert_db() {
        ContentValues context_data;

        Log.d("Niels", "storing PHQ9");
        context_data = new ContentValues();
        context_data.put(Provider.HYKS_data.TIMESTAMP, System.currentTimeMillis());
        context_data.put(Provider.HYKS_data.START_TIME, String.valueOf(start_time));
        context_data.put(Provider.HYKS_data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        context_data.put(Provider.HYKS_data.ANSWER, answers.toString());
        getContentResolver().insert(Provider.HYKS_data.CONTENT_URI, context_data);

        // close application
        finish();
    }
}
