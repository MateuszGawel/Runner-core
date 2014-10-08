package com.apptogo.runner.settings;

import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.SaveManager;

public class Settings 
{
	public boolean soundState;
	public boolean musicState;
	public boolean vibrationState;
	
	public int soundLevel;
	public int musicLevel;
	
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
		return soundState;
	}

	public void setSoundState(boolean soundState) 
	{
		this.soundState = soundState;
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

	public int getSoundLevel() 
	{
		return soundLevel;
	}

	public void setSoundLevel(int soundLevel) 
	{
		this.soundLevel = soundLevel;
	}

	public int getMusicLevel() 
	{
		return musicLevel;
	}

	public void setMusicLevel(int musicLevel) 
	{
		this.musicLevel = musicLevel;
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
