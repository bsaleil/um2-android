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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

// Ce thread est chargé de calculer la route depuis la position actuelle, vers un point donné
public class RouteThread extends Thread 
{
	private Context context;
	private MapView mapView;
	private Location position;
	private Building targetBuilding;
	Handler updateHandler;
	
	public RouteThread(MapView mv, Context c, Location l, Handler h) 
	{
		mapView = mv;
		context = c;
		position = l;
		targetBuilding = null; // Si null, le chemin n'est pas dessiné
		updateHandler = h;
	}
	
	public void setPosition(Location l)
	{
		position = l;
	}
	
	public void setTargetBuilding(Building building)
	{
		targetBuilding = building;
	}

	public void run() 
	{		
		try
		{		
			while(true)
			{
			// On récupère l'endroit ou on se trouve
			// Si la position est nulle, ou que le batiment de destination est null, on fait rien
			if (position != null && targetBuilding != null) 
			{
				// Enlève les anciennes routes
				mapView.getOverlays().clear();
				
				// On calcule la route
				PathOverlay po = new PathOverlay(Color.RED, context);
				YOURSRoute yr = new YOURSRoute(targetBuilding.getPoints().get(0));
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
				
				// Envois le message au handler pour mettre la carte à jour
				Message msg = new Message();
				updateHandler.sendMessage(msg);
			}
			else // Lecture position impossible
			{
				Log.d("MONTAG", "ERROR");
			}
			
			try{Thread.sleep(1000);} 
	        catch(Exception e){}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
}
