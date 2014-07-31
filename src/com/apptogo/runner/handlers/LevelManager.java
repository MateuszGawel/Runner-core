package com.apptogo.runner.handlers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.apptogo.runner.levels.Level;
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

	private Array<XmlReader.Element> levelsList;

	
	private LevelManager()
	{
		XmlReader reader = new XmlReader();
		
		try
		{
			document = reader.parse( Gdx.files.internal(LVL_FILE_PATH) );
		}
		catch(IOException e) { Logger.log(this, "IO problem with levels file!"); }
				
		levelsList = document.getChildrenByName("level");
	}	
	
	public Array<Level> getLevels()
	{		
		Array<Level> levels = new Array<Level>();
				
		for(int i = 0; i < levelsList.size; i++)
		{
			XmlReader.Element level = levelsList.get(i);
			
			levels.add( new Level( level.getAttribute("buttonLabel"), level.getAttribute("mapPath"), level.getAttribute("unlockKey") ) );
		}
		
		return levels;
	}
}
