package com.um2.android;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.util.Log;

public class BuildingCsvReader
{
	BuildingCsvReader()
	{
		
	}

	public static ArrayList<Building> readFile(String file, AssetManager am)
	{
		ArrayList<Building> buildings = new ArrayList<Building>();
		try
		{			
			InputStreamReader isr = new InputStreamReader(am.open(file));
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine(); // Read first line
			
			while ((line = reader.readLine()) != null)
			{
				String str[] = line.split(":",5);
				int buildingNumber = Integer.parseInt(str[0]);
				int pointsNumber = Integer.parseInt(str[1]);
				Building b = new Building(buildingNumber); // Create new building
				b.addPoint(Double.parseDouble(str[2]), Double.parseDouble(str[3])); // Add first point
				
				for (int i=1; i<pointsNumber; i++) // For each point of this building
				{
					line = reader.readLine();
					str = line.split(":");
					b.addPoint(Double.parseDouble(str[2]), Double.parseDouble(str[3]));
				}
				buildings.add(b);
			}
		
			reader.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}	
		
		return buildings;
	}
}