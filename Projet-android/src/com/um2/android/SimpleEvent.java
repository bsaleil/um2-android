package com.um2.android;

import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;

public class SimpleEvent
{
	private String summary;
	private int numBuilding;
	private String startDay; // jj/mm/aaaa
	private String endDay; // jj/mm/aaaa
	private int minutesStart;
	private int minutesEnd;
	
	public SimpleEvent()
	{
	}
	
	public String eventToString(Context c)
	{
		String s = "";
		
		if (minutesStart/60 < 10) s+= "0" + String.valueOf(minutesStart/60) + ":";
		else s+= String.valueOf(minutesStart/60) + ":";
		
		if (minutesStart%60 < 10) s+= "0" + String.valueOf(minutesStart%60) + " - ";
		else s+= String.valueOf(minutesStart%60) + " - ";
		
		if (minutesEnd/60 < 10) s+= "0" + String.valueOf(minutesEnd/60) + ":";
		else s+= String.valueOf(minutesEnd/60) + ":";
		
		if (minutesEnd%60 < 10) s+= "0" + String.valueOf(minutesEnd%60);
		else s+= String.valueOf(minutesEnd%60);
		
		if (numBuilding > 0)
			s += "\n" + c.getString(R.string.building) + " " + String.valueOf(numBuilding);
		s += "\n"+summary;
		
		return s;
	}
	
	public int getNumBuilding()
	{
		return numBuilding;
	}
	
	public int getMinutesStart()
	{
		return minutesStart;
	}
	
	public int getMinutesEnd()
	{
		return minutesEnd;
	}
	
	public void setADTSummary(String s)
	{
		summary = s;
	}
	
	public void setADTBuilding(int num)
	{
		numBuilding = num;
	}
	
	public void setADTStartDay(String date)
	{
		startDay = date;
	}
	
	public void setADTEndDay(String date)
	{
		endDay = date;
	}
	
	public void setADTMinutesStart(int minutes)
	{
		minutesStart = minutes;
	}
	
	public void setADTMinutesEnd(int minutes)
	{
		minutesEnd = minutes;
	}
}
