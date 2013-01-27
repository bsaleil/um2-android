package com.um2.android;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.content.Context;

public class Building
{
	// Numero du batiment
	private int number;
	
	// Si ce bâtiment est un distributeur, wifi etc
	private String category = "default"; 
	
	// Liste des coordonnees du batiment
	ArrayList<GeoPoint> points;
	
	Building()
	{
		number = 0;
		points = new ArrayList<GeoPoint>();
	}
	
	Building(int n)
	{
		number = n;
		points = new ArrayList<GeoPoint>();
	}
	
	Building(int n, ArrayList<GeoPoint> c)
	{
		number = n;
		points = c;
	}
	
	public void addPoint(double lat, double lon)
	{
		GeoPoint p = new GeoPoint(lat, lon);
		points.add(p);
	}
	
	public String toString()
	{
		String r = "Batiment : " + String.valueOf(number) + " ";
		for (int i=0; i<points.size(); i++) { r+=points.get(i).toString(); };
		return r;
	}
	
	public String getName(Context c) // Besoin du context pour lire dans "strings"
	{
		String r = "";
		if (number < 101) // "Batiment XX"
		{
			
			r = c.getString(R.string.building);
			r += " " + String.valueOf(number);
			if (number == 31) r+= " Polytech"; // Si 100 on ajoute "polytech"
		}
		else if (number == 101) // RU triolet
		{
			r = c.getString(R.string.resto_u);
			r += " Triolet";
		}
		else if (number == 102) // Cafet
		{
			r = c.getString(R.string.cafeteria);
		}
		else if (number == 103)
		{
			r = c.getString(R.string.CSU);
		}
		
		return r;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int n)
	{
		number = n;
	}
	
	public ArrayList<GeoPoint> getPoints()
	{
		return points;
	}
	
	public void setPoints(ArrayList<GeoPoint> a)
	{
		points = a;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public void setCategory(String c)
	{
		category = c;
	}
}
