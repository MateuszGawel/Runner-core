package com.apptogo.runner.levels;

public class Level 
{
	public String buttonLabel;
	public String mapPath;
	public String unlockKey;
	
	public Level(String buttonLabel, String mapPath, String unlockKey)
	{
		this.buttonLabel = buttonLabel;
		this.mapPath = mapPath;
		this.unlockKey = unlockKey;
	}
}
