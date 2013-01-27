package com.um2.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

// Activité correspondant au préférences. Se charge d'afficher le fragment preferences
public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
    }
}