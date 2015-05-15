package com.apptogo.runner.shop;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.Player;
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
	
	Array<ShopItem> items;
	
	private ShopManager()
	{

	}	
	
	public Array<ShopItem> getShopItems(Player player)
	{
		items = new Array<ShopItem>();

		this.items.add( new ShopItem(this, CharacterAbilityType.SUPERSPEED, "item", "SUPERSPEED", "Fast as fuck!", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "item", "BOMB", "A little black sphere that will rock your socks!", new Integer[]{100, 200, 300}, player)  ); 
		this.items.add( new ShopItem(this, CharacterAbilityType.ARROW, "item", "ARROW", "Guess what is in your eye?", new Integer[]{100, 200, 300}, player)  ); 
		this.items.add( new ShopItem(this, CharacterAbilityType.LIFT, "item", "LIFT", "Straight to the sky!", new Integer[]{100, 200, 300}, player)  ); 
		this.items.add( new ShopItem(this, CharacterAbilityType.SNARES, "item", "SNARES", "Watch your back", new Integer[]{100, 200, 300}, player)  ); 
		this.items.add( new ShopItem(this, CharacterAbilityType.BLACKHOLE, "item", "BLACKHOLE", "Teleport in other words", new Integer[]{100, 200, 300}, player)  );
		
		
		return items;
	}
	
	public boolean buyShopItem(ShopItem shopItem, Player player)
	{
		int itemLevel = player.getAbilityLevel( shopItem.abilityType );
		
		if( itemLevel < shopItem.maxLevel && shopItem.prices.get( itemLevel ) <= player.coins )
		{
			player.coins -= shopItem.prices.get( itemLevel );
			player.abilities.put(shopItem.abilityType.toString(), itemLevel + 1);
						
			player.save();
			
			return true;
		}
		
		return false;
	}
}
