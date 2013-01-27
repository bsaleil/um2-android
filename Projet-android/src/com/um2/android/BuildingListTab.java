package com.um2.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

//Onglet des batiments de la fac dans la vue liste
public class BuildingListTab extends ListFragment
{
	ArrayList<Building> buildings;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> listItems = new ArrayList<String>();

		DBController db = new DBController(getActivity());
		db.open();
		buildings = db.getAllBuidings();
		db.close();

		for (int i = 0; i < buildings.size(); i++)
		{
			listItems.add(buildings.get(i).getName(this.getActivity()));
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
