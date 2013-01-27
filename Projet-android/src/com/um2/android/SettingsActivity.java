package com.um2.android;

import android.app.Activity;
import android.os.Bundle;

// Activité correspondant au préférences. Se charge d'afficher le fragment preferences
public class SettingsActivity extends Activity
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