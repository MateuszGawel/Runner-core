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

		this.items.add( new ShopItem(this, CharacterAbilityType.ARROW, "shop_arrow", "ARROW", "Guess what is in your eye?", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.OIL, "shop_arrow", "OIL", "Nie wypierdolisz siê, ten olej spowalnia.", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOAR, "shop_arrow", "BOAR", "W³ochaty jebaniec. Uciekaj", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BLACKHOLE, "shop_blackhole", "BLACKHOLE", "Teleport in other words", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.BOMB, "shop_bomb", "BOMB", "A little black sphere that will rock your socks!", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.DEATH, "shop_death", "DEATH", "Powerful weapon that will kill everyone", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.FORCEFIELD, "shop_forcefield", "FORCEFIELD", "Kick everyone in your area", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.LIFT, "shop_lift", "LIFT", "Straight to the sky!", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.SHIELD, "shop_shield", "SHIELD", "The only way you are protected when it is hot", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.SNARES, "shop_snares", "SNARES", "Watch your back", new Integer[]{100, 200, 300}, player)  );
		this.items.add( new ShopItem(this, CharacterAbilityType.SUPERSPEED, "shop_superspeed", "SUPERSPEED", "Fast as fuck!", new Integer[]{100, 200, 300}, player)  );	
		
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
			
			shopItem.currentLevel++;
			
			return true;
		}
		
		return false;
	}
}
