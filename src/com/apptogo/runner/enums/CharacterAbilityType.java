package com.apptogo.runner.enums;

import com.apptogo.runner.actors.CharacterButton;
import com.badlogic.gdx.utils.Array;

public enum CharacterAbilityType
{
	SUPERSPEED,
	BOMB, ARROW, LIFT, SNARES, BLACKHOLE,
	JUMP, SLIDE,
	SUPER_ABILITY_1;
	
	public static Array<CharacterAbilityType> values;
	
	static
	{
		values = new Array<CharacterAbilityType>( CharacterAbilityType.values() );
	}
	
	static public CharacterAbilityType parseFromString(String key)
	{
		if( key.equals( CharacterAbilityType.BOMB.toString() ) )
		{
			return CharacterAbilityType.BOMB;
		}
		else if( key.equals( CharacterAbilityType.ARROW.toString() ) )
		{
			return CharacterAbilityType.ARROW;
		}
		else if( key.equals( CharacterAbilityType.LIFT.toString() ) )
		{
			return CharacterAbilityType.LIFT;
		}
		//else if() kolejne abilities
		else return null;
	}
	
	public static CharacterAbilityType getRandom() 
	{
		double random = Math.random();
		
		random = ( random >= 1.0 )?0.9:random;
		
		int index = (int) Math.floor(random * (values.size));
		
		return values.get( index );
	}
	
	static public CharacterButton convertToPowerupButton(CharacterAbilityType abilityType, CharacterType characterType)
	{
		CharacterButton button = null;

		if(abilityType == CharacterAbilityType.SUPERSPEED)
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
		else if(abilityType == CharacterAbilityType.SUPER_ABILITY_1)
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
		else
		{
			button = null;
		}
		return button;
	}
}