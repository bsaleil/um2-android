package com.um2.android;

import android.app.Application;
import android.widget.Toast;

public class UM2Application extends Application
{
	private Building targetBuilding;
	
	public void setTargetBuilding(Building building)
	{
		targetBuilding = building;
		Toast t = Toast.makeText(getApplicationContext(),targetBuilding.getName(this), Toast.LENGTH_SHORT);
		t.show();
	}
	
	public Building getTargetBuilding()
	{
		return targetBuilding;
	}
}
