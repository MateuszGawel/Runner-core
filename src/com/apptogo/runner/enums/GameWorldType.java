package com.apptogo.runner.enums;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

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
			return new String[]{ "gfx/game/levels/mountains.png","gfx/game/levels/rocks.png","gfx/game/levels/skyBlue.png","gfx/game/levels/sand.png", "gfx/game/levels/barrelSmall.png", "gfx/game/levels/barrelBig.png" };
		}
		else if( gameWorldType == FOREST )
		{
			return new String[]{ "gfx/game/levels/barrelSmall.png", "gfx/game/levels/barrelBig.png", "gfx/game/levels/tree1.png", "gfx/game/levels/tree2.png", "gfx/game/levels/tree3.png", "gfx/game/levels/tree4.png" };
		}
		else if( gameWorldType == SPACE )
		{
			return new String[]{  };
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
			//return new WildWestWorld( level.mapPath, player );
		}
		
		return null;
	}
	
	public static Image convertToWorldBackground( GameWorldType gameWorldType, ScreenType screenType )
	{
		if( gameWorldType == WILDWEST )
		{
			Texture texture = ResourcesManager.getInstance().getResource(screenType, "ui/menuBackgrounds/campaignScreenBackgroundWildWest.png");
			return new Image( texture );
		}
		else if( gameWorldType == FOREST )
		{
			Texture texture = ResourcesManager.getInstance().getResource(screenType, "ui/menuBackgrounds/campaignScreenBackgroundForrest.png");
			return new Image( texture );
		}
		else if( gameWorldType == SPACE )
		{
			Texture texture = ResourcesManager.getInstance().getResource(screenType, "ui/menuBackgrounds/campaignScreenBackgroundSpace.png");
			return new Image( texture );
		}
		
		return null;
	}
}