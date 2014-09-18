package com.apptogo.runner.player;

import java.util.HashMap;
import java.util.Iterator;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.utils.Array;

public class Statistics
{
	private final int ABILITY_LEVELS_INDEX = 0;
	private final int CASH_INDEX = 1;
	private final int DIAMONDS_INDEX = 2;
	
	private HashMap<CharacterAbilityType, Integer> abilityLevels;
	private int cash;
	private int diamonds;
	
	public Statistics()
	{
		abilityLevels = new HashMap<CharacterAbilityType, Integer>();
		
		for(CharacterAbilityType characterAbilityType: CharacterAbilityType.values())
		{
			abilityLevels.put(characterAbilityType, 0);
		}
		
		cash = 0;
		diamonds = 0;
	}

	public int getAbilityLevel(CharacterAbilityType characterAbilityType)
	{
		return abilityLevels.get(characterAbilityType);
	}
	public void setAbilityLevel(CharacterAbilityType characterAbilityType, Integer level)
	{
		abilityLevels.put(characterAbilityType, level);
	}
	
	public int getCash()
	{
		return this.cash;
	}
	public void setCash(int cash)
	{
		this.cash = cash;
	}
	
	public int getDiamonds()
	{
		return this.diamonds;
	}
	public void setDiamonds(int diamonds)
	{
		this.diamonds = diamonds;
	}
	
	//Statistics serialization
	public void unserialize(String serializedStatistics)
	{	
		unserializeHashMap( serializedStatistics.split("\\$")[ ABILITY_LEVELS_INDEX ] );
		unserializeCash( serializedStatistics.split("\\$")[ CASH_INDEX ] );
		unserializeDiamonds( serializedStatistics.split("\\$")[ DIAMONDS_INDEX ] );
	}
	public String serialize()
	{
		String serializedStatistics = "";
		
		serializedStatistics += serializeAbilityLevels() + "$";
		serializedStatistics += serializeCash() + "$";
		serializedStatistics += serializeDiamonds() + "$";
		
		return serializedStatistics;
	}
	
	//Statistics members serialization
	private String serializeAbilityLevels()
	{
		String serializedAbilityLevels = "";
		
		Iterator<CharacterAbilityType> mapIter = abilityLevels.keySet().iterator();
		
		while( mapIter.hasNext() )
		{
			CharacterAbilityType key = mapIter.next();
			int value = abilityLevels.get(key);
			
			serializedAbilityLevels += key.toString() + "," + String.valueOf( value ) + ";";
		}
		
		return serializedAbilityLevels;
	}
	private void unserializeHashMap( String serializedMap )
	{
		Array<String> records = new Array<String>( serializedMap.split(";") );
		
		for(String s: records)
		{		
			abilityLevels.put( CharacterAbilityType.parseFromString( s.split(",")[0] ), Integer.parseInt( s.split(",")[1] ) );
		}
	}
	private String serializeCash()
	{
		return String.valueOf(cash);
	}
	private void unserializeCash( String serializedCash )
	{
		this.cash = Integer.parseInt(serializedCash);
	}
	private String serializeDiamonds()
	{
		return String.valueOf(diamonds);
	}
	private void unserializeDiamonds( String serializedDiamonds )
	{
		this.diamonds = Integer.parseInt(serializedDiamonds);
	}
}
