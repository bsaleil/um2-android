package com.um2.android;

import android.app.Application;

public class UM2Application extends Application
{
	private Building targetBuilding;
	
	public void setTargetBuilding(Building building)
	{
		targetBuilding = building;
	}
	
	public Building getTargetBuilding()
	{
		return targetBuilding;
	}
}
