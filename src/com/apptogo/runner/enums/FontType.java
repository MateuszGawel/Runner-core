package com.apptogo.runner.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public enum FontType
{
	
	DEFAULT,
	WOODFONT,
	LEAFFONT,
	ROCKFONT;
	
	public static BitmapFont convertToFont(FontType fontType)
	{
		if( fontType == FontType.DEFAULT )
		{
			return getFont("gfx/fonts/ComicSerif.ttf", 60);
		}
		else if( fontType == FontType.WOODFONT )
		{
			return getFont("gfx/fonts/ComicSerif.ttf", 48);
		}
		else if( fontType == FontType.LEAFFONT )
		{
			return getFont("gfx/fonts/ComicSerif.ttf", 48);
		}
		else if( fontType == FontType.ROCKFONT )
		{
			return getFont("gfx/fonts/ComicSerif.ttf", 48);
		}
		else 
			return null;
	}
	
	public static Color convertToColor(FontType fontType)
	{
		if( fontType == FontType.DEFAULT )
		{
			return new Color(1, 1, 1, 1);
		}
		else if( fontType == FontType.WOODFONT )
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
		else 
			return null;
	}
			
	private static BitmapFont getFont(String fontPath, int fontSize)
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal( fontPath ) );
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		
		parameter.size = fontSize;

		BitmapFont font = generator.generateFont(parameter);

		generator.dispose();
		
		return font;	
	}
}