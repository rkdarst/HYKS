package com.aware.plugin.hyks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by niels on 03/04/2017.
 */

public class olo extends Activity {
    CharSequence warning = "Muista täyttää kaikki kysymykset!";
    long start_time;
    HashMap<String, String> answers = new HashMap<>();

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

//        final RadioGroup v1_radio_group = (RadioGroup) findViewById(R.id.v1_radio_group);
        final Button v1_next = (Button) findViewById(R.id.v1_next);
        v1_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int radio_button_1 = v1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_1 = 1; //if -1, no radio button was checked

                if (radio_button_1 != -1) {
//                    RadioButton RB_radio_button_1 = (RadioButton) findViewById(radio_button_1);
//                    String radio_button_1_answer = String.valueOf(RB_radio_button_1.getText());


                    // put answer to var
                    answers.put("V1", "...");

                    prepare_olo_2();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_2() {
        Log.d("Niels", "Preparing olo 2!");
        setContentView(R.layout.olo_2);

        final Button v2_next = (Button) findViewById(R.id.v2_next);
        v2_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int radio_button_1 = v1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_1 = 1; //if -1, no radio button was checked

                if (radio_button_1 != -1) {
                    // put answer to var
                    answers.put("V2", "...");

                    prepare_olo_3();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_3() {
        Log.d("Niels", "Preparing olo 3!");
        setContentView(R.layout.olo_3);

        final Button v3_next = (Button) findViewById(R.id.v3_next);
        v3_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int radio_button_1 = v1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_1 = 1; //if -1, no radio button was checked

                if (radio_button_1 != -1) {
                    // put answer to var
                    answers.put("V3", "...");

                    prepare_olo_4();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void prepare_olo_4() {
        Log.d("Niels", "Preparing olo 4!");
        setContentView(R.layout.olo_4);

        final Button v4_next = (Button) findViewById(R.id.v4_next);
        v4_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int radio_button_1 = v1_radio_group.getCheckedRadioButtonId(); //if -1, no radio button was checked
                int radio_button_1 = 1; //if -1, no radio button was checked

                if (radio_button_1 != -1) {
                    // put answer to var
                    answers.put("V4", "...");

                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
