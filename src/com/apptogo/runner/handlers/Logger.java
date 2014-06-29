package com.apptogo.runner.handlers;

public class Logger {

	private static final boolean debug = true;
	
	public static void log( Object object, String message){
		if(debug)
			System.out.println(object.getClass() + " MESSAGE: " + message);
	}
	
	public static void log( Object object, int message){
		if(debug)
			System.out.println(object.getClass() + " MESSAGE: " + message);
	}
	
	public static void log( Object object, float message){
		if(debug)
			System.out.println(object.getClass() + " MESSAGE: " + message);
	}
}
