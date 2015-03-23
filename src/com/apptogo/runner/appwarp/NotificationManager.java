package com.apptogo.runner.appwarp;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterType;
import com.badlogic.gdx.math.Vector2;

public class NotificationManager 
{
	private static NotificationManager INSTANCE;
	public static void create()
	{
		INSTANCE = new NotificationManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static NotificationManager getInstance()
	{
		return INSTANCE;
	}
	public static void prepareManager(String playerName, CharacterType playerCharacterType)
	{
    	getInstance().playerName = playerName;
    	getInstance().playerCharacterType = playerCharacterType;
    }
	
	private String playerName;
	private CharacterType playerCharacterType;
	
	private boolean appWarpNotificationsEnabled = true;
		
	public void notifyStartRunning(Vector2 playerPosition)
	{
		//JSONObject data = getData(true, false, false, false, false, false, false, false, false, null);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("START_RUNNING", true);
			data.put("X", playerPosition.x);
			data.put("Y", playerPosition.y);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	/*
	public void notifyDieTop()
	{
		JSONObject data = getData(false, true, false, false, false, false, false, false, false, null);
		sendNotification(data);
	}
	
	public void notifyDieBottom()
	{
		JSONObject data = getData(false, false, true, false, false, false, false, false, false, null);
		sendNotification(data);
	}
	*/
	
	public void notifyJump(Vector2 playerPosition, float parameter1, float parameter2, float parameter3, float parameter4, boolean flag)
	{
		//JSONObject data = getData(false, false, false, true, false, false, false, false, false, null);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("JUMP", true);
			data.put("X", playerPosition.x);
			data.put("Y", playerPosition.y);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	public void notifySlide(Vector2 playerPosition)
	{
		//JSONObject data = getData(false, false, false, false, true, false, false, false, false, null);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("SLIDE", true);
			data.put("X", playerPosition.x);
			data.put("Y", playerPosition.y);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	public void notifyStandUp(Vector2 playerPosition)
	{
		//JSONObject data = getData(false, false, false, false, false, true, false, false, false, null);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("STAND_UP", true);
			data.put("X", playerPosition.x);
			data.put("Y", playerPosition.y);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	/*
	public void notifySlow()
	{
		JSONObject data = getData(false, false, false, false, false, false, true, false, false, null);
		sendNotification(data);
	}
	
	public void notifyAbortSlow()
	{
		JSONObject data = getData(false, false, false, false, false, false, false, true, false, null);
		sendNotification(data);
	}
	*/
	public void notifyReadyToRun()
	{
		//JSONObject data = getData(false, false, false, false, false, true, false, false, false, null);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("READY_TO_RUN", true);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	public void notifyAbility(CharacterAbilityType abilityType, Vector2 playerPosition)
	{
		//JSONObject data = getData(false, false, false, false, false, false, false, false, true, abilityType);
		JSONObject data = new JSONObject();
		try
		{
			data.put("PLAYER_NAME", playerName);
			data.put("ABILITY", true);
			data.put("X", playerPosition.x);
			data.put("Y", playerPosition.y);
		} 
		catch(JSONException e) { e.printStackTrace(); }
		sendNotification(data);
	}
	
	private void sendNotification(JSONObject data)
	{
		if( appWarpNotificationsEnabled )
		{
			WarpController.getInstance().sendGameUpdate(data.toString()); 
		}
	}
	

	public void screamMyName()
	{
		JSONObject data = new JSONObject();
		
		try 
	    {
			data.put("INITIAL_NOTIFICATION", true); 
			data.put("PLAYER_NAME", playerName); 
			data.put("PLAYER_CHARACTER", playerCharacterType); 
	    } 
	    catch (JSONException e) { e.printStackTrace(); }
		
		WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void disableAppWarpNotifications()
	{
		appWarpNotificationsEnabled = false;
	}
	
	public void enableAppWarpNotifications()
	{
		appWarpNotificationsEnabled = true;
	}
}
