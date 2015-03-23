package com.apptogo.runner.enums;


public enum ScreenType
{
	SCREEN_SPLASH,
	SCREEN_LOADING, 
	SCREEN_LOADING_GAME, 
	SCREEN_MAIN_MENU,
	SCREEN_CAMPAIGN,
	SCREEN_MULTIPLAYER,
	SCREEN_GAME_SINGLE,
	SCREEN_GAME_MULTI,
	SCREEN_WAITING_ROOM,
	SCREEN_REGISTER,
	SCREEN_SHOP,
	SCREEN_NONE;
	
	static public ScreenClass convertToScreenClass(ScreenType screenType)
	{
		if( screenType == SCREEN_SPLASH )
		{
			return ScreenClass.SPLASH;
		}
		else if( screenType == SCREEN_LOADING                 ||
				 screenType == ScreenType.SCREEN_LOADING_GAME 
			   )
		{
			return ScreenClass.STILL;
		}
		else if( screenType == ScreenType.SCREEN_MAIN_MENU    ||
				 screenType == ScreenType.SCREEN_CAMPAIGN     ||
				 screenType == ScreenType.SCREEN_MULTIPLAYER  ||
				 screenType == ScreenType.SCREEN_WAITING_ROOM ||
				 screenType == ScreenType.SCREEN_REGISTER     ||
				 screenType == ScreenType.SCREEN_SHOP
			   )
		{
			return ScreenClass.MENU;
		}
		else if( screenType == ScreenType.SCREEN_GAME_MULTI  ||
				 screenType == ScreenType.SCREEN_GAME_SINGLE
			   )
		{
			return ScreenClass.GAME;
		}
		
		return null;
	}
}