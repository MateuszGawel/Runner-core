package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Player;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ParallaxBackground extends Image{

	float speedFactor = 0;
	Player player;
	Vector2 mapSize;
	
	public ParallaxBackground(Texture texture, float speedFactor, Player player){
		super(texture);
		this.speedFactor = speedFactor;
		this.player = player;
	}
	
	public ParallaxBackground(Texture texture, Vector2 mapSize, Player player){
		super(texture);
		this.mapSize = mapSize;
		this.player = player;
		this.speedFactor = - (texture.getWidth() - Runner.SCREEN_WIDTH) / mapSize.x;
	}
	
	@Override
	public void act(float delta){
		if(speedFactor != 0)
			setPosition(player.getX()*PPM*speedFactor, getY());
	}
}
