package com.apptogo.runner.enums;

import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public enum EmoticonType
{
	THINKING, 
	LAUGH,
	BOSS, 
	BORED, 
	ANGRY;
		
	static public Image convertToImage(EmoticonType emoticonType)
	{
		if(emoticonType == EmoticonType.THINKING)
		{
			return new Image( ResourcesManager.getInstance().getAtlasRegion("e2") );
		}
		else if(emoticonType == EmoticonType.LAUGH)
		{
			return new Image( ResourcesManager.getInstance().getAtlasRegion("e3") );
		}
		else if(emoticonType == EmoticonType.BOSS)
		{
			return new Image( ResourcesManager.getInstance().getAtlasRegion("e5") );
		}
		else if(emoticonType == EmoticonType.BORED)
		{
			return new Image( ResourcesManager.getInstance().getAtlasRegion("e1") );
		}
		else if(emoticonType == EmoticonType.ANGRY)
		{
			return new Image( ResourcesManager.getInstance().getAtlasRegion("e4") );
		}
		else return null;
	}
}