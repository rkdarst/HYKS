package com.aware.plugin.hyks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aware.ESM;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Radio;

import org.json.JSONException;

/**
 * Created by niels on 03/04/2017.
 */

public class ESM_Random_Questionnaire extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals("ESM_RANDOM_TRIGGERED")) {
            return;
        }

        Intent resultIntent = new Intent(context, olo.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_aware_esm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_stat_aware_esm))
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.olo_notification_title))
                .setContentText(context.getString(R.string.olo_notification_text));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Will display the notification in the notification bar
        notificationManager.notify(710, notification.build());
    }
}