package com.apptogo.runner.enums;

public enum CharacterAbilityType
{
	SUPERSPEED,
	BOMB, ARROW, LIFT, SNARES,
	JUMP, SLIDE,
	ABILITY1;
	
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
}