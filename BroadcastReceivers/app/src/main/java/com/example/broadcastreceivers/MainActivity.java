package com.example.broadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_CUSTOM_BROADCAST =
            "com.example.broadcastreceivers"+ ".ACTION_CUSTOM_BROADCAST";

    private CustomReceiver mReceiver = new CustomReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent filters specify the types of intents a component can receive.
        //They are used in filtering out the intents based on Intent values like action and category.
        IntentFilter filter = new IntentFilter();

        //When the system receives an Intent as a broadcast,
        //it searches the broadcast receivers based on the action value specified in the IntentFilter object.
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);

        //Register the receiver using the activity context.(registering for a system broadcast, )
        //so the receiver is active and able to receive broadcasts as long as MainActivity is running.
        //this is called context-registered receivers unlike manifest-declared receivers
        this.registerReceiver(mReceiver, filter);

        //Registering for a local broadcast is similar to registering for a system broadcast,
        // which you do using a dynamic receiver. For broadcasts sent using LocalBroadcastManager,
//        LocalBroadcastManager.getInstance(this)
//                .registerReceiver(mReceiver,
//                        new IntentFilter(ACTION_CUSTOM_BROADCAST));


    }

    //to save system resources and avoid leaks, dynamic receivers must be unregistered
    //when they are no longer needed or before the corresponding activity or app is destroyed, depending on the context used.
    @Override
    protected void onDestroy() {
        //Unregister the receiver
        this.unregisterReceiver(mReceiver);
        //unregister your receiver from the LocalBroadcastManager:
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void sendCustomBroadcast(View view) {
        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);


    }
}
