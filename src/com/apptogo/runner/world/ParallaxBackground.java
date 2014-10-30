package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ParallaxBackground extends Image{

	
	float xFactor = 0;
	float yFactor = 0;
	Character player;
	float x, y;
	Vector2 mapSize;
	
	public ParallaxBackground(Texture texture, Character player, float x, float y){
		super(texture);
		this.player = player;
		this.x = x;
		this.y = y;
		setPosition(x/PPM, y/PPM);
		setWidth(getWidth()/PPM);
		setHeight(getHeight()/PPM);
	}
	public ParallaxBackground(Texture texture, float xFactor, float yFactor, Character player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = xFactor;
		this.yFactor = yFactor;		
	}
	
	public ParallaxBackground(Texture texture, Vector2 mapSize, Character player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = - (texture.getWidth() - Runner.SCREEN_WIDTH) / mapSize.x;
		this.yFactor = xFactor;
		this.mapSize = mapSize;
		Logger.log(this, "floor: " + mapSize.y/2/PPM);
	}
	
	public ParallaxBackground(Texture texture, Vector2 mapSize, float yFactor, Character player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = - (texture.getWidth() - Runner.SCREEN_WIDTH) / mapSize.x;
		this.yFactor = yFactor;
		this.mapSize = mapSize;
		Logger.log(this, "floor: " + mapSize.y/2/PPM);
	}
	
	@Override
	public void act(float delta){
		if(xFactor != 0)
			setPosition(player.getX()*xFactor, y + (player.getY() - (float)mapSize.y/2/PPM) * yFactor);
	}
}
