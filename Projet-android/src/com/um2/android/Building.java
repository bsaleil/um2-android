package com.um2.android;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;


public class Building
{
	// Numero du batiment
	int number;
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
}
