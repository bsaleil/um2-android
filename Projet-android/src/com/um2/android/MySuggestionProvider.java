package com.um2.android;

import android.content.SearchRecentSuggestionsProvider;

public class MySuggestionProvider extends SearchRecentSuggestionsProvider
{
	public final static String AUTHORITY = "com.um2.android.MySuggestionProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;
	
	public MySuggestionProvider()
	{
		setupSuggestions(AUTHORITY, MODE);
	}
}
