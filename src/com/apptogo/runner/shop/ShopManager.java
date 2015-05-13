package com.apptogo.runner.shop;

import com.apptogo.runner.enums.CharacterAbilityType;
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
		
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "item", "Bomb", "A little black sphere that will rock your socks!1", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "item", "Bomb", "A little black sphere that will rock your socks!2", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "item", "Bomb", "A little black sphere that will rock your socks!3", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "item", "Bomb", "A little black sphere that will rock your socks!4", new Integer[]{100, 200, 300}, player)  );
		
		return items;
	}
	
	public boolean buyShopItem(ShopItem shopItem, Player player)
	{
		int itemLevel = player.getAbilityLevel( shopItem.abilityType );
		
		if( shopItem.prices.get( itemLevel - 1 ) <= player.coins && itemLevel < shopItem.maxLevel )
		{
			player.coins -= shopItem.prices.get( itemLevel - 1 );
			player.abilities.put(shopItem.abilityType, itemLevel + 1);
			
			return true;
		}
		
		return false;
	}
}
