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
			return defaultFont;
		}
		else if( fontType == FontType.WOODFONT )
		{
			return woodFont;
		}
		else if( fontType == FontType.LEAFFONT )
		{
			return leafFont;
		}
		else if( fontType == FontType.ROCKFONT )
		{
			return rockFont;
		}
		else 
			return null;
	}
	
	public static Color convertToColor(FontType fontType)
	{
		if( fontType == FontType.DEFAULT )
		{
			return defaultFontColor;
		}
		else if( fontType == FontType.WOODFONT )
		{
			return woodFontColor;
		}
		else if( fontType == FontType.LEAFFONT )
		{
			return leafFontColor;
		}
		else if( fontType == FontType.ROCKFONT )
		{
			return rockFontColor;
		}
		else 
			return null;
	}
	
	//sprawdzic dokladnie pod katem wydajnosci!
	
	public static BitmapFont defaultFont;
	public static BitmapFont woodFont;
	public static BitmapFont leafFont;
	public static BitmapFont rockFont;
	
	public static Color defaultFontColor;
	public static Color woodFontColor;
	public static Color leafFontColor;
	public static Color rockFontColor;
	
	
	static
	{
		defaultFont = getFont("gfx/fonts/ComicSerif.ttf", 60);
		defaultFontColor = new Color(1, 1, 1, 1);
		
		woodFont = getFont("gfx/fonts/ComicSerif.ttf", 48);
		woodFontColor = new Color(151f/255f, 127f/255f, 91f/255f, 1);
		
		leafFont = getFont("gfx/fonts/ComicSerif.ttf", 48);
		leafFontColor = new Color(0.05f, 0.35f, 0.04f, 1);
		
		rockFont = getFont("gfx/fonts/ComicSerif.ttf", 48);
		rockFontColor = new Color(0.31f, 0.22f, 0.16f, 1);
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
	
	public static void dispose()
	{
		defaultFont.dispose();
		woodFont.dispose();
		leafFont.dispose();
		rockFont.dispose();
	}
}
