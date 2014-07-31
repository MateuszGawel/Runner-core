package com.apptogo.runner.handlers;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.main.Runner;

public class NotificationManager 
{
	private static final NotificationManager INSTANCE = new NotificationManager();
	public static NotificationManager getInstance()
	{
		return INSTANCE;
	}
	public static void prepareManager(String playerName)
	{
    	getInstance().playerName = playerName;
    }
	
	private String playerName;
		
	public void notifyStartRunning()
	{
		JSONObject data = getData(true, false, false, false, false, false, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyDieTop()
	{
		JSONObject data = getData(false, true, false, false, false, false, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyDieBottom()
	{
		JSONObject data = getData(false, false, true, false, false, false, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyJump()
	{
		JSONObject data = getData(false, false, false, true, false, false, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifySlide()
	{
		JSONObject data = getData(false, false, false, false, true, false, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyStandUp()
	{
		JSONObject data = getData(false, false, false, false, false, true, false, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifySlow()
	{
		JSONObject data = getData(false, false, false, false, false, false, true, false, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyAbortSlow()
	{
		JSONObject data = getData(false, false, false, false, false, false, false, true, false, null);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	public void notifyAbility(CharacterAbilityType abilityType)
	{
		JSONObject data = getData(false, false, false, false, false, false, false, false, true, abilityType);
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	private JSONObject getData(boolean startRunningState, boolean dieTopState, boolean dieBottomState, boolean jumpState, boolean slideState, boolean standUpState, boolean slowState, boolean abortSlowState, boolean abilityState, CharacterAbilityType abilityType)
	{
		JSONObject data = new JSONObject();
		
		try 
	    {
			data.put("PLAYER_NAME", playerName); // - to nam identyfikuje playera
			
			data.put("START_RUNNING", startRunningState);
			data.put("DIE_TOP", dieTopState);
			data.put("DIE_BOTTOM", dieBottomState);
			data.put("JUMP", jumpState);
			data.put("SLIDE", slideState);
			data.put("STAND_UP", standUpState);
			data.put("SLOW", slowState);
			data.put("ABORT_SLOW", abortSlowState);
			data.put("ABILITY", abilityState);
			
			if(abilityType != null) data.put("ABILITY_TYPE", abilityType.toString());
			else data.put("ABILITY_TYPE", "");
	    } 
	    catch (JSONException e) { e.printStackTrace(); }
		
		return data;
	}
	
	public void screamMyName()
	{
		JSONObject data = new JSONObject();
		
		try 
	    {
			data.put("INITIAL_NOTIFICATION", true); 
			data.put("PLAYER_NAME", playerName); 
			//tu musza byc jeszcze inne info nt statsow itp
	    } 
	    catch (JSONException e) { e.printStackTrace(); }
		
		WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
}
