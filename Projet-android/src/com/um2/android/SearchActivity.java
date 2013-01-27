package com.um2.android;

import android.os.Bundle;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class SearchActivity extends ListActivity 
{	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		Log.d("DEBUG", "On rentre dans l'activité recherche");
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			String query = intent.getStringExtra(SearchManager.QUERY);
			searchDestination(query);
		}
	}

	// Appelée lors d'une recherche
	private void searchDestination(String query)
	{
		Log.d("DEBUG", "Recherche : "+query);
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.activity_search, menu);
		return true;
	}

}
