package com.apptogo.runner.logger;

import com.badlogic.gdx.utils.Array;

public class Logger 
{
	public enum LogLevel
	{
		HIGH,
		MEDIUM,
		LOW;
		
		public int toInt()
		{
			if( this == HIGH )
				return 0;
			else if ( this == MEDIUM )
				return 1;
			else if ( this == LOW )
				return 2;
			else
				return 0;
		}
	}
	
	private static final LogLevel LOG_LEVEL = LogLevel.HIGH;
	
	private static final boolean USE_WHITE_LIST = false;
	private static final boolean USE_BLACK_LIST = false;
	
	private static final Array<String> whiteList = new Array<String>( new String[]{ //"class com.apptogo.runner.listeners.MyContactListener",
																					//"class com.apptogo.runner.world.GameWorldRenderer",
																					//"class com.apptogo.runner.handlers.CustomAction"
																				  } ); 
	
	private static final Array<String> blackList = new Array<String>( new String[]{ "class com.apptogo.runner.handlers.ResourcesManager",
																					"class com.apptogo.runner.handlers.ResourcesManager$ScreenMeta"
																			  	  } ); 
		
	//OVERLOADS
	public static void log( Object object, Object message)
	{
		log(object, String.valueOf(message), LogLevel.HIGH);
	}
	public static void log( Object object, Object message, LogLevel logLevel)
	{
		log(object, String.valueOf(message), logLevel);
	}
	public static void log( Object object, String message)
	{
		log(object, message, LogLevel.HIGH);
	}	
	
	public static void log( Object object, String message, LogLevel logLevel)
	{
		if( logLevel.toInt() <= LOG_LEVEL.toInt() )
		{
			boolean printMessage = true;
			
			//Allow to log only classes from whiteList
			if( USE_WHITE_LIST )
			{
				if( !(whiteList.contains(object.getClass().toString(), false)) )
				{
					printMessage = false;
				}
			}
				
			//Deny from logging classes from blackList
			else if( USE_BLACK_LIST )
			{
				if( blackList.contains(object.getClass().toString(), false) )
				{
					printMessage = false;
				}
			}
			
			if(printMessage)
			{
				System.out.println("LOG [" + logLevel.toString() + "] | " + object.getClass().toString() + " | MESSAGE: " + message);
			}
		}
	}
}
