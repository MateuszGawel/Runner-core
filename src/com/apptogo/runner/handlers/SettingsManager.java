package com.apptogo.runner.handlers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsManager 
{
	private static final SettingsManager INSTANCE = new SettingsManager();
	public static SettingsManager getInstance()
	{
		return INSTANCE;
	}
		
	private Preferences preferences;
	
	private String GAME_SOUND_ID = "GAME_SOUND";
	private String VIBRATION_ID = "VIBRATION";
	private String LANGUAGE_ID = "LANGUAGE";
	
	public SettingsManager()
	{
		preferences = Gdx.app.getPreferences("gamePreferences");
	}
	
	//GAME SOUND
	public boolean getGameSoundState()
	{
		boolean state = preferences.getBoolean(GAME_SOUND_ID, true);
		
		return state;
	}
	public void setGameSoundState(boolean state)
	{
		preferences.putBoolean(GAME_SOUND_ID, state);
		preferences.flush();
	}
	
	//VIBRATION
	public boolean getVibrationState()
	{
		boolean state = preferences.getBoolean(VIBRATION_ID, true);
		
		return state;
	}
	public void setVibrationState(boolean state)
	{
		preferences.putBoolean(VIBRATION_ID, state);
		preferences.flush();
	}
	
	//LANGUAGE
	public String getLanguage()
	{
		String state = preferences.getString(LANGUAGE_ID, LanguageManager.getInstance().getDefaultLanguageId()); //tu sie robi male kolko (LM <- [BS] -> SM -> LM) ale tak jest najbardziej logicznie |LM=LanguageManager itd
		return state;
	}
	public void setLanguage(String language)
	{
		preferences.putString(LANGUAGE_ID, language);
		preferences.flush();
	}		
}
