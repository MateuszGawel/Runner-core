package com.apptogo.runner.enums;

import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.SpaceWorld;
import com.apptogo.runner.world.WildWestWorld;

public enum GameWorldType
{
	WILDWEST,
	FOREST,
	SPACE;
	
	static public GameWorldType parseFromString(String key)
	{
		if( key.toUpperCase().equals( GameWorldType.WILDWEST.toString().toUpperCase() ) )
		{
			return GameWorldType.WILDWEST;
		}
		else if( key.toUpperCase().equals( GameWorldType.FOREST.toString().toUpperCase() ) )
		{
			return GameWorldType.FOREST;
		}
		else if( key.toUpperCase().equals( GameWorldType.SPACE.toString().toUpperCase() ) )
		{
			return GameWorldType.SPACE;
		}

		return null;
	}
		
	static public String[] convertToMusics(GameWorldType gameWorldType)
	{
		
		if( gameWorldType == WILDWEST )
		{
			return new String[]{"mfx/game/levels/wildWestMusic.ogg"};
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{"mfx/game/levels/forestMusic.ogg"};
		}
		else if( gameWorldType == SPACE )
		{
			return new String[]{"mfx/game/levels/spaceMusic.ogg"};
		}
		
		return null;
	}
	
	static public String[] convertToSounds(GameWorldType gameWorldType)
	{
		
		if( gameWorldType == WILDWEST )
		{
			return new String[]{"mfx/game/levels/bonfire.ogg", "mfx/game/levels/bush.ogg", "mfx/game/levels/barrel.ogg", "mfx/game/levels/powerup.ogg", "mfx/game/levels/shot.ogg", "mfx/game/levels/shotClicks.ogg"};
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{"mfx/game/levels/swamp.ogg", "mfx/game/levels/powerup.ogg", "mfx/game/levels/jaws.ogg", "mfx/game/levels/roar.ogg"};
		}
		else if( gameWorldType == SPACE )
		{
			return new String[]{"mfx/game/levels/powerup.ogg"};
		}
		
		return null;
	}
	
	public static String convertToButtonStyleName( GameWorldType gameWorldType)
	{
		if( gameWorldType == WILDWEST )
		{
			return "westWildCampaignButton";
		}
		else if( gameWorldType == FOREST )
		{
			return "forestCampaignButton";
		}
		else if( gameWorldType == SPACE )
		{
			return "spaceCampaignButton";
		}
		
		return null;
	}
	
	public static String convertToButtonLockedStyleName( GameWorldType gameWorldType)
	{
		if( gameWorldType == WILDWEST )
		{
			return "westWildCampaignLockedButton";
		}
		else if( gameWorldType == FOREST )
		{
			return "forestCampaignLockedButton";
		}
		else if( gameWorldType == SPACE )
		{
			return "spaceCampaignLockedButton";
		}
		
		return null;
	}
	
	public static GameWorld convertToGameWorld(String mapPath, GameWorldType gameWorldType, Player player)
	{
		if( gameWorldType == WILDWEST )
		{
			return new WildWestWorld(mapPath, player );
		}
		else if( gameWorldType == FOREST )
		{
			return new ForestWorld(mapPath, player );
		}
		else if( gameWorldType == SPACE )
		{
			return new SpaceWorld(mapPath, player );
		}
		
		return null;
	}
	
	public static String convertToWorldBackgroundRegionName( GameWorldType gameWorldType )
	{
		if( gameWorldType == WILDWEST )
		{
			return "campaignScreenBackgroundWildWest";
		}
		else if( gameWorldType == FOREST )
		{
			return "campaignScreenBackgroundForrest";
		}
		else if( gameWorldType == SPACE )
		{
			return "campaignScreenBackgroundSpace";
		}
		
		return null;
	}
	
	public static CharacterType convertToCharacterType( GameWorldType gameWorldType )
	{
		if( gameWorldType == WILDWEST )
		{
			return CharacterType.BANDIT;
		}
		else if( gameWorldType == FOREST )
		{
			return CharacterType.ARCHER;
		}
		else if( gameWorldType == SPACE )
		{
			return CharacterType.ALIEN;
		}
		
		return null;
	}
		
	public static String convertToLoaderButtonName( GameWorldType gameWorldType )
	{
		if( gameWorldType == WILDWEST )
		{
			return "westWildLoader";
		}
		else if( gameWorldType == FOREST )
		{
			return "forrestLoader";
		}
		else if( gameWorldType == SPACE )
		{
			return "spaceLoader";
		}
		
		return null;
	}

	public static String convertToAtlasPath(GameWorldType gameWorldType) 
	{
		if( gameWorldType == WILDWEST )
		{
			return "gfx/game/levels/wildWestAtlas.pack";
		}
		else if( gameWorldType == FOREST )
		{
			return "gfx/game/levels/forestAtlas.pack";
		}
		else if( gameWorldType == SPACE )
		{
			return "gfx/game/levels/spaceAtlas.pack";
		}
		
		return null;
	}
}
