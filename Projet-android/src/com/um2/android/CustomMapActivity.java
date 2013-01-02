package com.um2.android;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CustomMapActivity extends Activity implements LocationListener 
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private ArrayList<OverlayItem> overlayItemArray;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initializeMap();
	}
	
	public void initializeMap()
	{
		mapView = new MapView(this, 256);
		mapView.setClickable(true);
		mapController = this.mapView.getController();
		
		// Zooms
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		
		// Afficher la carte
		setContentView(mapView); 
		
		// Niveau de zoom initial
		mapController.setZoom(14); 
		
		// Entrée de la fac de science
		mapController.setCenter(new GeoPoint(43.6315843, 3.8612323)); 
		
		// Charger la carte en ligne : passer à false pour charger en local uniquement
		mapView.setUseDataConnection(true);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20, this);
		
		//--- Create Overlay
	        overlayItemArray = new ArrayList<OverlayItem>();
	        
	        DefaultResourceProxyImpl defaultResourceProxyImpl 
	         = new DefaultResourceProxyImpl(this);
	        MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);
	        mapView.getOverlays().add(myItemizedIconOverlay);
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);
		GeoPoint gpt = new GeoPoint(lat, lng);
		mapController.setCenter(gpt);
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
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

			if (!overlayItemArray.isEmpty()) 
			{
				GeoPoint in = overlayItemArray.get(0).getPoint();

				Point out = new Point();
				mapview.getProjection().toPixels(in, out);

				Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.focus_gps);
				canvas.drawBitmap(bm, out.x - bm.getWidth()/2, out.y - bm.getHeight()/2, null);
			}
		}
	}
}
