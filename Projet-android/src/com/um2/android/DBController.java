package com.um2.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.osmdroid.util.GeoPoint;

public class DBController 
{
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "buildings.db";
	private static final String TABLE_BUILDINGS = "buildings";
	private static final String ID = "id";
	private static final String BUILDING_NUMBER = "building_number";
	private static final String BUILDING_POINTS = "building_points";
	
	// Numéros des colonnes
	private static final int NUM_BUILDINGS_NUMBER = 0;
	private static final int NUM_BUILDINGS_POINTS = 1;
	
	private SQLiteDatabase bdd;
 
	private BuildingsDB database;
 
	public DBController(Context context)
	{
		database = new BuildingsDB(context, DB_NAME, null, DB_VERSION);
	}
 
	public void open()
	{
		//on ouvre la BDD en écriture
		bdd = database.getWritableDatabase();
	}
	
	public void close()
	{
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD()
	{
		return bdd;
	}
 
	public long insertBuilding(Building b)
	{
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BUILDING_NUMBER, b.getNumber());
		
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
		return bdd.insert(TABLE_BUILDINGS, null, values);
	}
	
	public Building getBuildingWithNumber(int n)
	{
		Cursor c = bdd.query(TABLE_BUILDINGS, new String[] {BUILDING_NUMBER, BUILDING_POINTS}, BUILDING_NUMBER + " LIKE \"" + n +"\"", null, null, null, null);
		return cursorToBuilding(c);
	}

	//Cette méthode permet de convertir un cursor en un batiment
	private Building cursorToBuilding(Cursor c)
	{
		// Si pas de résultats retourner null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
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
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		//On ferme le cursor
		c.close();
		return b;
	}
}
