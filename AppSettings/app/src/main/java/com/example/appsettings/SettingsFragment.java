package com.example.appsettings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    // The preference Fragment is rooted at the PreferenceScreen using rootKey.
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //associating preferences.xml with this Fragment
        //rootKey -> identify the preference root in PreferenceScreen:
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    //The reason why you are essentially replacing onCreateView() with onCreatePreferences() is because
    //you will be adding this SettingsFragment to the existing SettingsActivity to display preferences,
    //rather than showing a separate Fragment screen. Adding it to the existing Activity makes it easy to add or remove a Fragment
    //while the Activity is running. The preference Fragment is rooted at the PreferenceScreen using rootKey.


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }
}
