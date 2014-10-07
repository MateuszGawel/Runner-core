package com.apptogo.runner.player;

import java.util.HashMap;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelWorld;

public class Statistics
{	
	private HashMap<String, Integer> gameLevels;
	private HashMap<CharacterAbilityType, Integer> abilities;
	private int coins;
	private int diamonds;
	
	public Statistics()
	{
		gameLevels = new HashMap<String, Integer>();
		
		gameLevels.put("map1.0", 100);
		
		abilities = new HashMap<CharacterAbilityType, Integer>();
		
		for(CharacterAbilityType characterAbilityType: CharacterAbilityType.values())
		{
			abilities.put(characterAbilityType, 0);
		}
		
		coins = 0;
		diamonds = 0;
	}

	public boolean getLevelUnlockStatus(Level level, LevelWorld levelWorld) 
	{
		if( ( gameLevels.containsKey( level.requiredLevels ) || level.requiredLevels.equals("") ) && getWorldStars( levelWorld ) >= level.requiredStars ) return true;
		else return false;
	}
	public int getLevelScore(Level level) 
	{
		if( gameLevels.containsKey( level.unlockKey ) )
		{
			return (int)gameLevels.get( level.unlockKey );
		}
		else
			return 0;
	}
	public void setLevelScore(Level level, int score)
	{
		gameLevels.put(level.unlockKey, score);
	}
	
	public void setUnlockedLevels(HashMap<String, Integer> levels)
	{
		this.gameLevels = levels;
	}
	public HashMap<String, Integer> getUnlockedLevels()
	{
		return this.gameLevels;
	}
	/*ta funkcja ma sens wtedy kiedy przenosimy logike obliczania gwiazdek do campaignScreena ale czy to ma sens? takie rozwiazanie np praktycznie wymusza liniowa zaleznosc pkt od gwiazdek*/
	public int getWorldStars(LevelWorld levelWorld)
	{
		int score = 0;
		
		for(Level level: levelWorld.getLevels() )
		{
			score += this.getLevelScore( level );
		}
		
		return 10;//score;
	}	
	
	public int getAbilityLevel(CharacterAbilityType characterAbilityType)
	{
		return abilities.get(characterAbilityType);
	}
	public void setAbilityLevel(CharacterAbilityType characterAbilityType, Integer level)
	{
		abilities.put(characterAbilityType, level);
	}
	
	public int getCoinsValue()
	{
		return this.coins;
	}
	public void setCoinsValue(int coins)
	{
		this.coins = coins;
	}
	public void addCoin()
	{
		addCoins(1);
	}
	public void addCoins(int coins)
	{
		this.coins += coins;
	}
	public boolean takeCoins(int howMuch)
	{
		if( howMuch > this.coins )
		{
			return false;
		}
		else
		{
			this.coins -= howMuch;
			return true;
		}
	}
	
	public int getDiamondsValue()
	{
		return this.diamonds;
	}
	public void setDiamondsValue(int diamonds)
	{
		this.diamonds = diamonds;
	}
	public void addDiamonds(int diamonds)
	{
		this.diamonds += diamonds;
	}
	public boolean takeDiamonds(int howMuch)
	{
		if( howMuch > this.diamonds )
		{
			return false;
		}
		else
		{
			this.diamonds -= howMuch;
			return true;
		}
	}
}
