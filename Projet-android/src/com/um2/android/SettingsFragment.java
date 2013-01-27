package com.um2.android;

import android.os.Bundle;
import android.preference.PreferenceFragment;

// Fragment des préférence, tout ce qui est relatif aux preferences
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}