package com.um2.android;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CustomMapActivity extends Activity
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO : Gérer la MAJ de la route + marqueur dans le thread (pour l'instant, on calcule et affiche qu'une fois)
		// TODO : La destination est écrite en dur. A passer en paramètre du thread, puis relance le thread en cas de changement ?
		super.onCreate(savedInstanceState);
				
		initializeMap();
		
		// Démarrage du thread qui calcule et dessine la route et le marqueur
		RouteThread rt = new RouteThread(mapView, this);
		rt.start();
	}
	
	// Initialise l'affichage de la map
	public void initializeMap()
	{
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		mapView = new MapView(this, 256);
		mapView.setClickable(true);
		mapController = this.mapView.getController();
		mapView.setMultiTouchControls(true);
		
		// Afficher la carte
		setContentView(mapView); 
		// Niveau de zoom initial
		mapController.setZoom(16);
		// Entrée de la fac de science
		mapController.setCenter(new GeoPoint(43.6315843, 3.8612323));
		// Charger la carte en ligne : passer à false pour charger en local uniquement
		mapView.setUseDataConnection(true);
	}
	
	// Listener du click sur le bouton focus du menu
	public void onFocusClick(MenuItem item)
	{
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(lastLocation != null)
		{
			GeoPoint locGeoPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
			mapController.setCenter(locGeoPoint);
		}
	}
	
	// Menu
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_map, menu);
		return true;
	}
}
