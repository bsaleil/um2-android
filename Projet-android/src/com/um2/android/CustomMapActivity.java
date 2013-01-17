package com.um2.android;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CustomMapActivity extends Activity
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private ArrayList<OverlayItem> overlayItemArray;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initializeMap();
		
		new Thread(new Runnable() 
		{
			public void run()
			{
				testRoute();
			}
		}).start();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_map, menu);
		return true;
	}
	
	// Listener du click sur le bouton focus du menu
	public void onFocusClick(MenuItem item)
	{
//		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		if(lastLocation != null)
//		{
//			updateLoc(lastLocation);
//		}
	}
	
	public void initializeMap()
	{
		mapView = new MapView(this, 256);
		mapView.setClickable(true);
		mapController = this.mapView.getController();
		
		mapView.setMultiTouchControls(true);
		
		// Afficher la carte
		setContentView(mapView); 
		
		// Niveau de zoom initial
		mapController.setZoom(14);
		
		// Entrée de la fac de science
		mapController.setCenter(new GeoPoint(43.6315843, 3.8612323));
		
		// Charger la carte en ligne : passer à false pour charger en local uniquement
		mapView.setUseDataConnection(true);
		
		//--- Create Overlay
//		overlayItemArray = new ArrayList<OverlayItem>();
//		
//		DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
//		MyItemizedIconOverlay myItemizedIconOverlay 
//			= new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);
//		mapView.getOverlays().add(myItemizedIconOverlay);
//		
//		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//		
//		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		if(lastLocation != null)
//		{
//			updateLoc(lastLocation);
//		}
//		
//		ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
//		mapView.getOverlays().add(myScaleBarOverlay);
	}
	
	public void testRoute()
	{
		RoadManager roadManager = new OSRMRoadManager();
		//roadManager.addRequestOption("routeType=bicycle");
		
		ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
		waypoints.add(new GeoPoint(43.631975, 3.861254));
		waypoints.add(new GeoPoint(43.632647, 3.863856)); //end point
		Road road = roadManager.getRoad(waypoints);
		PathOverlay roadOverlay = RoadManager.buildRoadOverlay(road, mapView.getContext());
		
		mapView.getOverlays().add(roadOverlay);
		mapView.invalidate();
	}
	
/*	protected void onResume() 
	{	
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
	}

	protected void onPause() 
	{
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}
	
	private void updateLoc(Location loc)
	{
		GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
		mapController.setCenter(locGeoPoint);    	
	    	setOverlayLoc(loc);
	    	mapView.invalidate();
	}

	private void setOverlayLoc(Location overlayloc)
	{
		GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
		overlayItemArray.clear();
		OverlayItem newMyLocationItem = new OverlayItem("My Location", "My Location", overlocGeoPoint);
		overlayItemArray.add(newMyLocationItem);
    	}
	
	private LocationListener myLocationListener = new LocationListener()
	{
		public void onLocationChanged(Location location) 
		{
			updateLoc(location);
		}
	
		public void onProviderDisabled(String provider) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	    };
	    
	private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> 
	{
		public MyItemizedIconOverlay(
				List<OverlayItem> pList,
				org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
				ResourceProxy pResourceProxy) 
		{
			super(pList, pOnItemGestureListener, pResourceProxy);
		}

		public void draw(Canvas canvas, MapView mapview, boolean arg2) 
		{
			super.draw(canvas, mapview, arg2);

			if(!overlayItemArray.isEmpty()) 
			{
				GeoPoint in = overlayItemArray.get(0).getPoint();

				Point out = new Point();
				mapview.getProjection().toPixels(in, out);

				Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.focus_gps);
				canvas.drawBitmap(bm, out.x - bm.getWidth()/2, out.y - bm.getHeight()/2, null);
			}
		}
	}
	*/
}
