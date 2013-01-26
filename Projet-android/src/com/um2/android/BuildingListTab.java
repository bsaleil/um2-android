package com.um2.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
 
//Onglet des batiments de la fac dans la vue liste
public class BuildingListTab extends ListFragment {
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	
    	super.onActivityCreated(savedInstanceState);
    	
    	ArrayList<String> listItems=new ArrayList<String>();
    	listItems.add("ITEM");
    	
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems));
    }
 
}
