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

// Ce thread est chargé de calculer la route depuis la position actuelle, vers un point donné
public class RouteThread extends Thread
{
	private LocationManager locationManager;
	private Context context;
	private MapView mapView;
	
	public RouteThread(MapView mv, Context c)
	{
		mapView = mv;
		context = c;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public void run()
	{
		try
		{
			// On récupère l'endroit ou on se trouve
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(lastLocation != null)
			{
				// On calcule la route
				PathOverlay po = new PathOverlay(Color.RED, context);
				YOURSRoute yr = new YOURSRoute();
				ArrayList<double[]> al = yr.calculateRoute(lastLocation,null);
				
				// Pour chaque point
				for (int i=0; i<al.size(); i++)
				{
					GeoPoint p = new GeoPoint(al.get(i)[0], al.get(i)[1]);
					po.addPoint(p); // On ajoute le point au path overlay
				}
				// On ajoute la route à la map
		        mapView.getOverlays().add(po);
		        
		        // On ajoute un location overlay, et on dessine (Marqueur)
				GeoPoint locGeoPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());   
			    SimpleLocationOverlay oi = new SimpleLocationOverlay(context);
		        oi.setLocation(locGeoPoint);
		        oi.draw(new Canvas(), mapView, false);
		        ArrayList<SimpleLocationOverlay> overlayItemArray = new ArrayList<SimpleLocationOverlay>();
		        overlayItemArray.add(oi);
				mapView.getOverlays().addAll(0,overlayItemArray);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
