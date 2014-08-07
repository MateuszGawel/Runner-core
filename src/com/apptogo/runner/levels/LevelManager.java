package com.apptogo.runner.levels;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.world.GameWorld.GameWorldType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class LevelManager {

	private static final LevelManager INSTANCE = new LevelManager();
	public static LevelManager getInstance()
	{
		return INSTANCE;
	}
	
	
	private final String LVL_FILE_PATH = "lvl/lvl.xml";
	private XmlReader.Element document;

	private Array<XmlReader.Element> worldsList;

	
	private LevelManager()
	{
		XmlReader reader = new XmlReader();
		
		try
		{
			document = reader.parse( Gdx.files.internal(LVL_FILE_PATH) );
		}
		catch(IOException e) { Logger.log(this, "IO problem with levels file!"); }
				
		worldsList = document.getChildrenByName("world");
	}	
	
	public Array<LevelWorld> getCampaignLevelWorlds() 
	{
		return getLevelWorlds(true);
	}
	
	public Array<LevelWorld> getMultiplayerLevelWorlds() 
	{
		return getLevelWorlds(false);
	}
	
	private Array<LevelWorld> getLevelWorlds(boolean campaign) 
	{
		Array<LevelWorld> worlds = new Array<LevelWorld>();
		
		for(int i = 0; i < worldsList.size; i++)
		{
			XmlReader.Element world = worldsList.get(i);
			Array<Level> levels = getLevels( world, campaign );
			
			String levelWorldName = world.getAttribute("name");
			GameWorldType levelWorldType = null;
			
			if( levelWorldName.toUpperCase().equals("WILDWEST") )
			{
				levelWorldType = GameWorldType.WILDWEST;
			}
			else if( levelWorldName.toUpperCase().equals("FOREST") )
			{
				levelWorldType = GameWorldType.FOREST;
			}
			else //if( levelWorldName.toUpperCase().equals("SPACE") )
			{
				levelWorldType = GameWorldType.SPACE;
			}
			
			LevelWorld levelWorld = new LevelWorld( levelWorldName, levelWorldType );
			levelWorld.setLevels( levels );
			
			worlds.add( levelWorld );
		}
		
		return worlds;
	}
	
	private Array<Level> getLevels( XmlReader.Element world, boolean campaign )
	{		
		Array<XmlReader.Element> levelsList = new Array<XmlReader.Element>();
		Array<Level> levels = new Array<Level>();
		
		if( campaign ) 
		{
			levelsList = world.getChildByName("campaign").getChildrenByName("level");
		}
		else
		{
			levelsList = world.getChildByName("multiplayer").getChildrenByName("level");
		}
		
		for(int i = 0; i < levelsList.size; i++)
		{
			XmlReader.Element level = levelsList.get(i);
			
			levels.add( new Level( level.getAttribute("buttonLabel"), level.getAttribute("mapPath"), level.getAttribute("unlockKey") ) );
		}
		
		return levels;
	}
}
