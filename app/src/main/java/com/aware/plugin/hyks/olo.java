package com.aware.plugin.hyks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by niels on 03/04/2017.
 */

public class olo extends Activity {
    CharSequence warning = "Muista täyttää kaikki kysymykset!";
    long start_time;
    String answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start_time = System.currentTimeMillis();
        Log.d("Niels", "start time : " + start_time);
        Log.d("Niels", "start time String : " + String.valueOf(start_time));

        prepare_olo();
    }

    private void prepare_olo() {
        Log.d("Niels", "Preparing olo!");
        setContentView(R.layout.olo_1);
    }
}
