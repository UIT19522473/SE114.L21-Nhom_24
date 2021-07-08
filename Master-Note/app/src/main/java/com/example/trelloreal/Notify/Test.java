package com.example.trelloreal.Notify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trelloreal.Card.ActivityShowEvent;
import com.example.trelloreal.R;

public class Test extends AppCompatActivity {
    Button btnTest;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        createNotificationChanel();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        btnTest = findViewById(R.id.btnTestTT);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Test.this, ""+"Click", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Test.this,AlarmReceiver.class);

                Bundle mBundle = new Bundle();

                mBundle.putString("test","nhả nhớt");
                intent1.putExtras(mBundle);

                long time  = System.currentTimeMillis();
                pendingIntent = PendingIntent.getBroadcast(Test.this,0,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,time+20000,pendingIntent);



//
//                Bundle mBundle = new Bundle();
//
//                mBundle.putString("test","ero_systas");


               // Intent intent1 = new Intent(Test.this,Reminder.class);
                //intent1.putExtras(mBundle);

//                long time  = System.currentTimeMillis();
//                pendingIntent = PendingIntent.getBroadcast(Test.this,0,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
//                alarmManager.set(AlarmManager.RTC_WAKEUP,time+20000,pendingIntent);
            }
        });
    }

    private void createNotificationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel";
            String description = "channel notify";
            int important = NotificationManager.IMPORTANCE_DEFAULT;
           // NotificationChannel channel = new NotificationChannel("channel_1", name, important);
            NotificationChannel channel = new NotificationChannel("channel", name, important);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager!=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}