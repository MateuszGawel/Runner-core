package com.apptogo.runner.levels;

import com.apptogo.runner.enums.GameWorldType;

public class Level 
{
	public String buttonLabel;
	public String mapPath;
	public String unlockKey;
	public GameWorldType worldType;
	
	public Level(String buttonLabel, String mapPath, String unlockKey)
	{
		this.buttonLabel = buttonLabel;
		this.mapPath = mapPath;
		this.unlockKey = unlockKey;
	}
	
	public Level(String buttonLabel, String mapPath, String unlockKey, GameWorldType worldType)
	{
		this(buttonLabel, mapPath, unlockKey);
		this.worldType = worldType;
	}
}
