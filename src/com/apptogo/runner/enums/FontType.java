package com.apptogo.runner.enums;

import com.apptogo.runner.handlers.FontManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum FontType
{	
	BIG,
	MEDIUM,
	SMALL,
	LOADINGBIG,
	LOADINGSMALL,
	BLACKBOARDSMALL,
	BLACKBOARDMEDIUM,
	WOODFONT,
	WOODFONTSMALL,
	LEAFFONT,
	ROCKFONT,
	GAMEWORLDFONT;
	
	public static BitmapFont convertToFont(FontType fontType)
	{
		if( fontType == FontType.BIG )
		{
			return FontManager.getInstance().bigFont;
		}
		else if( fontType == FontType.MEDIUM )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.SMALL )
		{
			return FontManager.getInstance().smallFont;
		}
		else if( fontType == FontType.LOADINGBIG )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.LOADINGSMALL )
		{
			return FontManager.getInstance().smallFont;
		}
		else if( fontType == FontType.BLACKBOARDSMALL )
		{
			return FontManager.getInstance().smallFont;
		}
		else if( fontType == FontType.BLACKBOARDMEDIUM )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.WOODFONT )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.WOODFONTSMALL )
		{
			return FontManager.getInstance().smallFont;
		}
		else if( fontType == FontType.LEAFFONT )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.ROCKFONT )
		{
			return FontManager.getInstance().mediumFont;
		}
		else if( fontType == FontType.GAMEWORLDFONT )
		{
			return FontManager.getInstance().gameWorldFont;
		}
		else 
			return null;
	}
	
	public static Color convertToColor(FontType fontType)
	{
		if( fontType == FontType.BIG )
		{
			return new Color(1, 1, 1, 1);
		}
		else if( fontType == FontType.MEDIUM )
		{
			return new Color(1, 1, 1, 1);
		}
		else if( fontType == FontType.SMALL )
		{
			return new Color(1, 1, 1, 1);
		}
		else if( fontType == FontType.LOADINGBIG )
		{
			return new Color(1, 1, 1, 1);
		}
		else if( fontType == FontType.LOADINGSMALL )
		{
			return new Color(0.9f, 0.9f, 0.9f, 1);
		}
		else if( fontType == FontType.BLACKBOARDSMALL )
		{
			return new Color(0.87f, 0.78f, 0.59f, 1);
		}
		else if( fontType == FontType.BLACKBOARDMEDIUM )
		{
			return new Color(0.97f, 0.88f, 0.69f, 1);
		}
		else if( fontType == FontType.WOODFONT )
		{
			return new Color(151f/255f, 127f/255f, 91f/255f, 1);
		}
		else if( fontType == FontType.WOODFONTSMALL )
		{
			return new Color(151f/255f, 127f/255f, 91f/255f, 1);
		}
		else if( fontType == FontType.LEAFFONT )
		{
			return new Color(0.05f, 0.35f, 0.04f, 1);
		}
		else if( fontType == FontType.ROCKFONT )
		{
			return new Color(0.31f, 0.22f, 0.16f, 1);
		}
		else if( fontType == FontType.GAMEWORLDFONT )
		{
			return new Color(0.31f, 0.22f, 0.16f, 1);
		}
		else 
			return null;
	}
}