package com.apptogo.runner.enums;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.animation.CharacterAnimation;
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
			return new String[]{"gfx/game/characters/banditHead.png", "gfx/game/characters/banditTorso.png", "gfx/game/characters/banditArm.png", "gfx/game/characters/banditHand.png", "gfx/game/characters/banditLeg.png", "gfx/game/characters/banditFoot.png", "gfx/game/characters/banditBag.png", "gfx/game/characters/banditGround.png" };
		}
		else if( characterType == ARCHER )
		{
			//to trzeba zapakowaæ w atlas
			return new String[]{ "gfx/game/characters/arrow.png", "gfx/game/characters/archerHead.png", "gfx/game/characters/archerTorso.png", "gfx/game/characters/archerArm.png", "gfx/game/characters/archerHand.png", "gfx/game/characters/archerLeg.png", "gfx/game/characters/archerFoot.png", "gfx/game/characters/archerArrows.png", "gfx/game/characters/archerBow.png", "gfx/game/characters/archerGround.png" };
		}
		else if( characterType == ALIEN )
		{
			return new String[]{ "gfx/game/characters/alienGround.png" };
		}
		
		return null;
	}
	
	public static String[] convertToSoundsList(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return new String[]{"mfx/game/characters/banditJump.ogg", "mfx/game/characters/banditDeath.ogg", "mfx/game/characters/banditExplode.ogg", "mfx/game/characters/banditVictory.ogg"};
		}
		else if( characterType == ARCHER )
		{
			return new String[]{"mfx/game/characters/archerJump.ogg", "mfx/game/characters/archerDeath.ogg", "mfx/game/characters/archerExplode.ogg", "mfx/game/characters/archerVictory.ogg"};
		}
		else if( characterType == ALIEN )
		{
			return new String[]{};
		}
		
		return null;
	}
	
	static public Character convertToCharacter(CharacterType characterType, World world, GameWorld gameWorld)
	{
		if( characterType == BANDIT )
		{
			return new Bandit(world, gameWorld);
		}
		else if( characterType == ARCHER )
		{
			return new Archer(world, gameWorld);
		}
		else if( characterType == ALIEN )
		{
			return new Alien(world, gameWorld);
		}
		
		return null;
	}
	
	static public String[] convertToTextureAtlases(CharacterType characterType)
	{
		
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
			return "gfx/menu/menuBackgrounds/loadingScreenBackgroundWildWest.png";
		}
		else if( characterType == ARCHER )
		{
			return "gfx/menu/menuBackgrounds/loadingScreenBackgroundForrest.png";
		}
		else if( characterType == ALIEN )
		{
			return "gfx/menu/menuBackgrounds/loadingScreenBackgroundSpace.png";
		}
		
		return null;
	}
	
	static public CharacterAnimation convertToCharacterAnimation(CharacterType characterType, float x, float y, boolean running)
	{
		if( characterType == BANDIT )
		{
			return new CharacterAnimation("gfx/game/characters/bandit.pack", x, y, running);
		}
		else if( characterType == ARCHER )
		{
			return new CharacterAnimation("gfx/game/characters/archer.pack", x, y, running);
		}
		else if( characterType == ALIEN )
		{
			return new CharacterAnimation("gfx/game/characters/alien.pack", x, y, running);
		}

		return null;
	}
	
	static public String convertToGroundPath(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return "gfx/game/characters/banditGround.png";
		}
		else if( characterType == ARCHER )
		{
			return "gfx/game/characters/archerGround.png";
		}
		else if( characterType == ALIEN )
		{
			return "gfx/game/characters/alienGround.png";
		}

		return null;
	}
}