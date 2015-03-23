package com.apptogo.runner.handlers;

import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.utils.Array;


public class ShopManager 
{
	private static ShopManager INSTANCE;
	public static void create()
	{
		INSTANCE = new ShopManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	
	public static ShopManager getInstance()
	{
		return INSTANCE;
	}
	
	public class ShopItem
	{
		public String thumbnailName;
		public int price;
		public int maxLevel;
		public String title;
		public String description;
		
		public ShopItem(String thumbnailPath, String title, String description)
		{
			this.thumbnailName = thumbnailPath;
			this.title = title;
			this.description = description;
		}
	}
	
	Array<ShopItem> powerups;
	Array<ShopItem> abilities;
	Array<ShopItem> skins;
	
	private ShopManager()
	{
		powerups = new Array<ShopItem>();
		abilities = new Array<ShopItem>();
		skins = new Array<ShopItem>();   	
		
		//adding powerups
		this.powerups.add( new ShopItem("item", "Jakis tytul", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul2", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul3", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul4", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul5", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul6", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		this.powerups.add( new ShopItem("item", "Jakis tytul7", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		
		//adding abilities
		
		//adding skins
	}	
	
	public Array<String> getTextures()
	{
		Array<String> textures = new Array<String>();
		
		for(ShopItem item: this.powerups)
		{
			if( textures.indexOf(item.thumbnailName, true) == -1 )
			{
				textures.add(item.thumbnailName);
			}
		}
		
		for(ShopItem item: this.abilities)
		{
			if( textures.indexOf(item.thumbnailName, true) == -1 )
			{
				textures.add(item.thumbnailName);
			}
		}
		
		for(ShopItem item: this.skins)
		{
			if( textures.indexOf(item.thumbnailName, true) == -1 )
			{
				textures.add(item.thumbnailName);
			}
		}
		Logger.log(this, textures.size);
		return textures;
	}
	
	public Array<ShopItem> getPowerups()
	{
		return this.powerups;
	}
	
	public Array<ShopItem> getAbilities()
	{
		return this.abilities;
	}
	
	public Array<ShopItem> getSkins()
	{
		return this.skins;
	}
	
}
