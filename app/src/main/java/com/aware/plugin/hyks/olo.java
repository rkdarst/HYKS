package com.aware.plugin.hyks;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.aware.Aware;
import com.aware.Aware_Preferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by niels on 03/04/2017.
 */

public class olo extends Activity {
    long start_time;
    //HashMap<String, String> answers = new HashMap<>();
    JSONObject answers = new JSONObject();

    static void lickert_answer(JSONObject a, String key, float ans) throws JSONException {
        if (ans == 0) {
            a.put(key, JSONObject.NULL);
            return;
        }
        a.put(key, ans-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start_time = System.currentTimeMillis();
        Log.d("Niels", "start time : " + start_time);
        Log.d("Niels", "start time String : " + String.valueOf(start_time));

        prepare_olo_1();
    }

    private void prepare_olo_1() {
        Log.d("Niels", "Preparing olo 1!");
        setContentView(R.layout.olo_1);

        final Button v1_next = (Button) findViewById(R.id.v1_next);

        final RatingBar rb_olo_1_1 = (RatingBar) findViewById(R.id.rb_olo_1_1);
        final RatingBar rb_olo_1_2 = (RatingBar) findViewById(R.id.rb_olo_1_2);
        final RatingBar rb_olo_1_3 = (RatingBar) findViewById(R.id.rb_olo_1_3);
        final RatingBar rb_olo_1_4 = (RatingBar) findViewById(R.id.rb_olo_1_4);
        final RatingBar rb_olo_1_5 = (RatingBar) findViewById(R.id.rb_olo_1_5);
        final RatingBar rb_olo_1_6 = (RatingBar) findViewById(R.id.rb_olo_1_6);
        final RatingBar rb_olo_1_7 = (RatingBar) findViewById(R.id.rb_olo_1_7);
        final RatingBar rb_olo_1_8 = (RatingBar) findViewById(R.id.rb_olo_1_8);


        v1_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //int radio_button_1 = olo1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                // If unset, these are 0.0.  If set, some nmuber.
                if (true) {
                    //String radio_button_1_answer = String.valueOf(((RadioButton) findViewById(radio_button_1)).getText());
                    // put answer to var
                    try {
                        JSONArray olo1 = new JSONArray();
                        lickert_answer(answers, "olo_1_1", rb_olo_1_1.getRating());
                        lickert_answer(answers, "olo_1_2", rb_olo_1_2.getRating());
                        lickert_answer(answers, "olo_1_3", rb_olo_1_3.getRating());
                        lickert_answer(answers, "olo_1_4", rb_olo_1_4.getRating());
                        lickert_answer(answers, "olo_1_5", rb_olo_1_5.getRating());
                        lickert_answer(answers, "olo_1_6", rb_olo_1_6.getRating());
                        lickert_answer(answers, "olo_1_7", rb_olo_1_7.getRating());
                        lickert_answer(answers, "olo_1_8", rb_olo_1_8.getRating());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prepare_olo_2();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_2() {
        Log.d("Niels", "Preparing olo 2!");
        setContentView(R.layout.olo_2);

        final RatingBar rb_olo_2_1 = (RatingBar) findViewById(R.id.rb_olo_2_1);
        final RatingBar rb_olo_2_2 = (RatingBar) findViewById(R.id.rb_olo_2_2);
        final RatingBar rb_olo_2_3 = (RatingBar) findViewById(R.id.rb_olo_2_3);
        final RadioGroup olo2_4_radio_group = (RadioGroup) findViewById(R.id.olo2_4_radio_group);
        final Button v2_next = (Button) findViewById(R.id.v2_next);

        v2_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int radio_button_1 = olo2_4_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                if (radio_button_1 != -1) {
                    // put answer to var
                    float v2_1_answer = rb_olo_2_1.getRating();
                    float v2_2_answer = rb_olo_2_2.getRating();
                    float v2_3_answer = rb_olo_2_3.getRating();
                    String olo2_4_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_1)).getText());

                    //answers.put("OLO2", v2_1_answer + ";" + v2_2_answer + ";" + v2_3_answer  + ";" + olo2_4_radio_group_answer);
                    try {
                        answers.put("olo_2_1", v2_1_answer);
                        answers.put("olo_2_2", v2_2_answer);
                        answers.put("olo_2_3", v2_3_answer);
                        answers.put("olo_2_4", olo2_4_radio_group_answer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prepare_olo_3();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_3() {
        Log.d("Niels", "Preparing olo 3!");
        setContentView(R.layout.olo_3);

        final RatingBar rb_olo_3_1 = (RatingBar) findViewById(R.id.rb_olo_3_1);
        final RadioGroup olo3_2_radio_group = (RadioGroup) findViewById(R.id.olo3_2_radio_group);
        final RadioGroup olo3_3_radio_group = (RadioGroup) findViewById(R.id.olo3_3_radio_group);
        final LinearLayout ll_olo_3_a = (LinearLayout) findViewById(R.id.ll_olo_3_a);
        final LinearLayout ll_olo_3_b = (LinearLayout) findViewById(R.id.ll_olo_3_b);

        final RatingBar rb_olo_3_3_1_3 = (RatingBar) findViewById(R.id.rb_olo_3_3_1_3);
        final RadioGroup olo3_3_1_5_radio_group = (RadioGroup) findViewById(R.id.olo3_3_1_5_radio_group);
        final RatingBar rb_olo_3_3_1_2 = (RatingBar) findViewById(R.id.rb_olo_3_3_1_2);
        final RatingBar rb_olo_3_3_1_4 = (RatingBar) findViewById(R.id.rb_olo_3_3_1_4);
        final RatingBar rb_olo_3_3_2_1 = (RatingBar) findViewById(R.id.rb_olo_3_3_2_1);

        olo3_3_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                Log.d("Niels", "Radio button selected:" + String.valueOf(radioButton.getText()));

                String radioSelected = String.valueOf(radioButton.getText());

                if (checkedId == R.id.olo_3_3_1) { //(radioSelected.equals("Kyllä")) {
                    // Show Olo_3_3_2_1
                    ll_olo_3_a.setVisibility(View.GONE);
                    ll_olo_3_b.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.olo_3_3_2) { //(radioSelected.equals("Ei")) {
                    // Show Olo_3_3_1_1 - Olo_3_3_1_5
                    ll_olo_3_a.setVisibility(View.VISIBLE);
                    ll_olo_3_b.setVisibility(View.GONE);
                }
                else {
                    ll_olo_3_a.setVisibility(View.GONE);
                    ll_olo_3_b.setVisibility(View.GONE);
                }
            }
        });

        final Button v3_next = (Button) findViewById(R.id.v3_next);
        v3_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int radio_button_1 = olo3_3_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                int radio_button_olo3_2 = olo3_2_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo3_3 = olo3_3_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                if (radio_button_1 != -1 && radio_button_olo3_2 != -1 && radio_button_olo3_3 != -1) {
                    int radio_button_olo3_1_5 = olo3_3_1_5_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                    String radioSelected = String.valueOf(((RadioButton) findViewById(radio_button_1)).getText());
                    float v3_1_answer = rb_olo_3_1.getRating();
                    String olo3_2_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo3_2)).getText());
                    String olo3_3_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo3_3)).getText());

                    //answers.put("OLO3", v3_1_answer + ";" + olo3_2_radio_group_answer + ";" + olo3_3_radio_group_answer);
                    JSONArray olo3 = new JSONArray();
                    try {
                        answers.put("olo_3_1", v3_1_answer);
                        answers.put("olo_3_2", olo3_2_radio_group_answer);
                        answers.put("olo_3_3", olo3_3_radio_group_answer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        // Clear old answers
                        answers.put("olo_3_3_2_1", null);
                        answers.put("olo_3_3_1_3", null);
                        answers.put("olo_3_1_5", null);
                        answers.put("olo_3_3_1_2", null);
                        answers.put("olo_3_3_1_4", null);
                        if (radioSelected.equals(getResources().getString(R.string.olo_3_3_1))) { //(radioSelected.equals("Kyllä")) {
                            float v3_3_2_1_answer = rb_olo_3_3_2_1.getRating();
                            answers.put("olo_3_3_2_1", v3_3_2_1_answer);
                        } else if (radioSelected.equals(getResources().getString(R.string.olo_3_3_2))) {
                            float v3_3_1_3_answer = rb_olo_3_3_1_3.getRating();
                            String olo3_1_5_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo3_1_5)).getText());
                            float v3_3_1_2_answer = rb_olo_3_3_1_2.getRating();
                            float v3_3_1_4_answer = rb_olo_3_3_1_4.getRating();
                            //answers.put("OLO3_2", v3_3_1_3_answer + ";" + olo3_1_5_radio_group_answer + ";" + v3_3_1_2_answer + ";" + v3_3_1_4_answer);
                            answers.put("olo_3_3_1_3", v3_3_1_3_answer);
                            answers.put("olo_3_1_5", olo3_1_5_radio_group_answer);
                            answers.put("olo_3_3_1_2", v3_3_1_2_answer);
                            answers.put("olo_3_3_1_4", v3_3_1_4_answer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    prepare_olo_4();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_4() {
        Log.d("Niels", "Preparing olo 4!");
        setContentView(R.layout.olo_4);

        final RadioGroup olo4_1_radio_group = (RadioGroup) findViewById(R.id.olo4_1_radio_group);
        final RadioGroup olo4_2_radio_group = (RadioGroup) findViewById(R.id.olo4_2_radio_group);
        final RadioGroup olo4_3_radio_group = (RadioGroup) findViewById(R.id.olo4_3_radio_group);
        final RadioGroup olo4_4_radio_group = (RadioGroup) findViewById(R.id.olo4_4_radio_group);
        final RadioGroup olo4_5_radio_group = (RadioGroup) findViewById(R.id.olo4_5_radio_group);
        final RadioGroup olo4_6_radio_group = (RadioGroup) findViewById(R.id.olo4_6_radio_group);
        final RadioGroup olo4_7_radio_group = (RadioGroup) findViewById(R.id.olo4_7_radio_group);
        final RadioGroup olo4_7_1_radio_group = (RadioGroup) findViewById(R.id.olo4_7_1_radio_group);
        final LinearLayout ll_olo_4_a = (LinearLayout) findViewById(R.id.ll_olo_4_a);

        olo4_7_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                Log.d("Niels", "Radio button selected:" + String.valueOf(radioButton.getText()));

                String radioSelected = String.valueOf(radioButton.getText());

                if (checkedId == R.id.olo_4_7_1) { //(radioSelected.equals("Kyllä")) {
                    // Show ll_olo_4_a
                    olo4_7_1_radio_group.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.olo_4_7_2) { //(radioSelected.equals("Ei")) {
                    // Hide ll_olo_4_a
                    olo4_7_1_radio_group.setVisibility(View.GONE);
                } else {
                    olo4_7_1_radio_group.setVisibility(View.GONE);
                }
            }
        });

        final Button v4_next = (Button) findViewById(R.id.v4_next);
        v4_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int radio_button_1 = olo4_7_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                int radio_button_olo4_1 = olo4_1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_2 = olo4_2_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_3 = olo4_3_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_4 = olo4_4_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_5 = olo4_5_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_6 = olo4_6_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_olo4_7 = olo4_7_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked

                if (radio_button_1 != -1 && radio_button_olo4_1 != -1 && radio_button_olo4_2 != -1 && radio_button_olo4_3 != -1 &&
                        radio_button_olo4_4 != -1 && radio_button_olo4_5 != -1 && radio_button_olo4_6 != -1 && radio_button_olo4_7 != -1 ) {
                    String olo4_1_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_1)).getText());
                    String olo4_2_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_2)).getText());
                    String olo4_3_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_3)).getText());
                    String olo4_4_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_4)).getText());
                    String olo4_5_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_5)).getText());
                    String olo4_6_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_6)).getText());
                    String olo4_7_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_7)).getText());

                    String radioSelected = String.valueOf(((RadioButton) findViewById(radio_button_1)).getText());


                    //answers.put("OLO4", olo4_1_radio_group_answer + ";" + olo4_2_radio_group_answer + ";" + olo4_3_radio_group_answer + ";" + olo4_4_radio_group_answer +
                    //        olo4_5_radio_group_answer + ";" + olo4_6_radio_group_answer + ";" + olo4_7_radio_group_answer);
                    try {
                        answers.put("olo_4_1", olo4_1_radio_group_answer);
                        answers.put("olo_4_2", olo4_2_radio_group_answer);
                        answers.put("olo_4_3", olo4_3_radio_group_answer);
                        answers.put("olo_4_4", olo4_4_radio_group_answer);
                        answers.put("olo_4_5", olo4_5_radio_group_answer);
                        answers.put("olo_4_6", olo4_6_radio_group_answer);
                        answers.put("olo_4_7", olo4_7_radio_group_answer);
                        answers.put("olo_4_7_1", null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (radioSelected.equals(getResources().getString(R.string.olo_3_3_1))) {
                        int radio_button_olo4_7_1 = olo4_7_1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                        if (radio_button_olo4_7_1 == -1) {
                            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        String olo4_7_1_radio_group_answer = String.valueOf(((RadioButton) findViewById(radio_button_olo4_7_1)).getText());

                        try {
                            answers.put("olo_4_7_1", olo4_7_1_radio_group_answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    insert_db();

                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.warning_missing_answer), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void insert_db() {
        ContentValues context_data;

        JSONObject packet = new JSONObject();
        try {
            packet.put("olo", 0);  // identifier and version id
            packet.put("answers", answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Niels", "storing olo");
        context_data = new ContentValues();
        context_data.put(Provider.HYKS_data.TIMESTAMP, System.currentTimeMillis());
        context_data.put(Provider.HYKS_data.START_TIME, String.valueOf(start_time));
        context_data.put(Provider.HYKS_data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
        context_data.put(Provider.HYKS_data.ANSWER, packet.toString());
        getContentResolver().insert(Provider.HYKS_data.CONTENT_URI, context_data);

        // close application
        finish();
    }
}
