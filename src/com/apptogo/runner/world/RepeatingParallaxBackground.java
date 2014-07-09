package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static com.apptogo.runner.vars.Box2DVars.ZERO_GROUND_POSITION;

import com.apptogo.runner.actors.Player;
import com.apptogo.runner.handlers.Logger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RepeatingParallaxBackground extends Actor{

	float xFactor = 0;
	float yFactor = 0;
	Player player;
	Vector2 mapSize;
	Texture texture;
	float x, y;
	int xWrapCount;
	
	public RepeatingParallaxBackground(Texture texture, float xFactor, float yFactor, Vector2 mapSize, Player player, float x, float y){
		this.texture = texture;
		this.xFactor = xFactor;
		this.yFactor = yFactor;
		this.mapSize = mapSize;
		this.player = player;
		this.x = x;
		this.y = y;
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		xWrapCount = (int) Math.ceil(mapSize.x / texture.getWidth());
		Logger.log(this, mapSize.x + " / " + texture.getWidth() + " = " + xWrapCount);
	}
	
	@Override
	public void draw(Batch batch, float delta){
		batch.draw(texture, player.getX()*PPM*xFactor, y + (player.getY()*PPM - (float)ZERO_GROUND_POSITION) * yFactor, texture.getWidth()*xWrapCount, texture.getHeight(), 0, 1, xWrapCount, 0);
	}
}
