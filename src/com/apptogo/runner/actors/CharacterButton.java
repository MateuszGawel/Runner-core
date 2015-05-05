package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class CharacterButton extends Image{

	private TextureRegion textureRegion;
	
	public CharacterButton(String buttonName, float posX, float posY){
		super(ResourcesManager.getInstance().getAtlasRegion(buttonName));
		this.textureRegion = ResourcesManager.getInstance().getAtlasRegion(buttonName);
		setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		
		
		setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		setPosition(posX, posY);
		setBounds(getX(), getY(), getWidth(), getHeight());
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	
}
