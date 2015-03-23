package com.apptogo.runner.news;

import com.badlogic.gdx.utils.Array;

public class NewsManager 
{
	private static NewsManager INSTANCE;
	public static void create()
	{
		INSTANCE = new NewsManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	
	public static NewsManager getInstance()
	{
		return INSTANCE;
	}
	
	private Array<News> newsArray;
	
	private NewsManager()
	{
		newsArray = new Array<News>();
	}	
	
	public boolean isNewContent()
	{
		//sprawdzanie czy jest jakas nowa wiadomosc...
		//if(jest)
		return true;
		//else
		//return false
	}
	
	private void updateNewsArray()
	{
		newsArray.clear();
		
		//--TEMP DATA
        newsArray.add(new News("03.11.2014", "Topic one", "Message one. Message should be a little longer than topic to make it possible to see how label is wrapping at the end of cell"));
        newsArray.add(new News("01.11.2014", "Topic two", "Message one. Message should be a little longer than topic to make it possible to see how label is wrapping at the end of cell"));
        newsArray.add(new News("27.10.2014", "Topic three", "Message one. Message should be a little longer than topic to make it possible to see how label is wrapping at the end of cell"));
        newsArray.add(new News("20.10.2014", "Topic four", "Message one. Message should be a little longer than topic to make it possible to see how label is wrapping at the end of cell"));
        //--
	}
	
	public Array<News> getNewsArray()
	{
		updateNewsArray();
		
        return newsArray;
	}
}
