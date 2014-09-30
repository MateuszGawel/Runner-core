package com.apptogo.runner.enums;

public enum PowerupType
{
	SUPERJUMP, SUPERSPEED;
	
	static public PowerupType parseFromString(String key)
	{
		if( key.equals( PowerupType.SUPERJUMP.toString() ) )
		{
			return PowerupType.SUPERJUMP;
		}
		else if( key.equals( PowerupType.SUPERSPEED.toString() ) )
		{
			return PowerupType.SUPERSPEED;
		}
		//else if() kolejne powerupy
		else return null;
	}
	
	static public String convertToPowerupButtonImagePath(PowerupType powerupType, CharacterType characterType)
	{
		if(powerupType == PowerupType.SUPERJUMP)
		{
			if(characterType == CharacterType.BANDIT)
			{
				return "gfx/game/characters/bandit/buttons/superJumpButton.png";
			}
			else if(characterType == CharacterType.ARCHER)
			{
				return "gfx/game/characters/archer/buttons/superJumpButton.png";
			}
			else if(characterType == CharacterType.ALIEN)
			{
				return "gfx/game/characters/alien/buttons/superJumpButton.png";
			}
		}
		else if(powerupType == PowerupType.SUPERSPEED)
		{
			if(characterType == CharacterType.BANDIT)
			{
				return "gfx/game/characters/bandit/buttons/superSpeedButton.png";
			}
			else if(characterType == CharacterType.ARCHER)
			{
				return "gfx/game/characters/archer/buttons/superSpeedButton.png";
			}
			else if(characterType == CharacterType.ALIEN)
			{
				return "gfx/game/characters/alien/buttons/superSpeedButton.png";
			}
		}
		return null;
	}
}