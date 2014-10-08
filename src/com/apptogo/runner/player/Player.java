package com.apptogo.runner.player;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelWorld;
import com.apptogo.runner.logger.Logger;

public class Player 
{
	private String name;
	private String password;
	
	private CharacterType characterType;
	public Character character;
		
	public Statistics statistics;	
		
	public Player()
	{
		statistics = new Statistics();
	}
	
	public Player(String name, CharacterType characterType)
	{
		this();
		this.name = name;
		this.characterType = characterType;
		this.character = null;
	}
		
	public Statistics getStatistics()
	{
		return this.statistics;
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
	
	public void setLevelScore(Level level, int score)
	{
		if( score > 0)
		{
			this.statistics.setLevelScore(level, score);
		}
	}
	public boolean isLevelUnlocked(Level level, LevelWorld levelWorld)
	{
		return statistics.getLevelUnlockStatus(level, levelWorld);
	}
	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getPassword() 
	{
		return password;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}

	public CharacterType getCharacterType() 
	{
		return characterType;
	}
	public void setCharacterType(CharacterType currentCharacter) 
	{
		this.characterType = currentCharacter;
	}
	
	static public Player load()
	{
		return SaveManager.getInstance().loadPlayer();
	}
	
	public void save()
	{
		//musze przekopiowac playera, zeby czasem nie zapisac instancji charactera do pamieci
		Player copyOfThis = new Player();
		
		copyOfThis.name = this.name;
		copyOfThis.password = this.password;
		copyOfThis.characterType = this.characterType;
		copyOfThis.character = null;
		copyOfThis.statistics = this.statistics;
		
		SaveManager.getInstance().save( copyOfThis );
	}
}
