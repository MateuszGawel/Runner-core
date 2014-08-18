package com.apptogo.runner.actors;

import com.badlogic.gdx.physics.box2d.World;

/** przy dodawaniu kolejnego typu pamietac o obsluzeniu go w funkcji createCharacter klasy gameWorld
 *  oraz w tablicy z selectCharacter w MainMenuScreen                                              */
public enum CharacterType
{
	BANDIT, ARCHER;
	
	static public CharacterType parseFromString(String key)
	{
		if( key.equals( CharacterType.BANDIT.toString() ) )
		{
			return CharacterType.BANDIT;
		}
		else if( key.equals( CharacterType.ARCHER.toString() ) )
		{
			return CharacterType.ARCHER;
		}
		//else if() kolejni bohaterowie
		else return null;
	}
	
	static public Character convertToCharacter(CharacterType characterType, World world)
	{
		if( characterType == BANDIT )
		{
			return new Bandit(world);
		}
		else if( characterType == ARCHER )
		{
			return new Archer(world);
		}
		
		return null;
	}
	
	static public String[] convertToTextureAtlases(CharacterType characterType)
	{
		String[] textureAtlases;
		
		if( characterType == BANDIT )
		{
			return new String[]{"gfx/game/characters/bandit.pack"};
		}
		else if( characterType == ARCHER )
		{
			return new String[]{"gfx/game/characters/archer.pack"};
		}
		
		return null;
	}
	
	static public String convertToLoadingScreenBackground(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return "ui/menuBackgrounds/loadingScreenBackgroundWildWest.png";
		}
		else if( characterType == ARCHER )
		{
			return "ui/menuBackgrounds/loadingScreenBackgroundForrest.png";
		}
		
		return null;
	}
}