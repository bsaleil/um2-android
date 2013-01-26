package com.um2.android;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomMapActivity extends Activity
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private RouteThread routeThread;
	private String currentProvider;
	private DBController dbController;
	protected static final int RESULT_SPEECH = 1;
	private TextView txtText;
	private TextToSpeech tts;
	
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO : Gérer la MAJ de la route + marqueur dans le thread (pour l'instant, on calcule et affiche qu'une fois)
		// TODO : La destination est écrite en dur. A passer en paramètre du thread, puis relance le thread en cas de changement ?
		super.onCreate(savedInstanceState);
				
		initializeMap();
		
		// On récupère les batiments
		ArrayList<Building> bRet = new ArrayList<Building>();
		bRet = BuildingCsvReader.readFile("coordonnees", this.getAssets());
		
		// Remplissage de la BDD
		initializeDB(bRet);
		
		// Lancement du thread de calcul de route avec le handler
		initializeRouteThread();
		
		//Initialize du tts Pour la lecture vocale
        tts = new TextToSpeech(this, null);
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
	
	public void initializeRouteThread()
	{
		// Démarrage du thread qui calcule et dessine la route et le marqueur
		currentProvider = LocationManager.GPS_PROVIDER;
		Location position = locationManager.getLastKnownLocation(currentProvider);
		if(position == null)
		{
			currentProvider = LocationManager.NETWORK_PROVIDER;
			position = locationManager.getLastKnownLocation(currentProvider);	
		}
		// Créé le handler pour mettre à jour la carte depuis le thread principal
		Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            mapView.invalidate();
	        }
		};
		routeThread = new RouteThread(mapView, this, position, mHandler);
		routeThread.start();
	}
	
	// Remplir la base de données avec les batiments
	public void initializeDB(ArrayList<Building> buildings)
	{
		dbController = new DBController(this);
		dbController.open();
		
		// Si la BD semble vide, on la remplie
		if(dbController.databaseEmpty())
		{
			for(Building b : buildings)
				dbController.insertBuilding(b);
		}
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
				//Toast.makeText(getApplicationContext(), "Position : "+location.toString(), Toast.LENGTH_SHORT).show();
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
		startActivityForResult(myIntent, 10);
	}
	
	// Listener du click sur le bouton Vocal search du menu
	public void onVocalClick(MenuItem item)
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "fr-FR");
        
		try {
			startActivityForResult(intent, RESULT_SPEECH);
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Opps! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}
	
	// For 4.0.3 Compatibility
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        
            case R.id.menu_list:
            	onListClick(item);
                return true;
                
            case R.id.menu_focus:
            	onFocusClick(item);
                return true;
                
            case R.id.menu_vocal_search:
            	onVocalClick(item);
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		
			case RESULT_SPEECH: {
				if (resultCode == RESULT_OK && null != data) {
					ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					
					Toast t = Toast.makeText(getApplicationContext(),
							text.get(0),
							Toast.LENGTH_SHORT);
					t.show();
					// Speak
					tts.speak(text.get(0), TextToSpeech.QUEUE_FLUSH, null);
			        }
				}
				break;
		}	
	}
	
	// Menu
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_map, menu);
		return true;
	}
	
	public DBController getDbController()
	{
		return dbController;
	}
}
