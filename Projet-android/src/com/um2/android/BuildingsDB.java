package com.um2.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BuildingsDB extends SQLiteOpenHelper
{
	private static final String TABLE_BUILDINGS = "buildings";
	private static final String ID = "id";
	private static final String BUILDING_NUMBER = "building_number";
	private static final String BUILDING_POINTS = "building_points";
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_BUILDINGS + " ("
	+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BUILDING_NUMBER + " text not null, "
	+ BUILDING_POINTS +" TEXT NOT NULL );";
 
	public BuildingsDB(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}
 
	public void onCreate(SQLiteDatabase db)
	{
		//on crée la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}
 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE " + TABLE_BUILDINGS + ";");
		onCreate(db);
	}
}
