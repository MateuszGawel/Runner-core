package com.apptogo.runner.enums;

import com.apptogo.runner.actors.CharacterButton;
import com.badlogic.gdx.utils.Array;

public enum PowerupType
{
	SUPERSPEED, ABILITY1;
	
	public static Array<PowerupType> values;
	
	static
	{
		values = new Array<PowerupType>( PowerupType.values() );
	}
	
	static public PowerupType parseFromString(String key)
	{
		if( key.equals( PowerupType.SUPERSPEED.toString() ) )
		{
			return PowerupType.SUPERSPEED;
		}
		else if( key.equals( PowerupType.ABILITY1.toString() ) )
		{
			return PowerupType.ABILITY1;
		}
		//else if() kolejne powerupy
		else return null;
	}
	
	static public CharacterButton convertToPowerupButton(PowerupType powerupType, CharacterType characterType)
	{
		CharacterButton button = null;

		if(powerupType == PowerupType.SUPERSPEED)
		{
			if(characterType == CharacterType.BANDIT)
			{
				button = new CharacterButton("banditSuperSpeedButton", 20, 200);
			}
			else if(characterType == CharacterType.ARCHER)
			{
				button = new CharacterButton("archerSuperSpeedButton", 20, 200);
			}
			else if(characterType == CharacterType.ALIEN)
			{
				button = new CharacterButton("alienSuperSpeedButton", 20, 200);
			}
		}
		else if(powerupType == PowerupType.ABILITY1)
		{
			if(characterType == CharacterType.BANDIT)
			{
				button = new CharacterButton("banditBombAbilityButton", 20, 200);
			}
			else if(characterType == CharacterType.ARCHER)
			{
				button = new CharacterButton("archerArrowAbilityButton", 20, 200);
			}
			else if(characterType == CharacterType.ALIEN)
			{
				button = new CharacterButton("alienLiftAbilityButton", 20, 200);
			}
		}
		return button;
	}
	
	static public CharacterAbilityType convertToAbilityType(PowerupType powerupType, CharacterType characterType)
	{

		if(powerupType == PowerupType.SUPERSPEED)
		{
			return CharacterAbilityType.SUPERSPEED;
			
		}
		else if(powerupType == PowerupType.ABILITY1)
		{
			if(characterType == CharacterType.BANDIT)
			{
				return CharacterAbilityType.BOMB;
			}
			else if(characterType == CharacterType.ARCHER)
			{
				return CharacterAbilityType.ARROW;
			}
			else if(characterType == CharacterType.ALIEN)
			{
				return CharacterAbilityType.LIFT;
			}
		}
		return null;
	}

	public static PowerupType getRandom() 
	{
		double random = Math.random();
		
		random = ( random >= 1.0 )?0.9:random;
		
		int index = (int) Math.floor(random * (values.size));
		
		return values.get( index );
	}
}