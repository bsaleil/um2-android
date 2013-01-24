package com.um2.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBController 
{
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "buildings.db";
	private static final String BUILDING_NUMBER = "building_number";
	
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
 
//	public long insertBuilding(Building b)
//	{
//		//Création d'un ContentValues (fonctionne comme une HashMap)
//		ContentValues values = new ContentValues();
//		
//		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
//		values.put(BUILDING_NUMBER, b.getNumber());
//
//		//on insère l'objet dans la BDD via le ContentValues
//		return bdd.insert(DB_NAME, null, values);
//	}
// 
//	public int updateLivre(int id, Livre livre)
//	{
//		//La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
//		//il faut simplement préciser quel livre on doit mettre à jour grâce à l'ID
//		ContentValues values = new ContentValues();
//		values.put(COL_ISBN, livre.getIsbn());
//		values.put(COL_TITRE, livre.getTitre());
//		return bdd.update(TABLE_LIVRES, values, COL_ID + " = " +id, null);
//	}
// 
//	public int removeLivreWithID(int id)
//	{
//		//Suppression d'un livre de la BDD grâce à l'ID
//		return bdd.delete(TABLE_LIVRES, COL_ID + " = " +id, null);
//	}
// 
//	public Livre getLivreWithTitre(String titre)
//	{
//		//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
//		Cursor c = bdd.query(TABLE_LIVRES, new String[] {COL_ID, COL_ISBN, COL_TITRE}, COL_TITRE + " LIKE \"" + titre +"\"", null, null, null, null);
//		return cursorToLivre(c);
//	}
// 
//	//Cette méthode permet de convertir un cursor en un livre
//	private Livre cursorToLivre(Cursor c)
//	{
//		//si aucun élément n'a été retourné dans la requête, on renvoie null
//		if (c.getCount() == 0)
//			return null;
// 
//		//Sinon on se place sur le premier élément
//		c.moveToFirst();
//		//On créé un livre
//		Livre livre = new Livre();
//		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
//		livre.setId(c.getInt(NUM_COL_ID));
//		livre.setIsbn(c.getString(NUM_COL_ISBN));
//		livre.setTitre(c.getString(NUM_COL_TITRE));
//		//On ferme le cursor
//		c.close();
// 
//		//On retourne le livre
//		return livre;
//	}
}
