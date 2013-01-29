package com.um2.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.fortuna.ical4j.model.component.VEvent;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScheduleTab extends ListFragment
{
	List<SimpleEvent> events;
	ICSReader icsr;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> listItems = new ArrayList<String>();
		
		
		
		//TODO LIRE EN BD
		DBController dbC = new DBController(this.getActivity());
		dbC.open();
		//ArrayList<SimpleEvent> events = dbC.getAllEvents();
		events = dbC.getTodayEvents();
		dbC.close();
		
		// Les ajoute au tableaux
		for (int i=0; i<events.size(); i++)
		{
			listItems.add(events.get(i).eventToString(this.getActivity()));
		}
		// Définit l'adapter
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
		// Récupère l'event selectionné
		SimpleEvent e = events.get(position);
		int num = e.getNumBuilding();
    	if (num > 0)
    	{
			// Interroge la bd pour récupérer le batiment
			DBController dbController = new DBController(this.getActivity());
			dbController.open();
		    Building b = dbController.getBuildingWithNumber(num);
		    dbController.close();
	    	
		    // Définit le nouveau batiment
	    	((UM2Application) getActivity().getApplication()).setTargetBuilding(b);
	    	getActivity().finish();
    	}
    	else
    	{
    		AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
			alertDialog.setTitle(getString(R.string.attention));
			alertDialog.setMessage(getText(R.string.batiment_inconnu));
			alertDialog.show();
    	}
    }
}
