package com.fahim.onlinequiz.BroadcastReciever;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.fahim.onlinequiz.MainActivity;
import com.fahim.onlinequiz.R;

/**
 * Created by HSBC on 18-02-2018.
 */

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long when=System.currentTimeMillis();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notify=new Intent(context, MainActivity.class);
        notify.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notify,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Online Quiz App!")
                .setContentText("Hey ! try to solve question Today")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000,1000});
        notificationManager.notify(0,builder.build());

    }
}
