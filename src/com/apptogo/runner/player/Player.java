package com.apptogo.runner.player;

import com.apptogo.runner.levels.Level;


public class Player 
{

	private String name;
	private String password;
	private String currentCharacter;
	private String unlockedLevels;
	
	private Statistics statistics;	
	
	public Player()
	{
		
	}
	
	public boolean isAnonymous()
	{
		if( name == "" )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void unlockLevel(Level level)
	{
		unlockedLevels += ",";
		unlockedLevels += level.unlockKey;
	}
	
	public boolean isLevelUnlocked(Level level)
	{
		if( unlockedLevels.contains(level.unlockKey) ) return true;
		else return false;
	}
	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getCurrentCharacter() {
		return currentCharacter;
	}
	public void setCurrentCharacter(String currentCharacter) {
		this.currentCharacter = currentCharacter;
	}

	public String getUnlockedLevels() {
		return unlockedLevels;
	}
	public void setUnlockedLevels(String unlockedLevels) {
		this.unlockedLevels = unlockedLevels;
	}
}
