package com.aware.plugin.hyks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by niels on 21/03/2017.
 */

public class ESM_PHQ  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals("ESM_PHQ_TRIGGERED")) {
            return;
        }

        Intent resultIntent = new Intent(context, phq9.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_aware_esm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_stat_aware_esm))
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle("Kyselylomake odottaa vastaustasi")
                .setContentText("Avaa lomake vastataksesi kyselyyn");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Will display the notification in the notification bar
        notificationManager.notify(710, notification.build());
    }
}
