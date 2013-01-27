package com.um2.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.VEvent;

import org.apache.james.mime4j.parser.Event;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class CustomMapActivity extends Activity
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private RouteThread routeThread;
	private String currentProvider;
	private DBController dbController;
	private SharedPreferences preferences;
	protected static final int RESULT_SPEECH = 1;
	private TextToSpeech tts;
	private String previousMSG = "";
	private int timer = 0;
	private Context self = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO : Gérer la MAJ de la route + marqueur dans le thread
		// (pour l'instant, on calcule et affiche qu'une fois)
		// TODO : La destination est écrite en dur. A passer en
		// paramètre du thread, puis relance le thread en cas de
		// changement ?
		super.onCreate(savedInstanceState);

		// On récupère les préférences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		initializeMap();
		
		// On récupère les batiments
		ArrayList<Building> bRet = new ArrayList<Building>();
		bRet = BuildingCsvReader.readFile("coordonnees2", this.getAssets());

		// Parsing des points spéciaux (café, toilettes...)
		ArrayList<Building> pois = new ArrayList<Building>();
		pois = PoiCsvReader.readFile("poi", this.getAssets());
		bRet.addAll(pois);
		
		// Remplir la BDD
		initializeDB(bRet);
		
		// Calculer l'itineraire si itineraire automatique activé
		automaticGuidance();

		
		// Lancement du thread de calcul de route avec le handler
		initializeRouteThread();

		// Initialize du tts Pour la lecture vocale
		tts = new TextToSpeech(this, null);
		
		self = this;
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
		// Charger la carte en ligne : passer à false pour charger en
		// local uniquement
		mapView.setUseDataConnection(true);
	}

	public void initializeRouteThread()
	{
		// Démarrage du thread qui calcule et dessine la route et le
		// marqueur
		currentProvider = LocationManager.GPS_PROVIDER;
		Location position = locationManager.getLastKnownLocation(currentProvider);
		if (position == null)
		{
			currentProvider = LocationManager.NETWORK_PROVIDER;
			position = locationManager.getLastKnownLocation(currentProvider);
		}

		// Créé le handler pour mettre à jour la carte depuis le thread principal
		Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            mapView.invalidate();
	            
	            // Speak
	            boolean guidageVocal = preferences.getBoolean(SettingsFragment.GUIDAGE_VOCAL, true);
	            if (guidageVocal)
	            {
		            if(msg.getData().get("DESCRIPTION") != null)
		            {
		            	if(!msg.getData().get("DESCRIPTION").equals(previousMSG) || timer>60)
		            	{
		            		previousMSG = msg.getData().get("DESCRIPTION").toString();
		            		tts.speak(previousMSG, TextToSpeech.QUEUE_FLUSH, null);
		            		timer=0;
		            	}
		            	timer++;
		            }
	            }
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
		if (dbController.databaseEmpty())
		{
			for (Building b : buildings)
				dbController.insertBuilding(b);
		}
	}
	
	// Calcule la route vers le batiment du prochain cours si activé
	public void automaticGuidance()
	{
		boolean ics_actif = preferences.getBoolean(SettingsFragment.ACTIVER_ICS, true);
		if (ics_actif) // Si le guidage automatique est active, on cherche le prochaine batiment
		{
			ICSReader icsR = new ICSReader(this,preferences);
			Building b = icsR.getNextBuilding();
			if (b != null)
				((UM2Application)getApplication()).setTargetBuilding(b);
		}
	}

	public void listenerLocation()
	{
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener()
		{
			public void onLocationChanged(Location location)
			{
				// Called when a new location is found by the
				// network location provider.
				routeThread.setPosition(location);
				// Toast.makeText(getApplicationContext(),
				// "Position : "+location.toString(),
				// Toast.LENGTH_SHORT).show();
			}

			public void onStatusChanged(String provider, int status, Bundle extras)
			{
			}

			public void onProviderEnabled(String provider)
			{
			}

			public void onProviderDisabled(String provider)
			{
			}
		};

		// Register the listener with the Location Manager to receive
		// location updates
		locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
	}

	// Listener du click sur le bouton focus du menu
	public void onFocusClick(MenuItem item)
	{
		Location lastLocation = locationManager.getLastKnownLocation(currentProvider);
		if (lastLocation != null)
		{
			GeoPoint locGeoPoint = new GeoPoint(lastLocation.getLatitude(),
					lastLocation.getLongitude());
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

		try
		{
			startActivityForResult(intent, RESULT_SPEECH);
		} catch (ActivityNotFoundException a)
		{
			Toast t = Toast.makeText(getApplicationContext(),
					"Opps! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}

	// Listener du click sur le bouton Vocal search du menu
	public void onAboutClick(MenuItem item)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.apropos_menu);
		alertDialogBuilder
				.setMessage("\t\t\t\tCreated by :\n\n\t\t\t Baptiste SALEIL\n\t\t\t Julien PAGÈS\n"
						+ "\t\t Walid BENGHABRIT\n\n\t\t\tMASTER II  AIGLE");
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	// Listener du click sur le bouton des préférences
	public void onPrefsClick(MenuItem item)
	{
		Intent myIntent = new Intent(this, SettingsActivity.class);
		startActivityForResult(myIntent, 10);
	}

	// For 4.0.3 Compatibility
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.menu_list:
				onListClick(item);
				return true;

			case R.id.menu_focus:
				onFocusClick(item);
				return true;

			case R.id.menu_vocal_search:
				onVocalClick(item);
				return true;

			case R.id.menu_about:
				onAboutClick(item);
				return true;
			case R.id.menu_prefs:
				onPrefsClick(item);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
			case RESULT_SPEECH:
			{
				if (resultCode == RESULT_OK && null != data)
				{
					ArrayList<String> text = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

					Building b =  search(text.get(0));
					Toast t;
					if (b == null)
					{
						Log.d("DEBUG", "Aucun résultat");
						t = Toast.makeText(getApplicationContext(),"Aucun résultat", Toast.LENGTH_SHORT);
						t.show();
						tts.speak("Aucun résultat", TextToSpeech.QUEUE_FLUSH, null);
					}
					else
					{
					    ((UM2Application) getApplication()).setTargetBuilding(b);
					}
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

		// Gestion de la recherche
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
		{
			public boolean onQueryTextChange(String newText)
			{
				// Do something
				return true;
			}

			public boolean onQueryTextSubmit(String query)
			{
				Building b = search(query);
				Toast t;
				if (b == null)
				{
					Log.d("DEBUG", "Aucun résultat");
					t = Toast.makeText(getApplicationContext(),
							"Aucun résultat", Toast.LENGTH_SHORT);
					t.show();
					tts.speak("Aucun résultat", TextToSpeech.QUEUE_FLUSH, null);
				} 
				else
				{
					t = Toast.makeText(getApplicationContext(),
							b.getName(self), Toast.LENGTH_SHORT);
					t.show();
					tts.speak(b.getName(self), TextToSpeech.QUEUE_FLUSH, null);
					((UM2Application) getApplication()).setTargetBuilding(b);
				}

				return true;
			}
		};

		 searchView.setOnQueryTextListener(queryTextListener);
		return true;
	}
	
	// Fonction de recherche
	private Building search(String query)
	{
		Building b = null ;
	
		// Traitement des mots clès
		query = query.toLowerCase();
		
		
		if(query.startsWith("1") || query.startsWith("2") || query.startsWith("3")
				 || query.startsWith("4")  || query.startsWith("5")){
			return dbController.getBuildingWithNumber(Integer.parseInt(query));
		}

		if(query.startsWith("bâtiment") || query.startsWith("batiment")){
			return dbController.getBuildingWithNumber(Integer.parseInt(query.substring(9)));
		}
		
		if(query.startsWith("bate") || query.startsWith("bat")){
			return dbController.getBuildingWithNumber(Integer.parseInt(query.substring(4)));
		}
		
		
		if(query.equals("polytech")){
			return dbController.getBuildingWithNumber(31);
		}
		
		if(query.equals("resto u") || query.equals("rue") || query.equals("r u") || query.equals("ru") || 
			query.equals("resto universitaire") || query.equals("restaurant universitaire")){
			return dbController.getBuildingWithNumber(101);
		}
		
		if(query.equals("café") || query.equals("t'as fait") || query.equals("ca fait") || 
				query.equals("caféteria") || query.equals("caffet")){
				return dbController.getBuildingWithNumber(102);
		}
		
		if(query.equals("csu") || query.equals("centre sportif") || query.equals("sport") ||
				query.equals("sport")){
			return dbController.getBuildingWithNumber(103);
		}

		if(query.equals("bu") || query.equals("b u") || query.equals("bibliotheque") || query.equals("bibliothèque")
				|| query.equals("biblio")){
			return dbController.getBuildingWithNumber(8);
		}
	    
		if(query.equals("admin") || query.equals("administration")){
			return dbController.getBuildingWithNumber(7);
		}
	    
		if(query.equals("maison des étudiants")){
			return dbController.getBuildingWithNumber(34);
		}
	    
		return b;
	}
}
