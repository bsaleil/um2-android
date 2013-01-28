package com.um2.android;

import java.util.Scanner;
import java.util.regex.Pattern;

import net.fortuna.ical4j.model.component.VEvent;

public class ADTEvent extends VEvent
{
	
	public String getADTSummary()
	{
		return getSummary().getValue();
	}
	
	public int getADTBuilding()
	{
		String location = getLocation().getValue();
	    Scanner s = new Scanner(location);
	    Pattern p = Pattern.compile("[0-9]+");
	    
	    return Integer.parseInt(s.findInLine(p));
	}
	
	public String getADTStartDay()
	{
		String start = getStartDate().getValue(); // Get string date
		String r = "";
		r += start.substring(6,8) + "/";	// Ajoute le jour
		r += start.substring(4,6) + "/";	// Ajoute le mois
		r += start.substring(0,4);			// Ajoute l'année
		
		return r;
	}
	
	public String getADTEndDay()
	{
		String end = getEndDate().getValue(); // Get string date
		String r = "";
		r += end.substring(6,8) + "/";	// Ajoute le jour
		r += end.substring(4,6) + "/";	// Ajoute le mois
		r += end.substring(0,4);			// Ajoute l'année
		
		return r;
	}
	
	public int getADTMinutesStart()
	{
		String start = getStartDate().getValue();
		String startH = start.substring(9, 11); // Récupère l'h de debut
		String startM = start.substring(11, 13);// Récupère l'm de fin
		
		return Integer.parseInt(startH)*60 + Integer.parseInt(startM);
	}
	
	public int getADTMinutesEnd()
	{
		String end = getStartDate().getValue();
		String endH = end.substring(9, 11); // Récupère l'h de debut
		String endM = end.substring(11, 13);// Récupère l'm de fin
		
		return Integer.parseInt(endH)*60 + Integer.parseInt(endM);
	}
}
