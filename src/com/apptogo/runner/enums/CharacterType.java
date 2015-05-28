package com.apptogo.runner.enums;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.actors.Animation;
import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.player.Player;
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
	
	static public Character convertToCharacter(CharacterType characterType, World world, GameWorld gameWorld, int startingPosition, Player player)
	{
		if( characterType == BANDIT )
		{
			return new Bandit(world, gameWorld, startingPosition, player.getName(), player.abilities);
		}
		else if( characterType == ARCHER )
		{
			return new Archer(world, gameWorld, startingPosition, player.getName(), player.abilities);
		}
		else if( characterType == ALIEN )
		{
			return new Alien(world, gameWorld, startingPosition, player.getName(), player.abilities);
		}
		
		return null;
	}
	
	static public String[] convertToTextureAtlases(CharacterType characterType)
	{
		
		if( characterType == BANDIT )
		{
			return new String[]{"gfx/game/characters/characters.pack", "gfx/game/characters/bomb.pack"};
		}
		else if( characterType == ARCHER )
		{
			return new String[]{"gfx/game/characters/characters.pack"};
		}
		else if( characterType == ALIEN )
		{
			return new String[]{"gfx/game/characters/characters.pack"};
		}
		
		return null;
	}
	
	static public String convertToLoadingScreenBackground(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return "loadingWildWest";
		}
		else if( characterType == ARCHER )
		{
			return "loadingForest";
		}
		else if( characterType == ALIEN )
		{
			return "loadingSpace";
		}
		
		return null;
	}
	
	static public Animation convertToCharacterAnimation(CharacterType characterType, boolean running)
	{
		if( characterType == BANDIT )
		{
			if(running)
			{
				return new Animation("bandit_run", 18, 0.03f, CharacterAnimationState.IDLE, true, true);
			}
			else
			{
				return new Animation("bandit_idle", 22, 0.06f, CharacterAnimationState.IDLE, true, true);
			}
		}
		else if( characterType == ARCHER )
		{
			if(running)
			{
				return new Animation("archer_run", 18, 0.03f, CharacterAnimationState.IDLE, true, true);
			}
			else
			{
				return new Animation("archer_idle", 21, 0.06f, CharacterAnimationState.IDLE, true, true);
			}
		}
		else if( characterType == ALIEN )
		{
			if(running)
			{
				return new Animation("alien_run", 18, 0.03f, CharacterAnimationState.IDLE, true, true);
			}
			else
			{
				return new Animation("alien_idle", 21, 0.06f, CharacterAnimationState.IDLE, true, true);
			}
		}

		return null;
	}
	
	static public String convertToGroundPath(CharacterType characterType)
	{
		if( characterType == BANDIT )
		{
			return "banditGround";
		}
		else if( characterType == ARCHER )
		{
			return "archerGround";
		}
		else if( characterType == ALIEN )
		{
			return "alienGround";
		}

		return null;
	}
}