package com.um2.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.location.Location;

// Cette classe permet d'interroger l'API YOUR pour obtenir une route entre 2 points, en mode piéton
public class YOURSRoute
{
	static String service = "http://www.yournavigation.org/api/1.0/gosmore.php?";
	
	static String format = "kml";	// Return XML
	static String type = "foot";	// Return route by walking
	static String fast = "0";		// Return shortest route
	
	String endLat = "43.633156";		// End point latitude
	String endLon = "3.864269";		// End point latitude
	
	public YOURSRoute()
	{
	}
	
	// Calcule and return route from point to other point
	public ArrayList<double[]> calculateRoute(Location from, Location to) throws IOException
	{
		// Build query
		String query = service;
		query += "format=" + format;
		query += "&flat=" + String.valueOf(from.getLatitude());
		query += "&flon=" + String.valueOf(from.getLongitude());
		query += "&tlat=" + endLat;
		query += "&tlon=" + endLon;
		query += "&v=" + type;
		query += "&fast=" + fast;
		
		// Get XML
		URL url = new URL(query);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		
		// Read results
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		boolean copy = false;
		ArrayList<double[]> routePoints = new ArrayList<double[]>();
		
		// Permet d'avoir un trait entre "from" et le 1er point de la route
		routePoints.add(new double[]{from.getLatitude(),from.getLongitude()});
		
		String s = "";
		while ((s = reader.readLine()) != null)
		{
			// If open tag
			if (s.contains("<coordinates>"))
			{
				s = normalizeString(s.replaceAll("<coordinates>", "")); // Remove all bad chars
				addStringPoint(s,routePoints);
				copy = true;
			}
			// If close tag
			else if (s.contains("</coordinates>"))
			{
				s = normalizeString(s.replaceAll("</coordinates>", "")); // Remove all bad chars
				addStringPoint(s,routePoints);
				copy = false;
			}
			// If between open and close tags
			else if (copy)
			{
				addStringPoint(s,routePoints);
			}
		}
		
		return routePoints;
	}
	
	// Add point to routePoints
	private void addStringPoint(String point, ArrayList<double[]> routePoints)
	{
		if (point != "") // TODO valider format chaine "N.N,N.N"?
		{
			String[] strArr = point.split("\\,");
			double[] dblArr = new double[] {Double.parseDouble(strArr[1]),Double.parseDouble(strArr[0])};
			if (dblArr.length == 2)
				routePoints.add(dblArr);
		}
	}
	
	// Delete spaces, tabs, and return
	private String normalizeString(String s)
	{
		return s = s.replaceAll(" ", "").replaceAll("\t","").replaceAll("\n", "");
	}
}
