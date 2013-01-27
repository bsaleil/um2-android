package com.um2.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListPopupWindow;

// Fragment des préférence, tout ce qui est relatif aux preferences
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
	public static final String GUIDAGE_VOCAL = "guidage_vocal";
	public static final String FICHIER_ICS = "fichier_ics";
	
	private EditTextPreference icsPref;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        icsPref = (EditTextPreference)getPreferenceScreen().findPreference(FICHIER_ICS);
        icsPref.setSummary(PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(FICHIER_ICS, ""));
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if (key.equals(FICHIER_ICS))
		{
			icsPref.setSummary(sharedPreferences.getString(FICHIER_ICS, ""));
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
}