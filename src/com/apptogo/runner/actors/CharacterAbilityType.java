package com.apptogo.runner.actors;

public enum CharacterAbilityType
{
	BOMB, ARROW;
	
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
		//else if() kolejne abilities
		else return null;
	}
}