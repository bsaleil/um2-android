package com.um2.android;

import java.io.IOException;
import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

// Ce thread est chargé de calculer la route depuis la position actuelle, vers un point donné
public class RouteThread extends Thread 
{
	private LocationManager locationManager;
	private Context context;
	private MapView mapView;
	private Location position;
	
	public RouteThread(MapView mv, Context c, Location l) 
	{
		mapView = mv;
		context = c;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		position = l;
	}
	
	public void setPosition(Location l)
	{
		position = l;
	}

	public void run() 
	{
		Looper.prepare();
		try
		{
			// On récupère l'endroit ou on se trouve
			if (position != null) 
			{
				// Enlève les anciennes routes
				mapView.getOverlays().clear();
				
				// On calcule la route
				PathOverlay po = new PathOverlay(Color.RED, context);
				YOURSRoute yr = new YOURSRoute();
				ArrayList<double[]> al = yr.calculateRoute(position, null);
				// Pour chaque point
				for (int i = 0; i < al.size(); i++) 
				{
					GeoPoint p = new GeoPoint(al.get(i)[0], al.get(i)[1]);
					po.addPoint(p); // On ajoute le point au
							// path overlay
				}
				// On ajoute la route à la map
				mapView.getOverlays().add(po);

				// On ajoute un location overlay, et on dessine
				// (Marqueur)
				GeoPoint locGeoPoint = new GeoPoint(position.getLatitude(), position.getLongitude());
				SimpleLocationOverlay oi = new SimpleLocationOverlay(context);
				oi.setLocation(locGeoPoint);
				oi.draw(new Canvas(), mapView, false);
				ArrayList<SimpleLocationOverlay> overlayItemArray = new ArrayList<SimpleLocationOverlay>();
				overlayItemArray.add(oi);
				mapView.getOverlays().addAll(0,	overlayItemArray);
			}
			else // Lecture position impossible
			{
				Log.d("MONTAG", "ERROR");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
