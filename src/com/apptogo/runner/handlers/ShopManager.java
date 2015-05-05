package com.apptogo.runner.handlers;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.PowerupType;
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
		
		public String title;
		public String description;
		
		public Array<Integer> prices;
		
		public int maxLevel;
		public int currentLevel;
		
		public ShopItem(String id)
		{
			
		}
	}
	
	public class AbilityItem extends ShopItem
	{
		CharacterAbilityType id;
		
		public AbilityItem(String id) {
			super(id);
			// TODO Auto-generated constructor stub
		}
	}
	
	public class PowerupItem extends ShopItem
	{
		PowerupType id;
		
		public PowerupItem(String id) {
			super(id);
			// TODO Auto-generated constructor stub
		}
	}
	
	Array<AbilityItem> powerups;
	Array<PowerupItem> abilities;
	
	private ShopManager()
	{
		powerups = new Array<AbilityItem>();
		abilities = new Array<PowerupItem>();
		//skins = new Array<ShopItem>();   	
		
		//adding powerups
		//this.powerups.add( new ShopItem(1000, "item", "Jakis tytul", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(2000, "item", "Jakis tytul2", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(3000, "item", "Jakis tytul3", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(4000, "item", "Jakis tytul4", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(5000, "item", "Jakis tytul5", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(6000, "item", "Jakis tytul6", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		//this.powerups.add( new ShopItem(7000, "item", "Jakis tytul7", "Przydlugawy opis tylko na potrzeby prezentacji, nie majacy nic wspolnego z tym itemem. Po prostu chce zobaczyc jak to wyglada.") );
		
		//adding abilities
	}	
	
	public void refreshItems()
	{
		powerups = new Array<AbilityItem>();
		abilities = new Array<PowerupItem>();
		
		
	}
		
	public Array<ShopItem> getPowerups()
	{
		return null;//this.powerups;
	}
	
	public Array<ShopItem> getAbilities()
	{
		return null;//this.abilities;
	}	
}
