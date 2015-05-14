package com.apptogo.runner.shop;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.utils.Array;

public class ShopItem
{
	/**
	 * 
	 */
	private final ShopManager ShopItem;

	public CharacterAbilityType abilityType;
	
	public String thumbnailName;
	
	public String title;
	public String description;
	
	public Array<Integer> prices;
	
	public int maxLevel;
	public int currentLevel;
	
	public ShopItem(ShopManager shopManager, CharacterAbilityType abilityType, String thumbnailName, String title, String description, Integer[] prices, Player player)
	{
		ShopItem = shopManager;
		this.abilityType = abilityType;
		this.thumbnailName = thumbnailName;
		this.title = title;
		this.description = description;
					
		this.prices = new Array<Integer>(prices);
		
		this.maxLevel = this.prices.size;
		this.currentLevel = player.getAbilityLevel(this.abilityType);
	}
}