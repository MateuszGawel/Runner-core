package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static com.apptogo.runner.vars.Box2DVars.ZERO_GROUND_POSITION;

import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ParallaxBackground extends Image{

	
	float xFactor = 0;
	float yFactor = 0;
	Bandit player;
	float x, y;
	
	public ParallaxBackground(Texture texture, Bandit player, float x, float y){
		super(texture);
		this.player = player;
		this.x = x;
		this.y = y;
		setPosition(x, y);
	}
	public ParallaxBackground(Texture texture, float xFactor, float yFactor, Bandit player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = xFactor;
		this.yFactor = yFactor;		
	}
	
	public ParallaxBackground(Texture texture, Vector2 mapSize, Bandit player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = - (texture.getWidth() - Runner.SCREEN_WIDTH) / mapSize.x;
		this.yFactor = xFactor;
	}
	
	public ParallaxBackground(Texture texture, Vector2 mapSize, float yFactor, Bandit player, float x, float y){
		this(texture, player, x, y);
		this.xFactor = - (texture.getWidth() - Runner.SCREEN_WIDTH) / mapSize.x;
		this.yFactor = yFactor;
	}
	
	@Override
	public void act(float delta){
		if(xFactor != 0)
			setPosition(player.getX()*PPM*xFactor, y + (player.getY()*PPM - (float)ZERO_GROUND_POSITION) * yFactor);
	}
}
