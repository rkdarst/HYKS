package com.aware.plugin.hyks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.aware.ESM;
import com.aware.providers.ESM_Provider;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Likert;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by niels on 21/03/2017.
 */

public class ESM_Evening_Questionnaire extends BroadcastReceiver {

    boolean eveningAnsweredToday = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals("ESM_EVENING_TRIGGERED")) {
            return;
        }

        Log.d("Niels", "Trigger evening received");

        buildEveningESM(context);

//        eveningAnsweredToday = false;
//
//        // Check if evening questionnaire has been answered
//        try {
//            Calendar today = Calendar.getInstance();
//            today.set(Calendar.HOUR_OF_DAY,0);
//            today.set(Calendar.MINUTE,0);
//            today.set(Calendar.SECOND,0);
//
//            Cursor esm_data = context.getContentResolver().query(ESM_Provider.ESM_Data.CONTENT_URI, null,
//                    ESM_Provider.ESM_Data.TIMESTAMP + ">=" + today.getTimeInMillis() +
//                            " AND " + ESM_Provider.ESM_Data.TRIGGER + " LIKE 'Evening'" +
//                            " AND " + ESM_Provider.ESM_Data.STATUS + "=" + ESM.STATUS_ANSWERED, null, null);
//            if (esm_data != null) {
//                if (esm_data.getCount() > 1) {
//                    // Evening has been answered today
//                    eveningAnsweredToday = true;
//                }
//            }
//            else Log.d("Niels", "cursor is null");
//            if(esm_data != null && !esm_data.isClosed()) {
//                esm_data.close();
//            }
//        }
//        catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        // Time to launch an ESM
//        if (!eveningAnsweredToday) {
//            // Include evening questionnaire
//            buildEveningESM(context);
//        }
    }

    private void buildEveningESM(Context context) {
        try {
            ESMFactory factory = new ESMFactory();

            ESM_Likert evening_1 = new ESM_Likert();
            evening_1.setLikertMaxLabel(context.getString(R.string.esm_scale_yes))
                    .setLikertMinLabel(context.getString(R.string.esm_scale_no))
                    .setLikertStep(1)
                    .setTitle(context.getResources().getString(R.string.evening_1))
                    .setTrigger("Evening")
                    .setSubmitButton(context.getString(R.string.esm_submit))
                    .setInstructions("");
            factory.addESM(evening_1);

            ESM_Likert evening_2 = new ESM_Likert();
            evening_2.setLikertMaxLabel(context.getString(R.string.esm_scale_yes))
                    .setLikertMinLabel(context.getString(R.string.esm_scale_no))
                    .setLikertStep(1)
                    .setTitle(context.getResources().getString(R.string.evening_2))
                    .setTrigger("Evening")
                    .setSubmitButton(context.getString(R.string.esm_submit))
                    .setInstructions("");
            factory.addESM(evening_2);

            ESM_Likert evening_3 = new ESM_Likert();
            evening_3.setLikertMaxLabel(context.getString(R.string.esm_scale_yes))
                    .setLikertMinLabel(context.getString(R.string.esm_scale_no))
                    .setLikertStep(1)
                    .setTitle(context.getResources().getString(R.string.evening_3))
                    .setTrigger("Evening")
                    .setSubmitButton(context.getString(R.string.esm_submit))
                    .setInstructions("");
            factory.addESM(evening_3);

            //Queue them
            Intent queueESM = new Intent(ESM.ACTION_AWARE_QUEUE_ESM);
            queueESM.putExtra(ESM.EXTRA_ESM, factory.build());
            context.sendBroadcast(queueESM);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
