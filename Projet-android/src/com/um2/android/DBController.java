package com.um2.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.fortuna.ical4j.model.DateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

public class DBController 
{
	private static final int DB_VERSION = 2;
	private static final String DB_NAME = "buildings.db";
	private static final String TABLE_BUILDINGS = "buildings";
	private static final String BUILDING_NUMBER = "building_number";
	private static final String BUILDING_POINTS = "building_points";
	private static final String BUILDING_CATEGORY = "building_category";
	
	// Numéros des colonnes
	private static final int NUM_BUILDINGS_NUMBER = 0;
	private static final int NUM_BUILDINGS_POINTS = 1;
	private static final int NUM_BUILDINGS_CATEGORY = 2;
	
	// Table Events
	private static final String TABLE_EVENTS = "events";
	private static final String EVENT_ID = "id";
	private static final String EVENT_SUMMARY = "event_summary";
	private static final String EVENT_BUILDING = "event_building";
	private static final String EVENT_START_DAY = "event_start_day";
	private static final String EVENT_END_DAY = "event_end_day";
	private static final String EVENT_START_MINUTES = "event_start_minutes";
	private static final String EVENT_END_MINUTES = "event_end_minutes";
	
	private static final String CREATE_BDD_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " ("
			+ EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +EVENT_BUILDING + " INTEGER NOT NULL,"
			+ EVENT_SUMMARY + " TEXT NOT NULL, " 
			+ EVENT_START_DAY + " TEXT NOT NULL, " + EVENT_END_DAY + " TEXT NOT NULL, "
			+ EVENT_START_MINUTES + " INTEGER NOT NULL, " + EVENT_END_MINUTES + " INTEGER NOT NULL "
			+"); ";
	
	private SQLiteDatabase db;
 
	private BuildingsDB database;
 
	public DBController(Context context)
	{
		database = new BuildingsDB(context, DB_NAME, null, DB_VERSION);
	}
 
	public void open()
	{
		//on ouvre la BDD en écriture
		db = database.getWritableDatabase();
	}
	
	public void close()
	{
		//on ferme l'accès à la BDD
		db.close();
	}
 
	public SQLiteDatabase getBDD()
	{
		return db;
	}
	
	// Insère tous les évènements en BD
	public void insertAllEvents(List<ADTEvent> events)
	{
		db.execSQL("DROP TABLE " + TABLE_EVENTS + ";");
		db.execSQL(CREATE_BDD_EVENTS);
		for (int i=0; i<events.size(); i++)
		{
			ADTEvent e = events.get(i);
			
			ContentValues values = new ContentValues();
			values.put(EVENT_BUILDING, e.getADTBuilding());
			values.put(EVENT_SUMMARY, e.getADTSummary());
			values.put(EVENT_START_DAY, e.getADTStartDay());
			values.put(EVENT_END_DAY, e.getADTEndDay());
			values.put(EVENT_START_MINUTES, e.getADTMinutesStart());
			values.put(EVENT_END_MINUTES, e.getADTMinutesEnd());
			db.insert(TABLE_EVENTS, null, values);
		}
	}
	
	// Récupère la liste de SimpleEvent depuis la BD
	public ArrayList<SimpleEvent> getAllEvents()
	{
		ArrayList<SimpleEvent> res = new ArrayList<SimpleEvent>();
		
		// Avoir toutes les catégories stockées en base
		Cursor c = db.query(TABLE_EVENTS, new String[] { EVENT_BUILDING, EVENT_SUMMARY,
				EVENT_START_DAY, EVENT_END_DAY, EVENT_START_MINUTES,
				EVENT_END_MINUTES }, null, null, null, null, null);
		
		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			SimpleEvent se = new SimpleEvent();
			
			se.setADTBuilding(c.getInt(0));
			se.setADTSummary(c.getString(1));
			se.setADTStartDay(c.getString(2));
			se.setADTEndDay(c.getString(3));
			se.setADTMinutesStart(c.getInt(4));
			se.setADTMinutesEnd(c.getInt(5));
			
			res.add(se);
		}
		
