package com.example.pingme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String pingTitle, pingBody, pingLocation, pingToken;

        // Get data payload if any
        //if (remoteMessage.getData().size() > 0) {
        //    Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        //}
        // Get notification payload if any
        if (remoteMessage.getNotification() != null) {
            pingTitle = remoteMessage.getNotification().getTitle();
            pingBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Title: " + pingTitle);
            Log.d(TAG, "Message Notification Body: " + pingBody);
        }

        if(remoteMessage.getData().containsKey("location") && remoteMessage.getData().containsKey("token")) {
            pingLocation = remoteMessage.getData().get("location").toString();
            pingToken = remoteMessage.getData().get("token").toString();
            Log.d("Location: ", pingLocation);
            Log.d("InstanceID token: ", pingToken);
        }

        pingReceiverMessageToActivity(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("location").toString());


        //Calling method to generate notification

        //sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    //Method for generating push notification
    private void sendNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    private void pingReceiverMessageToActivity(String pingTitle, String pingBody, String pingLocation) {
        Intent intent = new Intent("pingReceiver");
        sendPingDataBroadcast(intent, pingTitle, pingBody, pingLocation);
    }

    private void sendPingDataBroadcast(Intent intent, String pingTitle, String pingBody, String pingLocation){
        intent.putExtra("title", pingTitle);
        intent.putExtra("body", pingBody);
        intent.putExtra("location", pingLocation);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}