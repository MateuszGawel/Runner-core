package com.apptogo.runner.settings;

import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.SaveManager;

public class Settings 
{	
	public boolean soundsState;
	public boolean musicState;
	public boolean vibrationState;
	
	public float soundsLevel;
	public float musicLevel;
	
	public String language;
	
	public Settings()
	{
		language = LanguageManager.getInstance().getDefaultLanguageId();
	}
	
	static public Settings load()
	{
		return SaveManager.getInstance().loadSettings();
	}
	
	public void save()
	{
		SaveManager.getInstance().save(this);
	}

	public boolean isSoundState() 
	{
		return soundsState;
	}

	public void setSoundState(boolean soundState) 
	{
		this.soundsState = soundState;
	}

	public boolean isMusicState() 
	{
		return musicState;
	}

	public void setMusicState(boolean musicState) 
	{
		this.musicState = musicState;
	}

	public boolean isVibrationState() 
	{
		return vibrationState;
	}

	public void setVibrationState(boolean vibrationState) 
	{
		this.vibrationState = vibrationState;
	}

	public float getSoundLevel() 
	{
		return soundsLevel;
	}

	public void setSoundLevel(float f) 
	{
		this.soundsLevel = f;
	}

	public float getMusicLevel() 
	{
		return musicLevel;
	}

	public void setMusicLevel(float f) 
	{
		this.musicLevel = f;
	}

	public String getLanguage() 
	{
		return language;
	}

	public void setLanguage(String language) 
	{
		this.language = language;
	}

}