		c.close();
		return res;
	}
	
	// Récupère les évenements du jour
	public ArrayList<SimpleEvent> getTodayEvents()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String strDay = dateFormat.format(date);
		
		ArrayList<SimpleEvent> res = new ArrayList<SimpleEvent>();
		
		Cursor c = db.query(TABLE_EVENTS, new String[] { EVENT_BUILDING, EVENT_SUMMARY,
				EVENT_START_DAY, EVENT_END_DAY, EVENT_START_MINUTES,
				EVENT_END_MINUTES }, EVENT_START_DAY + " = \"" + strDay +"\"", null, null, null, null);
		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			SimpleEvent se = new SimpleEvent();
			
			se.setADTBuilding(c.getInt(0));
			se.setADTSummary(c.getString(1));
			se.setADTStartDay(c.getString(2));
			se.setADTEndDay(c.getString(3));
			se.setADTMinutesStart(c.getInt(4));
			se.setADTMinutesEnd(c.getInt(5));
			
			res.add(se);
		}
		c.close();
		return res;
	}
	
	public Building getNextBuilding(Context c)
	{
		DateTime date = new DateTime();
		int nowMinutes = date.getHours()*60 + date.getMinutes();
		int minMinutes = 1440;
		
		int numCurrentBuilding = -1;
		int indexCurrentEvent = 0;
		
		List<SimpleEvent> events = getTodayEvents();
		
		for (int i=0; i<events.size(); i++) // Pour chaque évènement
		{
			if (events.get(i).getMinutesStart() > nowMinutes && events.get(i).getMinutesStart() < minMinutes)
			{
				indexCurrentEvent = i;
				minMinutes = events.get(i).getMinutesStart();
				numCurrentBuilding = events.get(i).getNumBuilding();
			}
			if (events.get(i).getMinutesEnd() > nowMinutes && events.get(i).getMinutesEnd() < minMinutes)
			{
				indexCurrentEvent = i;
				minMinutes = events.get(i).getMinutesEnd();
				numCurrentBuilding = events.get(i).getNumBuilding();
			}
		}
		
		if (numCurrentBuilding <= 0)
			return null;
		else
		{
			Toast.makeText(c, events.get(indexCurrentEvent).eventToString(c), Toast.LENGTH_LONG).show();
			Building b = getBuildingWithNumber(numCurrentBuilding);
			return b;
		}
	}
 
	public long insertBuilding(Building b)
	{
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BUILDING_NUMBER, b.getNumber());
		values.put(BUILDING_CATEGORY, b.getCategory());
		
		// Conversion d'un arraylist en JSON
		JSONObject json = new JSONObject();
		try
		{
			json.put("buildings_cords", new JSONArray(b.getPoints()));
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		String arrayList = json.toString();
		values.put(BUILDING_POINTS, arrayList);
		
		//on insère l'objet dans la BDD via le ContentValues
		return db.insert(TABLE_BUILDINGS, null, values);
	}
	
	// Indique si la base de données existe et est remplie
	public boolean databaseEmpty()
	{
		if(db.getAttachedDbs().size() > 0)
		{
			Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS}, null, null, null, null, null);
			if(c.getCount() == 0)
				return true;
		}
		return false;
	}
	
	public ArrayList<String> getAllCategories()
	{
		ArrayList<String> res = new ArrayList<String>();
		
		// Avoir toutes les catégories stockées en base
		Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_CATEGORY}, 
				BUILDING_CATEGORY + " NOT LIKE \"default\"", null, BUILDING_CATEGORY, null, null);
		
		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			res.add(c.getString(0));
		}
		c.close();
		return res;
	}
	
	public ArrayList<Building> getBuildingsWithCategory(String category)
	{
		ArrayList<Building> res = new ArrayList<Building>();
		
		Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS, BUILDING_CATEGORY}, BUILDING_CATEGORY + " LIKE \"" + category +"\"", null, null, null, null);
		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			Building b = specificCursorToBuilding(c);
			if(b != null)
				res.add(b);
		}
		c.close();
		return res;
	}
	
	public Building getBuildingWithNumber(int n)
	{
		Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS, BUILDING_CATEGORY}, BUILDING_NUMBER + " LIKE \"" + n +"\"", null, null, null, null);
		return cursorToBuilding(c);
	}
	
	public ArrayList<Building> getAllBuidings()
	{
		ArrayList<Building> res = new ArrayList<Building>();
		Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS, BUILDING_CATEGORY},
				BUILDING_CATEGORY+" LIKE \"default\" ", null, null, null, null);

		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			Building b = specificCursorToBuilding(c);
			if(b != null)
				res.add(b);
		}
		c.close();
		return res;
	}
	
	public ArrayList<Building> getAllPoiBuidings()
	{
		ArrayList<Building> res = new ArrayList<Building>();
		Cursor c = db.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS, BUILDING_CATEGORY}, 
				BUILDING_CATEGORY+" NOT LIKE \"default\" ", null, null, null, null);

		for(int i=0; i<c.getCount(); i++)
		{
			c.moveToPosition(i);
			Building b = specificCursorToBuilding(c);
			if(b != null)
				res.add(b);
		}
		c.close();
		return res;
	}

	// À partir d'un curseur spécifique retourne un batiment
	private Building specificCursorToBuilding(Cursor c)
	{
		if (c.getCount() == 0)
			return null;
		
		return buildingFromCursor(c);
	}
	
	//Cette méthode permet de convertir un cursor en un batiment
	private Building cursorToBuilding(Cursor c)
	{
		// Si pas de résultats retourner null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		Building b = buildingFromCursor(c);
		
		//On ferme le cursor
		c.close();
		return b;
	}

	private Building buildingFromCursor(Cursor c) 
	{
		Building b = new Building();
		
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		b.setNumber(c.getInt(NUM_BUILDINGS_NUMBER));

		JSONObject json;
		try 
		{
			// Convertir du JSON en ArrayList<GeoPoint>
			json = new JSONObject(c.getString(NUM_BUILDINGS_POINTS));
			JSONArray items = json.optJSONArray("buildings_cords");
			ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
			
			for(int i=0; i<items.length(); i++)
			{
				// Reconstruire les coordonées des angles du batiment
				String str[]= items.getString(i).split(",");
				GeoPoint g = new GeoPoint(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
				points.add(g);
			}
			b.setPoints(points);
			b.setCategory(c.getString(NUM_BUILDINGS_CATEGORY));
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return b;
	}
}
