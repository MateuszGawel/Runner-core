package com.apptogo.runner.player;

import java.util.HashMap;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.levels.LevelWorld;
import com.badlogic.gdx.utils.Array;

public class Player 
{
	public HashMap<String, Integer> gameLevels;
	public HashMap<String, Integer> abilities;
	
	public int coins, diamonds;
	
	public Array<String> friends;
	
	public boolean slidePressed; //pomocnicza
	public boolean jumpPressed; //pomocnicza
	public boolean tempPressed; //pomocnicza
	public boolean abilityPressed; //pomocnicza
	
	private String name;
	private String password;
	
	private CharacterType characterType;
	public Character character;
	
	public boolean readyToRun;
		
	public Player()
	{
		name = "";
		password = "";
		
		characterType = CharacterType.BANDIT;
		
		this.gameLevels = new HashMap<String, Integer>();
		this.abilities = new HashMap<String, Integer>();
		
		for(CharacterAbilityType characterAbilityType: CharacterAbilityType.values())
		{
			abilities.put(characterAbilityType.toString(), 1);
		}
		
		this.coins = 1000000;
		this.diamonds = 0;
		
		friends = new Array<String>();
	}
	
	public Player(String name, CharacterType characterType)
	{
		this();
		this.name = name;
		this.characterType = characterType;
		this.character = null;
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
	
	public int getLevelScore(Level level) 
	{
		if( gameLevels.containsKey( level.unlockKey ) )
		{
			return (int)gameLevels.get( level.unlockKey );
		}
		else
			return 0;
	}
	
	public int getWorldStars(LevelWorld levelWorld)
	{
		int score = 0;
		
		for(Level level: levelWorld.getLevels() )
		{
			score += this.getLevelScore( level );
		}
		
		return score;
	}	
	
	public void setLevelScore(Level level, int score)
	{
		if( score >= 0)
		{
			this.gameLevels.put(level.unlockKey, score);
		}
	}
	public boolean isLevelUnlocked(Level level, LevelWorld levelWorld)
	{
		if( ( gameLevels.containsKey( level.requiredLevels ) || level.requiredLevels.equals("") ) && getWorldStars( levelWorld ) >= level.requiredStars ) return true;
		else return false;
	}
	
	public int getAbilityLevel(CharacterAbilityType characterAbilityType)
	{
		return abilities.get(characterAbilityType.toString()).intValue();
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
	
	public boolean addFriend(String friendName)
	{
		if( friends.indexOf(friendName, false) == -1 )
		{
			friends.add(friendName);
			
			return true;
		}
		
		return false;
	}
	public boolean removeFriend(String friendName)
	{
		if( friends.indexOf(friendName, false) == -1 )
		{
			return false;	
		}
		
		friends.removeIndex( friends.indexOf(friendName, false) );
		
		return true;
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
		copyOfThis.abilities = this.abilities;
		
		copyOfThis.coins = this.coins;
		
		SaveManager.getInstance().save( copyOfThis );
	}
}
