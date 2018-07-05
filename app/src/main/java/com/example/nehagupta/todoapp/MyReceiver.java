package com.example.nehagupta.todoapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    int request_code=0;

    @Override
    public void onReceive(Context context, Intent intent) {


        request_code++;
        long id=intent.getLongExtra("id_alarm",0);
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel("mychannel","Expenses Channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"mychannel");
        builder.setContentTitle("Todo");
        builder.setContentText("get details");
        builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
        Intent intent1=new Intent(context,DescribeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent1.putExtra("id",id);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,request_code,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification= builder.build();
        manager.notify(1,notification);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
