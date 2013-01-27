package com.um2.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.util.Log;

/* Classe parsant le fichier des points d'interêts (toilettes, distributeurs, parking...)*/
public class PoiCsvReader
{
	public PoiCsvReader()
	{
	}

	public static ArrayList<Building> readFile(String file, AssetManager am)
	{
		String category = "default";
		ArrayList<Building> buildings = new ArrayList<Building>();
		try
		{
			InputStreamReader isr = new InputStreamReader(am.open(file));
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine(); // Read first line
			
			while((line = reader.readLine()) != null)
			{
				if (line.startsWith("#") || line.equals("") || line.startsWith(" "))
				{
					// Catégories de POI
					if(line.startsWith("#"))
						category = line.substring(1);
					
					line = reader.readLine();
				}
				
				String str[] = line.split(":");
				int buildingNumber = Integer.parseInt(str[0]);
				Building b = new Building(buildingNumber); // Create new building
				
				// Ces bâtiments ont toujours un seul point
				b.addPoint(Double.parseDouble(str[2]), Double.parseDouble(str[3]));
				b.setCategory(category);
				
				buildings.add(b);
			}
		
			reader.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}	
		
		return buildings;
	}

}
