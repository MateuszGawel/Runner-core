package com.apptogo.runner.logger;

import com.apptogo.runner.player.Player;
import com.apptogo.runner.screens.CampaignScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.badlogic.gdx.utils.Array;

public class Logger {

	private static final boolean debugAll = true;
	private static final boolean debugCustom = true;
		
	private static final Array<Class> classes = new Array<Class>( new Class[]{ 
																				MainMenuScreen.class,
																				CampaignScreen.class,
																				Player.class
																			 } );
		
	public static void log( Object object, String message)
	{
		if(debugAll)
		{
			System.out.println(object.getClass() + " MESSAGE: " + message);
		}
		else if(debugCustom)
		{
			if( classes.contains( object.getClass(), true) )
			{
				System.out.println(object.getClass() + " MESSAGE: " + message);
			}
		}	
	}
	
	public static void log( Object object, int message)
	{
		log(object, String.valueOf(message));
	}
	
	public static void log( Object object, float message)
	{
		log(object, String.valueOf(message));
	}
}
