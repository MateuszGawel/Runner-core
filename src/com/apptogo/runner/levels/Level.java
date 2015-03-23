package com.apptogo.runner.levels;

import com.apptogo.runner.enums.GameWorldType;

public class Level 
{
	public String buttonLabel;
	public String mapPath;
	public String unlockKey;
	public String requiredLevels;
	public int requiredStars;
	public GameWorldType worldType;
	
	public Level(String buttonLabel, String mapPath, String unlockKey, String requiredLevels, String requiredStars)
	{
		this.buttonLabel = buttonLabel;
		this.mapPath = mapPath;
		this.unlockKey = unlockKey;
		this.requiredLevels = requiredLevels;
		this.requiredStars = Integer.parseInt( requiredStars );
	}
	
	public Level(String buttonLabel, String mapPath, String unlockKey, String requiredLevels, String requiredStars, GameWorldType worldType)
	{
		this(buttonLabel, mapPath, unlockKey, requiredLevels, requiredStars);
		this.worldType = worldType;
	} //to tylko chwilowe bo wywolujemy to na pale w waiting room - potem trzeba przerobic i wywalic ten konstruktor
}
