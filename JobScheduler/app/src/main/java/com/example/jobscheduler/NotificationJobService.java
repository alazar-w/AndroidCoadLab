package com.example.jobscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {
    NotificationManager mNotifyManager;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    //onStartJob() callback: Called when the system determines that your task should be run. In this method, you implement the job to be done.
    //Returns a boolean indicating whether the job needs to continue on a separate thread. If true, the work is offloaded to a different thread,
    //and your app must call jobFinished() explicitly in that thread to indicate that the job is complete.
    //If false, the system knows that the job is completed by the end of onStartJob(), and the system calls jobFinished() on your behalf.
    // The onStartJob() method is executed on the main thread, and therefore any long-running tasks must be offloaded to a different thread.
    @Override
    public boolean onStartJob(JobParameters params) {

        //Create the notification channel
        createNotificationChannel();

        //Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Your Job ran to completion!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_job_running)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mNotifyManager.notify(0, builder.build());
        return false;
    }

    //If the conditions described in the JobInfo are no longer met, the job must be stopped, and the system calls onStopJob().
    //The onStopJob() callback returns a boolean that determines what to do if the job is not finished.
    //If the return value is true, the job is rescheduled; otherwise, the job is dropped.
    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Job Service notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

}
