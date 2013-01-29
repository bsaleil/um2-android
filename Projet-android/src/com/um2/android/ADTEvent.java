package com.um2.android;

import java.util.Scanner;
import java.util.regex.Pattern;

import android.util.Log;

import net.fortuna.ical4j.model.component.VEvent;

public class ADTEvent
{	
	private VEvent event;
	
	public ADTEvent(VEvent e)
	{
		event = e;
	}
	
	public String getADTSummary()
	{
		return event.getSummary().getValue();
	}
	
	public int getADTBuilding()
	{
		String location = event.getLocation().getValue();
		Scanner s = new Scanner(location);
		Pattern p = Pattern.compile("[0-9]+");
	    
		String found = s.findInLine(p);
		if (found == null || found.equals("")) // Si pas de batiment trouvé
			return -1;
		else			// Sinon, le batiment est trouvé
		{
			return Integer.parseInt(found);
		}
	}
	
	public String getADTStartDay()
	{
		String start = event.getStartDate().getValue(); // Get string date
		String r = "";
		r += start.substring(6,8) + "/";	// Ajoute le jour
		r += start.substring(4,6) + "/";	// Ajoute le mois
		r += start.substring(0,4);			// Ajoute l'année
		
		return r;
	}
	
	public String getADTEndDay()
	{
		String end = event.getEndDate().getValue(); // Get string date
		String r = "";
		r += end.substring(6,8) + "/";	// Ajoute le jour
		r += end.substring(4,6) + "/";	// Ajoute le mois
		r += end.substring(0,4);			// Ajoute l'année
		
		return r;
	}
	
	public int getADTMinutesStart()
	{
		String start = event.getStartDate().getValue();
		String startH = start.substring(9, 11); // Récupère l'h de debut
		String startM = start.substring(11, 13);// Récupère l'm de fin
		
		return Integer.parseInt(startH)*60 + Integer.parseInt(startM);
	}
	
	public int getADTMinutesEnd()
	{
		String end = event.getStartDate().getValue();
		String endH = end.substring(9, 11); // Récupère l'h de debut
		String endM = end.substring(11, 13);// Récupère l'm de fin
		
		return Integer.parseInt(endH)*60 + Integer.parseInt(endM);
	}
}
