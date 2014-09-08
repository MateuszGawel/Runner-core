package com.apptogo.runner.enums;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.World;

/** przy dodawaniu kolejnego typu pamietac o obsluzeniu go w funkcji createCharacter klasy gameWorld
 *  oraz w tablicy z selectCharacter w MainMenuScreen                                              */
public enum CharacterType
{
	BANDIT, ARCHER, ALIEN;
	
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
		else if( key.equals( CharacterType.ALIEN.toString() ) )
		{
			return CharacterType.ALIEN;
		}
		else return null;
	}
	
	public static String[] convertToTexturesList(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return null;
		}
		else if( characterType == ARCHER )
		{
			//to trzeba zapakowaæ w atlas
			return new String[]{ "gfx/game/characters/arrow.png", "gfx/game/characters/archerHead.png", "gfx/game/characters/archerTorso.png", "gfx/game/characters/archerArm.png", "gfx/game/characters/archerHand.png", "gfx/game/characters/archerLeg.png", "gfx/game/characters/archerFoot.png", "gfx/game/characters/archerArrows.png", "gfx/game/characters/archerBow.png" };
		}
		else if( characterType == ALIEN )
		{
			return null;
		}
		
		return null;
	}
	
	static public Character convertToCharacter(CharacterType characterType, World world, GameWorld gameWorld)
	{
		if( characterType == BANDIT )
		{
			return new Bandit(world);
		}
		else if( characterType == ARCHER )
		{
			return new Archer(world, gameWorld);
		}
		else if( characterType == ALIEN )
		{
			return new Alien(world);
		}
		
		return null;
	}
	
	static public String[] convertToTextureAtlases(CharacterType characterType)
	{
		String[] textureAtlases;
		
		if( characterType == BANDIT )
		{
			return new String[]{"gfx/game/characters/bandit.pack", "gfx/game/characters/bomb.pack"};
		}
		else if( characterType == ARCHER )
		{
			return new String[]{"gfx/game/characters/archer.pack"};
		}
		else if( characterType == ALIEN )
		{
			return new String[]{"gfx/game/characters/alien.pack"};
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
		else if( characterType == ALIEN )
		{
			return "ui/menuBackgrounds/loadingScreenBackgroundSpace.png";
		}
		
		return null;
	}
	
	static public String convertToCharacterHead(String selectedCharacterString)
	{
		if( selectedCharacterString.equals( CharacterType.BANDIT.toString() ) )
		{
			return "ui/menuBackgrounds/banditHead.png";
		}
		else if( selectedCharacterString.equals( CharacterType.ARCHER.toString() ) )
		{
			return "ui/menuBackgrounds/archerHead.png";
		}
		else if( selectedCharacterString.equals( CharacterType.ALIEN.toString() ) )
		{
			return "ui/menuBackgrounds/alienHead.png";
		}
		else return null;
	}
	
}