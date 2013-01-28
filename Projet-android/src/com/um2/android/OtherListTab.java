package com.um2.android;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Onglet des autres batiments dans la vue liste
public class OtherListTab extends ListFragment
{
	private ArrayList<String> listItems;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		listItems = new ArrayList<String>();

		DBController db = new DBController(getActivity());
		db.open();
		
		listItems = db.getAllCategories();
		db.close();

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listItems));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = new Intent(getActivity().getApplicationContext(), CustomMapActivity.class);
<<<<<<< HEAD
		intent.putExtra("category",listItems.get((int)id));
		getActivity().setResult(1,intent);
		getActivity().finish();		
=======
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent.putExtra("category", listItems.get((int)id));
		((UM2Application) getActivity().getApplication()).setTargetBuilding(null);
		
		startActivity(intent);
>>>>>>> 58fd10b88ac797b551ffa337fae87236769f043c
	}
}
