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
	+ EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +EVENT_BUILDING + "INTEGER NOT NULL,"
	+ EVENT_SUMMARY + " TEXT NOT NULL," 
	+ EVENT_START_DAY + " TEXT NOT NULL, " + EVENT_END_DAY + " TEXT NOT NULL, "
	+ EVENT_START_MINUTES + " INTEGER NOT NULL, " + EVENT_END_MINUTES + " INTEGER NOT NULL"
	+"); ";
	
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
