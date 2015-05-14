package com.apptogo.runner.enums;

import com.apptogo.runner.actors.CharacterButton;
import com.badlogic.gdx.utils.Array;

public enum CharacterAbilityType
{
	SUPERSPEED,
	BOMB, ARROW, LIFT, SNARES, BLACKHOLE;
	
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
	
	//uwaga nie ma znaczenia co zwroci - w character sobie zadba o to zeby przerzutowac umiejetnosc
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

		if(characterType == CharacterType.BANDIT)
		{
			if(abilityType == CharacterAbilityType.SUPERSPEED) button = new CharacterButton("banditSuperSpeedButton", 20, 200);
			if(abilityType == CharacterAbilityType.BOMB      ) button = new CharacterButton("banditBombAbilityButton", 20, 200);
		}
		else if(characterType == CharacterType.ARCHER) //podmienic snares
		{
			if(abilityType == CharacterAbilityType.SUPERSPEED) button = new CharacterButton("archerSuperSpeedButton", 20, 200);
			if(abilityType == CharacterAbilityType.ARROW     ) button = new CharacterButton("archerArrowAbilityButton", 20, 200);
			if(abilityType == CharacterAbilityType.SNARES    ) button = new CharacterButton("archerArrowAbilityButton", 20, 200);
		}
		else if(characterType == CharacterType.ALIEN) //podmienic blackhole
		{
			if(abilityType == CharacterAbilityType.SUPERSPEED) button = new CharacterButton("alienSuperSpeedButton", 20, 200);
			if(abilityType == CharacterAbilityType.LIFT      ) button = new CharacterButton("alienLiftAbilityButton", 20, 200);
			if(abilityType == CharacterAbilityType.BLACKHOLE ) button = new CharacterButton("alienLiftAbilityButton", 20, 200);
		}
		
		return button;
	}
}