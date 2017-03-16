package com.aware.plugin.hyks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.aware.ESM;
import com.aware.providers.ESM_Provider;
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
        if(!intent.getAction().equals("ESM_TRIGGERED")) {
            return;
        }

        Log.d("Niels", "Trigger received");

        morningAnsweredToday = false;

        // Check if morning questionnaire has been answered
        try {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY,0);
            today.set(Calendar.MINUTE,0);
            today.set(Calendar.SECOND,0);

            Cursor esm_data = context.getContentResolver().query(ESM_Provider.ESM_Data.CONTENT_URI, null,
                    ESM_Provider.ESM_Data.TIMESTAMP + ">=" + today.getTimeInMillis() +
                            " AND " + ESM_Provider.ESM_Data.TRIGGER + " LIKE 'Morning'" +
                            " AND " + ESM_Provider.ESM_Data.STATUS + "=" + ESM.STATUS_ANSWERED, null, null);
            if (esm_data != null) {
                if (esm_data.getCount() > 1) {
                    // Morning has been answered today
                    morningAnsweredToday = true;
                }
            }
            else Log.d("Niels", "cursor is null");
            if(esm_data != null && !esm_data.isClosed()) {
                esm_data.close();
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Time to launch an ESM
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 11 && !morningAnsweredToday) {
            // Include morning questionnaire
            buildESM(true, context);
        }
        else {
            // Include only the regular questionnaire
            buildESM(false, context);
        }
    }

    private void buildESM(boolean morning, Context context) {
        try {
            ESMFactory factory = new ESMFactory();

            if(morning) {
                ESM_Freetext esmFreetext = new ESM_Freetext();
                esmFreetext.setTitle("Freetext")
                        .setTrigger("Morning")
                        .setSubmitButton("OK")
                        .setInstructions("Morning-only question");

                // Add morning question to the queue
                factory.addESM(esmFreetext);
            }

            ESM_Radio esmRadio = new ESM_Radio();
            esmRadio.addRadio("Option 1")
                    .addRadio("Option 2")
                    .setInstructions("")
                    .setTitle("Radio question")
                    .setSubmitButton("OK");
            factory.addESM(esmRadio);

            ESM_Likert esmLikert = new ESM_Likert();
            esmLikert.setLikertMax(5)
                    .setLikertMaxLabel("Great")
                    .setLikertMinLabel("Poor")
                    .setLikertStep(1)
                    .setInstructions("")
                    .setTitle("Likert question")
                    .setSubmitButton("OK");
            factory.addESM(esmLikert);

            //Queue them
            //ESM.queueESM(context, factory.build());
            Intent queueESM = new Intent(ESM.ACTION_AWARE_QUEUE_ESM);
            queueESM.putExtra(ESM.EXTRA_ESM, factory.build());
            context.sendBroadcast(queueESM);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
