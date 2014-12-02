package com.apptogo.runner.handlers;

import java.io.IOException;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class TipManager 
{
	private static TipManager INSTANCE;
	public static void create()
	{
		INSTANCE = new TipManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	
	public static TipManager getInstance()
	{
		return INSTANCE;
	}
	
	
	private final String TIP_FILE_PATH = "xml/tip/tip.xml";
	private XmlReader.Element document;
	private Array<XmlReader.Element> gameWorldTypeTips;
	
	
	private TipManager()
	{
		XmlReader reader = new XmlReader();
		
		try
		{
			document = reader.parse( Gdx.files.internal(TIP_FILE_PATH) );
		}
		catch(IOException e) { Logger.log(this, "IO problem with tip file!"); }
				
		gameWorldTypeTips = document.getChildrenByName("gameWorldType");
	}	
	
	public String getTip(GameWorldType gameWorldType)
	{		
		String tip = "";
		String gameWorldTypeString = gameWorldType.toString();
		
		XmlReader.Element currentGameWorldType = null;
		
		for(int i = 0; i < gameWorldTypeTips.size; i++)
		{
			if( gameWorldTypeTips.get(i).getAttribute("type").equals( gameWorldTypeString ) )
			{
				currentGameWorldType = gameWorldTypeTips.get(i);
			}
		}
		
		if( currentGameWorldType == null ) return "";
		
		//trzeba bedzie dodac jeszcze obsluge jezykow!
		Array<XmlReader.Element> tipNodes = currentGameWorldType.getChildrenByName( "tip" );
				
		int tipIndex = (int)(Math.random() * tipNodes.size);
		
		tip = tipNodes.get(tipIndex).getText();
		
		return tip;
	}
}
