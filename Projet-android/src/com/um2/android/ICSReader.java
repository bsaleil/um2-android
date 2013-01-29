package com.um2.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.VEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ICSReader
{
	private String icsFile; // Nom du fichier ics. Se fichier est obligatoirement à la racine de la SD
	private Context context;
	private SharedPreferences preferences;
	
	public ICSReader(Context c, SharedPreferences p)
	{
		preferences = p;
		icsFile = preferences.getString(SettingsFragment.FICHIER_ICS, "");
		Log.d("MONTAG",icsFile);
		context = c;
	}
	
	// Retourne les evenements du fichier entré dans les options
	public List<ADTEvent> readEventsFromPrefFile()
	{
		File dir = Environment.getExternalStorageDirectory();
		File file = new File(dir, icsFile);
		if (file.exists()) // Si le ics existe
		{
			try
			{
				FileInputStream fis = new FileInputStream(file);
				CalendarBuilder builder = new CalendarBuilder();
				
				// Créé un calendrier depuis le fichier ics
				Calendar calendar = builder.build(fis);
				
				// On récupère la liste des évènements
				List<ADTEvent> eventsToday = calendar.getComponents(Component.VEVENT);
				
				return eventsToday;
			}
			catch (IOException e) { e.printStackTrace(); }
			catch (ParserException e) { e.printStackTrace(); }
			
		}
		else
		{
			Log.d("MONTAG", "Fichier ICS inexistant.");
			return (new ArrayList<ADTEvent>());
		}
		return (new ArrayList<ADTEvent>());
	}
	
	// Retourne une chaine qui décrit l'évènement
	public String eventToString(VEvent e)
	{
		String start = e.getStartDate().getValue();
		String startH = start.substring(9, 11);
		String startM = start.substring(11, 13);
		
		String end = e.getEndDate().getValue();
		String endH = end.substring(9, 11);
		String endM = end.substring(11, 13);
		
		String r = "";
		r += startH + ":" + startM + " - ";
		r += endH + ":" + endM + "\n";
		r += context.getString(R.string.building) + " " + getBuildingNumberFromEvent(e) + "\n";
		r += e.getSummary().getValue();
		return r;
	}
	
	// Retourne le numéro de batiment depuis l'event
	public int getBuildingNumberFromEvent(VEvent e)
	{
		String location = e.getLocation().getValue();
		
	    Scanner s = new Scanner(location);
	    Pattern p = Pattern.compile("[0-9]+");
	    
	    return Integer.parseInt(s.findInLine(p));
	}
	
	// Retourne le batiment du prochain cours
	public Building getNextBuilding()
	{
		// TODO
		/*List<VEvent> events = getDayEvents();
		if (events.size() > 0)
		{
			VEvent nextEvent = events.get(0); // Premier event
			// Pour chaque event
			for (int i=0; i<events.size(); i++)
			{
				if (eventValue(events.get(i)) < eventValue(nextEvent))
					nextEvent = events.get(i);
			}
			
			// Interroge la bd pour récupérer le batiment
			DBController dbController = new DBController(context);
			dbController.open();
		    Building b = dbController.getBuildingWithNumber(getBuildingNumberFromEvent(nextEvent));
		    dbController.close();
		    if (b != null)
		    {
		    	Toast.makeText(context, eventToString(nextEvent), Toast.LENGTH_LONG).show();
		    	return b;
		    }
		}*/
		return null;
	}
	
	public int eventValue(VEvent e)
	{
		String start = e.getStartDate().getValue();
		String startH = start.substring(9, 11);
		String startM = start.substring(11, 13);
		
		return Integer.parseInt(startH)*60 + Integer.parseInt(startM);
	}
}

