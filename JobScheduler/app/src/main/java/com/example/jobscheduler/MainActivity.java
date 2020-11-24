package com.example.jobscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;
    //Switches for setting job options
    private Switch mDeviceIdleSwitch;
    private Switch mDeviceChargingSwitch;
    //Override deadline seekbar
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDeviceIdleSwitch = findViewById(R.id.idleSwitch);
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch);
        mSeekBar = findViewById(R.id.seekBar);
        final TextView seekBarProgress = findViewById(R.id.seekBarProgress);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //onProgressChanged() is the current value of the seek bar.
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0){
                    seekBarProgress.setText(i + " s");
                }else {
                    seekBarProgress.setText("Not Set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



    }

    public void scheduleJob(View view) {
        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        //get the selected network ID and save it in an integer variable.
        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();

        //create an integer variable for the selected network option. Set the variable to the default network option, which is NETWORK_TYPE_NONE.
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;

        //The override deadline should only be set if the integer value of the SeekBar is greater than 0
        int seekBarInteger = mSeekBar.getProgress();
        boolean seekBarSet = seekBarInteger > 0;

        switch(selectedNetworkID){
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }
        //use getSystemService() to initialize mScheduler.
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);


        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());

        // The first parameter is the JOB_ID. The second parameter is the ComponentName for the JobService you created.
        // The ComponentName is used to associate the JobService with the JobInfo object.
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
                .setRequiresCharging(mDeviceChargingSwitch.isChecked());

        //if seekBarSet is true, call setOverrideDeadline() on the JobInfo.Builder
        //value multiplied by 1000 --> (The parameter is in milliseconds, and you want the user to set the deadline in seconds.)
        if (seekBarSet) {
            builder.setOverrideDeadline(seekBarInteger * 1000);
        }

        //"No Network Required" condition is the default, and this condition does not count as a constraint. To properly schedule the JobService, the JobScheduler needs at least one constraint.
        //create a boolean that tracks whether this requirement is met,
        //so that you can notify the user to set at least one constraint if they haven't already.
        //The variable is true if the selected network option is not the default. (The default is JobInfo.NETWORK_TYPE_NONE.)
        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE
                || mDeviceChargingSwitch.isChecked() || mDeviceIdleSwitch.isChecked()
                || seekBarSet;

        if(constraintSet) {
            //Schedule the job and notify the user
            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please set at least one constraint",
                    Toast.LENGTH_SHORT).show();
        }

    }

    //call cancelAll() on the object to remove all pending jobs.
    public void cancelJobs(View view) {
        if (mScheduler!=null){
            mScheduler.cancelAll();
            //reset the JobScheduler to null
            mScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
