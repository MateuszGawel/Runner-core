package com.apptogo.runner.handlers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class LanguageManager {

	private static final LanguageManager INSTANCE = new LanguageManager();
	public static LanguageManager getInstance()
	{
		return INSTANCE;
	}
	
	
	private final String STR_FILE_PATH = "ui/lang/str.xml";
	private XmlReader.Element document;
	private XmlReader.Element currentLanguage;
	private Array<XmlReader.Element> languages;
	
	private String currentLanguageId;
	
	private LanguageManager()
	{
		XmlReader reader = new XmlReader();
		
		try
		{
			document = reader.parse( Gdx.files.internal(STR_FILE_PATH) );
		}
		catch(IOException e) { Logger.log(this, "IO problem with language pack strings file!"); }
				
		languages = document.getChildrenByName("language");
	}	
	
	/** Nalezy zadbac aby currentLanguage byl wczesniej ustawiony! */
	public String getString(String key)
	{		
		String string = "";
		Array<XmlReader.Element> stringNodes = currentLanguage.getChildrenByName("string");
				
		for(int i = 0; i < stringNodes.size; i++)
		{
			XmlReader.Element stringNode = stringNodes.get(i);

			if( stringNode.getAttribute("key").equals(key) )
			{
				string = stringNode.getText();
				break;
			}
		}
		
		return string;
	}
		
	public XmlReader.Element getCurrentLanguage()
	{
		return currentLanguage;
	}
	public void setCurrentLanguage(String id)
	{
		for(int i = 0; i < languages.size; i++)
		{
			XmlReader.Element node = languages.get(i);

			if( node.getAttribute("id").equals(id) )
			{
				currentLanguage = node;
				break;
			}
		}
	}	
	public String getDefaultLanguageId()
	{
		String defaultLanguage = "";
		
		for(int i = 0; i < languages.size; i++)
		{
			XmlReader.Element node = languages.get(i);
			
			if( node.getAttribute("default").equals("true") )
			{
				defaultLanguage = node.getAttribute("id");
				break;
			}
		}
		
		return defaultLanguage;
	}
	public FileHandle getIcoFile(String key)
	{
		FileHandle icon = null;
		
		for(int i = 0; i < languages.size; i++)
		{
			XmlReader.Element node = languages.get(i);
			
			if( node.getAttribute("id").equals(key) )
			{
				if( currentLanguage.getAttribute("id").equals(key) )
				{
					icon = Gdx.files.internal( node.getAttribute("chosenIcoFilePath") );
				}
				else icon = Gdx.files.internal( node.getAttribute("icoFilePath") );
				break;
			}
		}
		
		return icon;
	}
}
