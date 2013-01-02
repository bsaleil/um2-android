package com.um2.android;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class CustomMapActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initializeMap();
	}
	
	public void initializeMap()
	{
		MapView mapView = new MapView(this, 256);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		
		// Afficher la carte
		setContentView(mapView); 
		
		// Niveau de zoom initial
		mapView.getController().setZoom(14); 
		
		// Entrée de la fac de science
		mapView.getController().setCenter(new GeoPoint(43.6315843, 3.8612323)); 
		
		// Charger la carte en ligne : passer à false pour charger en local uniquement
		mapView.setUseDataConnection(true);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_map, menu);
	    return true;
	}
}
