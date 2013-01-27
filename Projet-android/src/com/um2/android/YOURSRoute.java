package com.um2.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.osmdroid.util.GeoPoint;
import android.location.Location;
import android.util.Log;

// Cette classe permet d'interroger l'API YOUR pour obtenir une route entre 2 points, en mode piéton
public class YOURSRoute
{
	static String service = "http://www.yournavigation.org/api/1.0/gosmore.php?";
	
	static String format = "kml";	// Return XML
	static String type = "foot";	// Return route by walking
	static String fast = "0";		// Return shortest route
	static String instructions="1"; // Return description route
	static String lang = "fr";
	
	String description = "";
	
	String endLat = "43.633156";		// End point latitude
	String endLon = "3.864269";		// End point latitude
	GeoPoint targetPoint;
	
	public YOURSRoute(GeoPoint target)
	{
		targetPoint = target;
		endLat = String.valueOf(target.getLatitudeE6() / 1e6);
		endLon = String.valueOf(target.getLongitudeE6() / 1e6);
	}
	
	// Calcule and return route from point to other point
	public ArrayList<double[]> calculateRoute(Location from) throws IOException
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
		query += "&instructions=" + instructions;
		query += "&lang=" + lang;
		
		// Get XML
		URL url = new URL(query);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		
		// Read results
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		boolean copy = false;
		boolean copy1 = false;
		boolean desFound = false;
		ArrayList<double[]> routePoints = new ArrayList<double[]>();
		
		// Permet d'avoir un trait entre "from" et le 1er point de la route
		routePoints.add(new double[]{from.getLatitude(),from.getLongitude()});
		
		String s = "";
		description= "";
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
			
			/** Description **/
			// If open tag
			if (s.contains("<description>") && !desFound)
			{
				s = normalizeString2(s.replaceAll("<description>", " ")); // Remove all bad chars
				description = description.concat(s);
				copy1 = true;
			}
			// If close tag
			else if (s.contains("</description>") && !desFound)
			{
				s = normalizeString2(s.replaceAll("</description>", " ")); // Remove all bad chars
				description = description.concat(s);
				copy1 = false;
				desFound = true;
			}
			// If between open and close tags
			else if (copy1)
			{
				description = description.concat(s);
			}
		}
		description = description.substring(0, description.indexOf("."));
		description = normalizeString2(description);
		
		// Ajoute un point de la fin de la route, au point de destination
		routePoints.add(new double[]{targetPoint.getLatitudeE6() / 1e6,targetPoint.getLongitudeE6() / 1e6});
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
		return s = s.replaceAll(" ", "").replaceAll("\t","").replaceAll("\n", "").replaceAll("&lt;br&gt;", "");
	}
	
	// Delete, tabs, and return
	private String normalizeString2(String s)
	{
		return s = s.replaceAll("\t","").replaceAll("\n", "").replaceAll("&lt;br&gt;", "");
	}
	
	// Return Description
	public String getDescription()
	{
		return description;
	}
}
