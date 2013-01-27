package com.um2.android;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Onglet des autres batiments dans la vue liste
public class OtherListTab extends ListFragment
{
	ArrayList<Building> buildings;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> listItems = new ArrayList<String>();

		DBController db = new DBController(getActivity());
		db.open();
		buildings = db.getAllPoiBuidings();
		db.close();
		
		for(Building b : buildings)
		{
			listItems.add(b.getCategory()+" bâtiment "+b.getNumber());
		}

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listItems));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Building selected = buildings.get(position);
		((UM2Application) getActivity().getApplication()).setTargetBuilding(selected);
		getActivity().finish();
	}
}
