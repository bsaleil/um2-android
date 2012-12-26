package com.um2.android;

import android.os.Bundle;
import android.app.Activity;
import org.osmdroid.util.GeoPoint;

import org.osmdroid.views.MapView;

public class CustomMapActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MapView mapView = new MapView(this, 256);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		
		// Afficher la carte
		setContentView(mapView); 
		
		// Niveau de zoom initial
		mapView.getController().setZoom(15); 
		
		// Entrée de la fac de science
		mapView.getController().setCenter(new GeoPoint(43.6315843, 3.8612323)); 
		
		// Charger la carte en local
		mapView.setUseDataConnection(false);
	}
}
