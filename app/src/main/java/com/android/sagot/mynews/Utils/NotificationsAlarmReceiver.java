package com.android.sagot.mynews.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.sagot.mynews.R;

public class NotificationsAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationsAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        // Description of the notification
        int nbrArticle;
        
         // Execute Stream " NYTimesStreams.streamFetchArticleSearch "
         executeHttpRequestWithRetrofit();
        
        getEXTRA("NBR_ARTICLE_FOUND",nbrArticleFound);
        
        String description = "Today, "+ nbrArticleFound +"articles match your expectations. Come quickly to consult them.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "onReceive: Android 8.0 (API level 26) and higher");
            CharSequence name = "Channel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My Channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManagerNew = context.getSystemService(NotificationManager.class);
            notificationManagerNew.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context,"My Channel")
                    .setContentTitle("TITRE")
                    .setContentText(description)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .build();
            notificationManagerNew.notify(1, notification);
        }else{
            Log.d(TAG, "onReceive: Other Android Version");
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("TITRE")
                    .setContentText(description)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }
    }
}
