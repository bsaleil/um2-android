package com.um2.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fortuna.ical4j.model.component.VEvent;

import android.app.ListFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScheduleTab extends ListFragment
{
	List<VEvent> events;
	ICSReader icsr;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> listItems = new ArrayList<String>();
		
		// Récupère les evenements du jour
		icsr = new ICSReader(this.getActivity(), PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
		
		//TODO LIRE EN BD
		
		/*events = icsr.getDayEvents();
		// Les ajoute au tableaux
		for (int i=0; i<events.size(); i++)
		{
			listItems.add(icsr.eventToString(events.get(i)));
		}*/
		// Définit l'adapter
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
		// Récupère l'event selectionné
		VEvent e = events.get(position);
		int n = icsr.getBuildingNumberFromEvent(e);
    	
		// Interroge la bd pour récupérer le batiment
		DBController dbController = new DBController(this.getActivity());
		dbController.open();
	    Building b = dbController.getBuildingWithNumber(n);
	    dbController.close();
    	
	    // Définit le nouveau batiment
    	((UM2Application) getActivity().getApplication()).setTargetBuilding(b);
    	getActivity().finish();
    }
}
