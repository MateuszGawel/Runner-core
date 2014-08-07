package com.apptogo.runner.levels;

import com.apptogo.runner.world.GameWorld.GameWorldType;

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
}
