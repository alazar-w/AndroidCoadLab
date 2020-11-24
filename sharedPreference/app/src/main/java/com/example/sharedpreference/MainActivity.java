package com.example.sharedpreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.sharedpreference";
    // Current count
    private int mCount = 0;
    // Current background color
    private int mColor;
    // Text view to display both count and color
    private TextView mShowCountTextView;

    // Key for current count
    private final String COUNT_KEY = "count";
    // Key for current color
    private final String COLOR_KEY = "color";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the shared preferences
        //The getSharedPreferences() method (from the activity Context) opens the file at the given filename (sharedPrefFile) with the mode MODE_PRIVATE.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);


        // Initialize views, color
        mShowCountTextView = findViewById(R.id.count_textview);
        mColor = ContextCompat.getColor(this,
                R.color.default_background);

        //Restore preferences in onCreate()
        //getInt() method takes two arguments: one for the key, and the other for the default value if the key cannot be found.
        mCount = mPreferences.getInt(COUNT_KEY, 0);

        //Update the value of the main TextView with the new count.
        mShowCountTextView.setText(String.format("%s", mCount));

        mColor = mPreferences.getInt(COLOR_KEY, mColor);
        mShowCountTextView.setBackgroundColor(mColor);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //Saving preferences is a lot like saving the instance state – both operations set aside the data to a Bundle object as a key/value pair.
        //For shared preferences, however, you save that data in the onPause() lifecycle callback,
        //and you need a shared editor object ( SharedPreferences.Editor) to write to the shared preferences object.
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        //putInt() method to put both the mCount and mColor integers into the shared preferences with the appropriate keys:
        preferencesEditor.putInt(COUNT_KEY, mCount);
        preferencesEditor.putInt(COLOR_KEY, mColor);

        //apply() method saves the preferences asynchronously, off of the UI thread.
        //The shared preferences editor also has a commit() method to synchronously save the preferences.The commit() method is discouraged as it can block other operations.
        preferencesEditor.apply();
    }

    /**
     * Handles the onClick for the background color buttons. Gets background
     * color of the button that was clicked, and sets the TextView background
     * to that color.
     *
     * @param view The view (Button) that was clicked.
     */
    public void changeBackground(View view) {
        int color = ((ColorDrawable) view.getBackground()).getColor();
        mShowCountTextView.setBackgroundColor(color);
        mColor = color;
    }

    /**
     * Handles the onClick for the Count button. Increments the value of the
     * mCount global and updates the TextView.
     *
     * @param view The view (Button) that was clicked.
     */
    public void countUp(View view) {
        mCount++;
        mShowCountTextView.setText(String.format("%s", mCount));
    }


    /**
     * Handles the onClick for the Reset button. Resets the global count and
     * background variables to the defaults and resets the views to those
     * default values.
     *
     * @param view The view (Button) that was clicked.
     */
    public void reset(View view) {
        // Reset count
        mCount = 0;
        mShowCountTextView.setText(String.format("%s", mCount));

        // Reset color
        mColor = ContextCompat.getColor(this, R.color.default_background);
        mShowCountTextView.setBackgroundColor(mColor);

        // Clear preferences
        //The reset button in the starter app resets both the count and color for the activity to their default values.
        //Because the preferences hold the state of the activity, it's important to also clear the preferences at the same time.
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}
