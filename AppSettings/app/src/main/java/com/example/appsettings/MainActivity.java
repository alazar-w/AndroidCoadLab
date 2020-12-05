package com.example.appsettings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //ensures that the settings are properly initialized with their default values
                //The resource ID (preferences) for the XML resource file with one or more settings.
                //A boolean indicating whether the default values should be set more than once. When false, the system sets the default values only if this method has never
                // been called. As long as you set this third argument to false, you can safely call this method every time MainActivity starts without overriding the user's
                // saved settings values. However, if you set it to true, the method will override any previous values with the defaults.
                PreferenceManager.setDefaultValues(MainActivity.this, R.xml.preferences, false);

                // to get the setting as a SharedPreferences object (sharedPref).
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                //getBoolean() to get the Boolean value of the setting that uses the key (KEY_PREF_EXAMPLE_SWITCH defined in SettingsActivity)
                //and assign it to switchPref. If there is no value for the key, the getBoolean() method sets the setting value (switchPref) to false.
                //For other values such as strings, integers, or floating point numbers, you can use the getString(), getInt(), or getFloat() methods respectively.
                Boolean switchPref = sharedPref.getBoolean
                        (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
                Toast.makeText(MainActivity.this, switchPref.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
