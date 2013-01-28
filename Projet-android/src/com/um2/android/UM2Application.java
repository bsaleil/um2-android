package com.um2.android;

import android.app.Application;
import android.os.Vibrator;
import android.widget.Toast;

public class UM2Application extends Application
{
	private Building targetBuilding;
	
	public void setTargetBuilding(Building building)
	{
		targetBuilding = building;
		Toast t = Toast.makeText(getApplicationContext(),targetBuilding.getName(this), Toast.LENGTH_SHORT);
		t.show();
		Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		v.vibrate(300);
	}
	
	public Building getTargetBuilding()
	{
		return targetBuilding;
	}
}
