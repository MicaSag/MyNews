package com.android.sagot.mynews.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.android.sagot.mynews.Controllers.Activities.NotificationsActivity;
import com.android.sagot.mynews.Controllers.Activities.ResultSearchActivity;
import com.android.sagot.mynews.Controllers.Activities.SearchActivity;
import com.android.sagot.mynews.Models.Model;
import com.android.sagot.mynews.Models.NYTimesNews;
import com.android.sagot.mynews.Models.NYTimesStreams.ArticleSearch.NYTimesArticleSearch;
import com.android.sagot.mynews.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class NotificationsAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationsAlarmReceiver.class.getSimpleName();

    // Nbr article found
    int nbrArticleFound;

    // Context
    Context mContext;

    // Declare Subscription
    protected Disposable mDisposable;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        mContext = context;

        // Execute Stream " NYTimesStreams.streamFetchArticleSearch "
        executeHttpRequestWithRetrofit();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------
    // Formatting Request for Stream " NYTimesStreams.streamFetchArticleSearch "
    protected Map<String, String> formattingRequest() {
        // Create a new request and put criteria
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(Model.getInstance().getSavedModel().getSearchCriteria());
        // Display request
        request.displayQuery();
        return request.getQuery();
    }

    //  Execute Stream " NYTimesStreams.streamFetchArticleSearch "
    protected void executeHttpRequestWithRetrofit() {

        // Get api_key
        String api_key = mContext.getResources().getString(R.string.api_key);

        // Execute the stream subscribing to Observable defined inside NYTimesStreams
        mDisposable = NYTimesStreams.streamFetchArticleSearch(api_key,  formattingRequest())
                .subscribeWith(new DisposableObserver<NYTimesArticleSearch>() {
                    @Override
                    public void onNext(NYTimesArticleSearch articleSearch) {
                        Log.d(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Display a toast message
                        Log.d(TAG, "onError: ");
                        this.dispose();
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG,"On Complete !!");
                        sendNotification();
                    }
                });
    }

    private void sendNotification() {

        String title = "MyNews make you a little hello";
        String notificationDescription = "Today, "+ nbrArticleFound +" articles match your expectations.";
        if(nbrArticleFound>0) notificationDescription+=" Come quickly to consult them.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "onReceive: Android 8.0 (API level 26) and higher");
            CharSequence name = "Channel";
            String channelDescription = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My Channel", name, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManagerNew = mContext.getSystemService(NotificationManager.class);
            notificationManagerNew.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(mContext,"My Channel")
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(notificationDescription))
                    .setContentText(notificationDescription)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .build();
            notificationManagerNew.notify(1, notification);
        }else{
            Log.d(TAG, "onReceive: Other Android Version");
            Notification notification = new Notification.Builder(mContext)
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(notificationDescription))
                    .setContentText(notificationDescription)
                    .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                    .build();
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);

            //  Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
            dispose();
        }
    }

    // -----------
    //  ( OUT )
    // -----------
    //  Unsubscribe the stream when the fragment is destroyed so as not to create a memory leaks
    private void dispose(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }
}
