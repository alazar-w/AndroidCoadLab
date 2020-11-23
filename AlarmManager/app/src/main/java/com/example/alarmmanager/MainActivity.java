package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);

        //initialize mNotificationManager using getSystemService().
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        // Set up the Notification Broadcast Intent.
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        //call PendingIntent.getBroadcast() with the FLAG_NO_CREATE flag.
        //If a PendingIntent exists, that PendingIntent is returned; otherwise the call returns null.
        //The flag determines what happens if a PendingIntent whose intent matches the intent you are trying to create already exists.
        //The NO_CREATE flag returns null unless a PendingIntent with a matching Intent exists.
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        //if the app is closed, the toggle button resets to the off state, even if the alarm has already been set.
        //To fix this, you need to check the state of the alarm every time the app is launched.,
        //To track the state of the alarm you need a boolean variable that is true if the alarm exists, and false otherwise.
        alarmToggle.setChecked(alarmUp);


        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Initialize the AlarmManager
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;
                if(isChecked){
                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    //The trigger time in milliseconds. Use the current elapsed time, plus 15 minutes. To get the current elapsed time, call SystemClock``.elapsedRealtime().
                    //Then use a built-in AlarmManager constant to add 15 minutes to the elapsed time.
                    long triggerTime = SystemClock.elapsedRealtime()
                            + repeatInterval;

                    //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                    if (alarmManager != null) {
                        //The alarm type. In this case only the relative time is important, and you want to wake the device if it's asleep, so use ELAPSED_REALTIME_WAKEUP.
                        //notifyPendingIntent --> is the pending intent to be delivered
                        alarmManager.setInexactRepeating
                                (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                        triggerTime, repeatInterval, notifyPendingIntent);
                    }
                    //Set the toast message for the "on" case
                    toastMessage = "Stand Up Alarm On!";
                } else {
                    //Cancel notification if the alarm is turned off
                    mNotificationManager.cancelAll();

                    //cancel the alarm by calling cancel() on the AlarmManager. Pass in the pending intent used to create the alarm.
                    if (alarmManager != null) {
                        alarmManager.cancel(notifyPendingIntent);
                    }

                    //Set the toast message for the "off" case
                    toastMessage = "Stand Up Alarm Off!";
                }

                //Show a toast to say the alarm is turned on or off.
                Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT)
                        .show();
            }
        });

        //Create the notification channel.
        createNotificationChannel();

    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
