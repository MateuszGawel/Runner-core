package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameProgressBarHead extends Image{

	private TextureRegion texture;
	private GameWorld gameWorld;
	private Character character;
	private float percent, parentWidth, parentHeight;
	
	public GameProgressBarHead(TextureRegion texture, GameWorld gameWorld, Character character, float parentWidth, float parentHeight){
		super(texture);
		this.texture = texture;
		this.gameWorld = gameWorld;
		this.character = character;
		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
		setSize(texture.getRegionWidth(), texture.getRegionHeight());
		setPosition(0, parentHeight/2 - getHeight()/2);
	}
	

	@Override
	public void act(float delta) {		
		percent = (character.getBody().getPosition().x - 15.5f) / (((gameWorld.mapSize.x)/PPM-10.5f-20));
		if(character.getX() < gameWorld.mapSize.x/PPM-20) 
			setPosition((parentWidth) * percent - getWidth()/2, getY());
	}

}
