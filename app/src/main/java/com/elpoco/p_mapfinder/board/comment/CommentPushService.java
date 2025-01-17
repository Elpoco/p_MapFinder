package com.elpoco.p_mapfinder.board.comment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.elpoco.p_mapfinder.G;
import com.elpoco.p_mapfinder.IntroActivity;
import com.elpoco.p_mapfinder.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.util.Map;

import static android.app.Notification.DEFAULT_ALL;
import static android.app.Notification.DEFAULT_SOUND;

public class CommentPushService extends FirebaseMessagingService {

    Notification notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Map<String, String> datas = remoteMessage.getData();

        Uri uri=Uri.parse("android.resource://" + getPackageName() +"/" + R.raw.mococo_seed);

        if (datas != null) {
            String name = datas.get("boardName");
            String msg = datas.get("nickName");

            NotificationCompat.Builder builder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "ch01";
                String channelName = "Channel 01";
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
                builder = new NotificationCompat.Builder(this, channelId);
            } else {
                builder = new NotificationCompat.Builder(this, null);
            }
            builder.setSmallIcon(R.drawable.icon_app)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app))
                    .setContentTitle(name)
                    .setContentText(msg)
                    .setAutoCancel(true);
            if(G.isSound) builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mococo_seed));
            if(!G.isVibrate) builder.setVibrate(new long[] {0,0,0,0});

            Intent intent = new Intent(this, IntroActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1010, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            notification=builder.build();

            notificationManager.notify(100, notification);
        }
    }
}
