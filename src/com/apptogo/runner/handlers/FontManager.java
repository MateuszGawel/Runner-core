package com.apptogo.runner.handlers;

import com.apptogo.runner.handlers.BitmapFontWriter.FontInfo;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.logger.Logger.LogLevel;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.Page;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.apptogo.runner.handlers.BitmapFontWriter.OutputFormat;

public class FontManager 
{
	private static FontManager INSTANCE;
	
	public static void create()
	{		
		INSTANCE = new FontManager();
		
		Logger.log(INSTANCE, "Manager has been created", LogLevel.LOW);
	}
	public static void destroy()
	{
		Logger.log(INSTANCE, "Manager has been destroyed", LogLevel.LOW);
		
		INSTANCE = null;
	}
	public static FontManager getInstance()
	{
		return INSTANCE;
	}
	
	public FontManager()
	{
		
	}
	
	public BitmapFont titleFont;
	public BitmapFont labelFont;
	
	private PixmapPacker packer;
	
	public void initializeFonts() 
	{
	    Preferences fontPreferences = Gdx.app.getPreferences("com.apptogo.runner.preferences.FontPreferences");
	    
	    int displayWidth = fontPreferences.getInteger("DISPLAY_WIDTH", 0);
	    int displayHeight = fontPreferences.getInteger("DISPLAY_HEIGHT", 0);
	    boolean fontsLoaded = false;
	    
	    if (displayWidth != Gdx.graphics.getWidth() || displayHeight != Gdx.graphics.getHeight()) 
	    {
	        Logger.log(this, "Generating fonts because of: display size has changed!", LogLevel.HIGH);
	    } 
	    else 
	    {
	        try 
	        {
	        	titleFont = new BitmapFont( Gdx.files.local("title.fnt") );
	        	labelFont = new BitmapFont( Gdx.files.local("label.fnt") );

	            fontsLoaded = true;
	        } 
	        catch (GdxRuntimeException e) 
	        {
	        	Logger.log(this, "Generating fonts because of: fonts have not been generated yet!", LogLevel.HIGH);
	        }
	    }
	    
	    if ( !fontsLoaded ) 
	    {
	    	fontPreferences.putInteger("DISPLAY_WIDTH", Gdx.graphics.getWidth());
	        fontPreferences.putInteger("DISPLAY_HEIGHT", Gdx.graphics.getHeight());
	        fontPreferences.flush();

	        packer = new PixmapPacker(512, 512, Format.RGBA8888, 2, false);
	        
	        FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.internal( "gfx/fonts/ComicSerif.ttf" ) );
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			
			parameter.size = 60;
			parameter.minFilter = TextureFilter.Linear;
			parameter.magFilter = TextureFilter.Linear;

			titleFont = generator.generateFont(parameter);
			saveFontToFile(titleFont, 28, "title");
			
			parameter.size = 45;
			
			labelFont = generator.generateFont(parameter);
			saveFontToFile(titleFont, 28, "label");
						
			generator.dispose();
			packer.dispose();
	    }
	}
		 
	private void saveFontToFile(BitmapFont font, int fontSize, String fontName) 
	{
		FileHandle fontFile = Gdx.files.local("generatedFonts/" + fontName + ".fnt");
		FileHandle pixmapDir = Gdx.files.local("generatedFonts/" + fontName);
		
		BitmapFontWriter.setOutputFormat(OutputFormat.Text);
	 
		String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(), pixmapDir, fontName);
		
		Logger.log(this, "  - saving font file: " + fontFile.path(), LogLevel.HIGH);
		
		// here we must add the png dir to the page refs
		for (int i = 0; i < pageRefs.length; i++) 
		{
			pageRefs[i] = fontName + "/" + pageRefs[i];
		}
		
		BitmapFontWriter.writeFont(font.getData(), pageRefs, fontFile, new FontInfo(fontName, fontSize), 1, 1);
	}
}
