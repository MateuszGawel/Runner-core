package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class CharacterButton extends Image{

	private TextureRegion textureRegion;
	
	public CharacterButton(String buttonName, float posX, float posY){
		super(((TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/gameGuiAtlas.pack")).findRegion(buttonName));
		this.textureRegion = ((TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/gameGuiAtlas.pack")).findRegion(buttonName);
		setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		
		
		setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		setPosition(posX, posY);
		setBounds(getX(), getY(), getWidth(), getHeight());
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	
}
