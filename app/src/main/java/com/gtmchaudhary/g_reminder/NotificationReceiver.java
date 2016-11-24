package com.gtmchaudhary.g_reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by gtmchaudhary on 10/23/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    String TAG = "myReminderApp";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Generate Notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.fab_edit_icon)
                        .setContentTitle(intent.getStringExtra("label"))
                        .setAutoCancel(false)
                        .setOngoing(true);
        // Sets an ID for the notification
        int mNotificationId = intent.getIntExtra("notificationID",1947);
        String label_reminder = intent.getStringExtra("label");
        Log.d(TAG, mNotificationId + "-" + label_reminder);

        //OnClick of notification
        Intent resultIntent = new Intent(context, ViewReminder.class);
        resultIntent.putExtra("notificationID", mNotificationId);
        resultIntent.putExtra("label",label_reminder);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        Intent.FLAG_ACTIVITY_NEW_TASK
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
