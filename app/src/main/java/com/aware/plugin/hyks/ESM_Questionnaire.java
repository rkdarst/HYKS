package com.aware.plugin.hyks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aware.ESM;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Freetext;
import com.aware.ui.esms.ESM_Likert;
import com.aware.ui.esms.ESM_Radio;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by niels on 10/03/2017.
 */

public class ESM_Questionnaire extends BroadcastReceiver {

    boolean morningAnsweredToday = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Niels", "Trigger received");

        // Time to launch an ESM
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 11 && !morningAnsweredToday) {
            // Include morning questionnaire
            buildESM(true, context);
        }
        else {
            buildESM(false, context);
            // Include only the regular questionnaire

        }
    }

    // Set morning answered today to true if the question was answered.

    private void buildESM(boolean morning, Context context) {
        try {
            ESMFactory factory = new ESMFactory();

            if(morning) {
                ESM_Freetext esmFreetext = new ESM_Freetext();
                esmFreetext.setTitle("Freetext")
                        .setTrigger("How did you sleep?")
                        .setSubmitButton("OK")
                        .setInstructions("Morning-only question");

                // Add morning question to the queue
                factory.addESM(esmFreetext);
            }

            ESM_Radio esmRadio = new ESM_Radio();
            esmRadio.addRadio("Option 1")
                    .addRadio("Option 2")
                    .setTitle("Radio question")
                    .setSubmitButton("OK");
            factory.addESM(esmRadio);

            ESM_Likert esmLikert = new ESM_Likert();
            esmLikert.setLikertMax(5)
                    .setLikertMaxLabel("Great")
                    .setLikertMinLabel("Poor")
                    .setLikertStep(1)
                    .setTitle("Likert question")
                    .setSubmitButton("OK");
            factory.addESM(esmLikert);

            //Queue them
            ESM.queueESM(context, factory.build());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
