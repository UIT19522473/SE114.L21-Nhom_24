package com.example.trelloreal.Notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.trelloreal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reminder extends BroadcastReceiver {
    public static final String CHANNEL_ID = "channel_1";
    private  clNotify clNotify;
    private String User;
    private String idCard;
    private String idTable;
    private String nameTable;
    private String nameCard;
    private String code_start;
    private com.example.trelloreal.Card.clCard clCard;

    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Toi trong receiver", "xin chao");

        Bundle bundle = intent.getExtras();
        code_start = bundle.getString("code_start");
        User = bundle.getString("user");
        idTable = bundle.getString("IdTable");
        idCard = bundle.getString("IdCard");
        nameTable = bundle.getString("nameTable");
        nameCard = bundle.getString("nameCard");
        int id = bundle.getInt("Id");

        clNotify = new clNotify("",User,idTable,idCard,nameTable,nameCard,"","","");

        if(code_start.equals("0")){
            Date now = new Date();
            String strDate = sdfDate.format(now);

            clNotify.setIdNotify(idCard);
            clNotify.setTitle("Sự kiện đã bắt đầu");
            clNotify.setMess("Sự kiện: "+nameCard+" trong bảng "+ nameTable+" của bạn đã bắt đầu.");
            clNotify.setTime(strDate);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "channel_1")
                    .setSmallIcon(R.drawable.ic_on_notify)
                    .setContentTitle("Sự kiện đã bắt đầu")
                    .setContentText("Sự kiện: "+nameCard+" trong bảng "+ nameTable+" của bạn đã bắt đầu.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(id, notification.build());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" +User+ "/General/info");
            reference.child("notify/general/start/"+idCard).setValue(clNotify);


        }else if(code_start.equals("1")) {
            Date now = new Date();
            String strDate = sdfDate.format(now);

            clNotify.setIdNotify(idCard+"end");
            clNotify.setTitle("Sự kiện đã kết thúc");
            clNotify.setMess("Sự kiện: "+nameCard+" trong bảng "+ nameTable+" của bạn đã kết thúc.");
            clNotify.setTime(strDate);


            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "channel_1")
                    .setSmallIcon(R.drawable.ic_on_notify)
                    .setContentTitle("Sự kiện đã kết thúc")
                    .setContentText("Sự kiện: "+nameCard+" trong bảng "+ nameTable+" của bạn đã kết thúc.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(id, notification.build());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" +User+ "/General/info");
            reference.child("notify/general/start/"+idCard+"end").setValue(clNotify);
        }


    }

}