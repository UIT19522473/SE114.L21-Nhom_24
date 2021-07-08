package com.example.trelloreal.Notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.trelloreal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Toi trong receiver", "xin chao");
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("test");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel")
                .setSmallIcon(R.drawable.ic_on_notify)
                .setContentTitle("thong bao")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" +"tuan@gmailcom"+ "/General/info");
        reference.child("notify/general/start/"+"12345jqk"+"end/test").setValue("hello");
    }
}
