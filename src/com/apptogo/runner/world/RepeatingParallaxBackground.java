package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static com.apptogo.runner.vars.Box2DVars.ZERO_GROUND_POSITION;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.handlers.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RepeatingParallaxBackground extends Actor{

	float xFactor = 0;
	float yFactor = 0;
	Bandit player;
	Vector2 mapSize;
	Texture texture;
	float x, y;
	int xWrapCount;
	
	public RepeatingParallaxBackground(Texture texture, float xFactor, float yFactor, Vector2 mapSize, Bandit player, float x, float y){
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
	public void act(float delta){
        setPosition(player.getX()*xFactor, y + (player.getY() - (float)ZERO_GROUND_POSITION) * yFactor);
        setWidth(texture.getWidth()/PPM * xWrapCount);
        setHeight(texture.getHeight()/PPM);
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha); 
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(texture, getX(), getY(), getWidth(), getHeight(), 0, 1, xWrapCount, 0);
		//Logger.log(this, "rysuje na x: " + getX() + " y: " + getY() + " width: " + getWidth() + " height: " + getHeight() + " a player ma x: " + player.getX() + " y: " + player.getY());
	}
}
