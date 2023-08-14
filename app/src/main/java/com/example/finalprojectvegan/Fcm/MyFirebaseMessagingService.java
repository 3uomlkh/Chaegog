package com.example.finalprojectvegan.Fcm;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_ONE_SHOT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalprojectvegan.CommentActivity;
import com.example.finalprojectvegan.MainActivity;
import com.example.finalprojectvegan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private static String CHANNEL_ID = "My channel ID";
    private String to;

    @Override
    public void onNewToken(String to) {
        super.onNewToken(to);
        this.to = to;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: ");

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "onMessageReceived: 1");
            Log.d(TAG, remoteMessage.getNotification().getClickAction());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),
                    remoteMessage.getNotification().getClickAction());
        } else if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: 2");
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String click_action = remoteMessage.getNotification().getClickAction();
            sendNotification(title, body, click_action);
        }

//        notificationManager
//                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("message");
//
//        createNotificationChannel();
//        sendNotification(title, body);
//        Intent intent = new Intent(this, CommentActivity.class);
//        NotificationManager notificationManager
//                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Random random = new Random();
//        int notificationID = random.nextInt();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(notificationManager);
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(remoteMessage.getData().get("title"))
//                        .setContentText(remoteMessage.getData().get("message"))
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//        notificationManager.notify(notificationID, notificationBuilder.build());

//        Log.d(TAG, remoteMessage.getNotification().getTitle());
//        Log.d(TAG, remoteMessage.getNotification().getBody());

//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();

//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("message");
//        Log.d(TAG, title);
//        Log.d(TAG, body);

        //createNotificationChannel();
        //sendNotification(title, body);

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());
//        sendNotification(remoteMessage.getNotification().getBody());

    }
    public void sendNotification(String title, String body, String click_action) {
        Log.d(TAG, "sendNotification: ");

        Intent intent = new Intent(this, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("POSTSDocumentId", click_action);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "test_channel";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
