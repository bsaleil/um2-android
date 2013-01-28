package com.um2.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BuildingsDB extends SQLiteOpenHelper
{
	private static final String TABLE_BUILDINGS = "buildings";
	private static final String BUILDING_ID = "id";
	private static final String BUILDING_NUMBER = "building_number";
	private static final String BUILDING_POINTS = "building_points";
	private static final String BUILDING_CATEGORY = "building_category";
	
	private static final String CREATE_BDD_BUILDINGS = "CREATE TABLE " + TABLE_BUILDINGS + " ("
	+ BUILDING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BUILDING_NUMBER + " text not null, "
	+ BUILDING_POINTS +" TEXT NOT NULL ,"+ BUILDING_CATEGORY +" DEFAULT \"default\");";
 
	private static final String TABLE_EVENTS = "events";
	private static final String EVENT_ID = "id";
	private static final String EVENT_DATE = "event_date";
	
	private static final String CREATE_BDD_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " ("
	+ EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_DATE + " text not null); ";
	
	public BuildingsDB(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}
 
	public void onCreate(SQLiteDatabase db)
	{
		// on crée la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD_BUILDINGS);
		db.execSQL(CREATE_BDD_EVENTS);
	}
 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE " + TABLE_BUILDINGS + ";");
		db.execSQL("DROP TABLE " + TABLE_EVENTS + ";");
		onCreate(db);
	}
}
