package com.um2.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BuildingsDB extends SQLiteOpenHelper
{
	private static final String DB_BUILDINGS = "buildings";
	private static final String COL_ID = "ID";
	private static final String COL_ISBN = "ISBN";
	private static final String COL_TITRE = "Titre";
 
	private static final String CREATE_BDD = "CREATE TABLE " + DB_BUILDINGS + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ISBN + " TEXT NOT NULL, "
	+ COL_TITRE + " TEXT NOT NULL);";
 
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
		db.execSQL("DROP TABLE " + DB_BUILDINGS + ";");
		onCreate(db);
	}
}
