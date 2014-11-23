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

	public static String[] convertToTexturesList(GameWorldType gameWorldType)
	{
		if( gameWorldType == WILDWEST )
		{
			return new String[]{ "gfx/game/levels/mountains.png","gfx/game/levels/rocks.png","gfx/game/levels/skyBlue.png","gfx/game/levels/sand.png", "gfx/game/levels/barrelSmall.png", "gfx/game/levels/barrelBig.png", "gfx/game/levels/bush.png" };
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{ "gfx/game/levels/tree1.png", "gfx/game/levels/tree2.png", "gfx/game/levels/tree3.png", "gfx/game/levels/tree4.png" };
		}
		else if( gameWorldType == SPACE )
		{
			return new String[]{ "gfx/game/levels/space.jpg", "gfx/game/levels/planet1.png" , "gfx/game/levels/planet2.png" , "gfx/game/levels/planet3.png", "gfx/game/levels/asteroid1.png", "gfx/game/levels/asteroid2.png", "gfx/game/levels/asteroid3.png", "gfx/game/levels/asteroid4.png", "gfx/game/levels/asteroid5.png", "gfx/game/levels/rockBig.png", "gfx/game/levels/rockSmall.png" };
		}
		
		return null;
	}
	
	static public String[] convertToTextureAtlases(GameWorldType gameWorldType)
	{
		
		if( gameWorldType == WILDWEST )
		{
			return new String[]{ "gfx/game/levels/bonfire.pack"};
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{ "gfx/game/levels/mushroom.pack", "gfx/game/levels/catapult.pack", "gfx/game/levels/leaf.pack", "gfx/game/levels/hedgehog.pack", "gfx/game/levels/swamp.pack"};
		}
		else if( gameWorldType == SPACE )
		{
			return new String[]{ "gfx/game/levels/asteroid.pack"};
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
			return new String[]{"mfx/game/levels/bonfire.ogg", "mfx/game/levels/bush.ogg", "mfx/game/levels/barrel.ogg", "mfx/game/levels/powerup.ogg"};
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{"mfx/game/levels/swamp.ogg", "mfx/game/levels/powerup.ogg"};
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
	
	public static GameWorld convertToGameWorld( GameWorldType gameWorldType, String mapPath, Player player)
	{
		if( gameWorldType == WILDWEST )
		{
			return new WildWestWorld( mapPath, player );
		}
		else if( gameWorldType == FOREST )
		{
			return new ForestWorld( mapPath, player );
		}
		else if( gameWorldType == SPACE )
		{
			return new SpaceWorld( mapPath, player );
		}
		
		return null;
	}
	
	public static String convertToWorldBackgroundPath( GameWorldType gameWorldType, ScreenType screenType )
	{
		if( gameWorldType == WILDWEST )
		{
			return "gfx/menu/menuBackgrounds/campaignScreenBackgroundWildWest.png";
		}
		else if( gameWorldType == FOREST )
		{
			return "gfx/menu/menuBackgrounds/campaignScreenBackgroundForrest.png";
		}
		else if( gameWorldType == SPACE )
		{
			return "gfx/menu/menuBackgrounds/campaignScreenBackgroundSpace.png";
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
	
	public static FontType convertToButtonFontType( GameWorldType gameWorldType)
	{
		if( gameWorldType == WILDWEST )
		{
			return FontType.WOODFONT;
		}
		else if( gameWorldType == FOREST )
		{
			return FontType.LEAFFONT;
		}
		else if( gameWorldType == SPACE )
		{
			return FontType.ROCKFONT;
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
}
