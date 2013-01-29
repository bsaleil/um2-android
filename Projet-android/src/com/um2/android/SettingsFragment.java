package com.um2.android;

import java.io.File;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListPopupWindow;
import android.widget.Toast;

// Fragment des préférence, tout ce qui est relatif aux preferences
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
	public static final String GUIDAGE_VOCAL = "guidage_vocal";
	public static final String FICHIER_ICS = "fichier_ics";
	public static final String ACTIVER_ICS = "activer_ics";
	
	private EditTextPreference icsPref;
	
	    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		icsPref = (EditTextPreference) getPreferenceScreen().findPreference(FICHIER_ICS);
		icsPref.setSummary(PreferenceManager
				.getDefaultSharedPreferences(this.getActivity()).getString(
						FICHIER_ICS, ""));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if (key.equals(FICHIER_ICS))
		{
			icsPref.setSummary(sharedPreferences.getString(FICHIER_ICS, ""));
			
			// Si modification du nom de fichier, on teste l'existence
			File dir = Environment.getExternalStorageDirectory();
			File file = new File(dir, sharedPreferences.getString(FICHIER_ICS, ""));
			if (!file.exists()) // Si le ics n'existe pas
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
				alertDialog.setTitle(getString(R.string.attention));
				alertDialog.setMessage(getText(R.string.fichier_inexistant));
				alertDialog.show();
			}
			else
			{
				ICSReader reader = new ICSReader(getActivity().getApplicationContext(), sharedPreferences);
				DBController db = new DBController(getActivity().getApplicationContext());
				db.open();
				Log.d("DEBUG", "ON PASSE AVANT");
				db.insertAllEvents(reader.readEventsFromPrefFile());
				Log.d("DEBUG", "ON PASSE APRÈS");
				db.close();
			}
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