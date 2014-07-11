package com.apptogo.runner.vars;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.apptogo.runner.handlers.Logger;
import com.badlogic.gdx.Gdx;

public class LanguageManager {

	private static final LanguageManager INSTANCE = new LanguageManager();
	public static LanguageManager getInstance()
	{
		return INSTANCE;
	}
	
	
	private final String STR_FILE_PATH = "gfx/game/levels/ground.png";
	private Document document;
	private NodeList languages;
	private Node currentLanguage;
	
	private String currentLanguageId;
	
	private LanguageManager()
	{
		try
		{
			File file = new File( "C:\\Users\\Comarch\\Desktop\\materia³y\\_Run_ner_12302013\\Runner\\android\\assets\\ui\\lang\\str.xml" ); // new File(STR_FILE_PATH);
			if( file.exists() ) Logger.log(this, "Istnieje :)");
			else Logger.log(this, "Nie istnieje :(");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
			
			document.getDocumentElement().normalize();
			
			languages = document.getElementsByTagName("language");
		}
		catch(Exception e){}
		
		currentLanguageId = "en"; //tu bedzie odczytywanie z ustawien
		
		setCurrentLanguage(currentLanguageId);
	}	
	
	public String getString(String key)
	{
		String string = "";
		NodeList stringNodes = ((Element)currentLanguage).getElementsByTagName("string");
				
		for(int i = 0; i < stringNodes.getLength(); i++)
		{
			Node stringNode = stringNodes.item(i);

			if( (stringNode.getNodeType() == Node.ELEMENT_NODE) && ((Element)stringNode).getAttribute("key").equals(key) )
			{
				string = stringNode.getTextContent();
				break;
			}
		}
		
		return string;
	}
		
	public Element getCurrentLanguage()
	{
		return (Element)currentLanguage;
	}
	public void setCurrentLanguage(String id)
	{
		for(int i = 0; i < languages.getLength(); i++)
		{
			Node node = languages.item(i);

			if( (node.getNodeType() == Node.ELEMENT_NODE) && ((Element)node).getAttribute("id").equals(id) )
			{
				currentLanguage = node;
				break;
			}
		}
	}	
}
