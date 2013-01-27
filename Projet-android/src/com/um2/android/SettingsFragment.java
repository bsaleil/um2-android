package com.um2.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

// Fragment des préférence, tout ce qui est relatif aux preferences
public class SettingsFragment extends PreferenceFragment
{
	public static final String GUIDAGE_VOCAL = "guidage_vocal";
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}