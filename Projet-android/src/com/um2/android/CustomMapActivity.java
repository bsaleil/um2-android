package com.um2.android;

import java.io.IOException;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class CustomMapActivity extends Activity
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private RouteThread routeThread;
	private String currentProvider;
	
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO : Gérer la MAJ de la route + marqueur dans le thread (pour l'instant, on calcule et affiche qu'une fois)
		// TODO : La destination est écrite en dur. A passer en paramètre du thread, puis relance le thread en cas de changement ?
		super.onCreate(savedInstanceState);
				
		initializeMap();
		
		// On récupère les batiments
		ArrayList<Building> bRet = new ArrayList<Building>();
		bRet = BuildingCsvReader.readFile("coordonnees",this.getAssets());
		
		// Démarrage du thread qui calcule et dessine la route et le marqueur
		currentProvider = LocationManager.GPS_PROVIDER;
		Location position = locationManager.getLastKnownLocation(currentProvider);
		if(position == null)
		{
			currentProvider = LocationManager.NETWORK_PROVIDER;
			position = locationManager.getLastKnownLocation(currentProvider);	
		}
		
		routeThread = new RouteThread(mapView, this, position);
		routeThread.start();
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
	
	public void listenerLocation()
	{
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() 
		{
			public void onLocationChanged(Location location) 
			{
				// Called when a new location is found by the network location provider.
				routeThread.setPosition(location);
				routeThread.start();
				
				Toast.makeText(getApplicationContext(), "Position : "+location.toString(), Toast.LENGTH_SHORT).show();
			}

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
	}
	
	// Listener du click sur le bouton focus du menu
	public void onFocusClick(MenuItem item)
	{
		Location lastLocation = locationManager.getLastKnownLocation(currentProvider);
		if(lastLocation != null)
		{
			GeoPoint locGeoPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
			mapController.setCenter(locGeoPoint);
		}
	}
	
	// Listener du click sur le bouton liste du menu
	public void onListClick(MenuItem item)
	{
		Intent myIntent = new Intent(this, TabViewActivity.class);
        startActivityForResult(myIntent, 0);
	}
	
	// Menu
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_map, menu);
		return true;
	}
}
