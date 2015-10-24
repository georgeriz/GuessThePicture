package com.example.george.guessthepicture;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by George on 2015-10-24.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
