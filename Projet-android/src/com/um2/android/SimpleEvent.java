package com.um2.android;

import java.util.Scanner;
import java.util.regex.Pattern;

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
