package com.apptogo.runner.player;

import java.util.HashMap;
import java.util.Random;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelWorld;


public class Player 
{
	private String name;
	private String password;
	
	private CharacterType characterType;
	public Character character;
	
	private HashMap<String, Integer> levels;
	
	private Statistics statistics;	
	
	public Player()
	{
		levels = new HashMap<String, Integer>();
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
	
	public void unlockLevel(Level level)
	{
		levels.put(level.unlockKey, 0);
	}
	
	public boolean isLevelUnlocked(Level level)
	{
		if( levels.containsKey( level.unlockKey ) ) return true;
		else return false;
	}
	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = "jaJuzNie";
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
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

	/*ta funkcja ma sens wtedy kiedy przenosimy logike obliczania gwiazdek do campaignScreena ale czy to ma sens? takie rozwiazanie np praktycznie wymusza liniowa zaleznosc pkt od gwiazdek*/
	public int getWorldScore(LevelWorld levelWorld)
	{
		int score = 0;
		
		for(Level level: levelWorld.getLevels() )
		{
			score += this.getLevelScore( level );
		}
		
		return score;
	}
	public int getLevelScore(Level level)
	{
		if( levels.containsKey( level.unlockKey ) )
		{
			return (int)levels.get( level.unlockKey );
		}
		else
			return 0;
	}
	public void setLevelScore(Level level, int score)
	{
		levels.put(level.unlockKey, score);
	}
	
	public void setUnlockedLevels(HashMap<String, Integer> levels)
	{
		this.levels = levels;
	}
	public HashMap<String, Integer> getUnlockedLevels()
	{
		return this.levels;
	}
}
