package com.apptogo.runner.player;

import com.apptogo.runner.handlers.LanguageManager;

public class SaveManager 
{
	private static final SaveManager INSTANCE = new SaveManager();
	public static SaveManager getInstance()
	{
		return INSTANCE;
	}
	
	public SaveManager()
	{
		
	}
}
