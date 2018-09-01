package com.fahim.onlinequiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Random;


public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registrationNotification();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.action_category:
                        selectedFragment=new CategoryFragment();
                        break;
                    case R.id.action_ranking:
                        selectedFragment=new RankingFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_home,selectedFragment);
                transaction.commit();
                return true;
            }
        });
        setDefaultFragment();
    }

    private void registrationNotification() {
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("pushNotification")){
                    String message = intent.getStringExtra("message");
                    showNotification("Fahim khan",message);
                }
            }
        };
    }

    private void showNotification(String title, String message) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent contentPending = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentPending);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(),builder.build());
    }

    private void setDefaultFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout_home,CategoryFragment.newInstance());
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("registrationComplete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("pushNotification"));

    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();

    }
}
