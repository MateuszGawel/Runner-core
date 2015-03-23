package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RepeatingConstantParallaxBackground extends Actor{

	float xSpeed = 0;
	float ySpeed = 0;
	Texture texture;
	Vector2 mapSize;
	float x, y;
	int xWrapCount;
	
	public RepeatingConstantParallaxBackground(Texture texture, float xSpeed, float ySpeed, Vector2 mapSize, float x, float y){
		this.texture = texture;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.mapSize = mapSize;
		this.x = x;
		this.y = y;
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		xWrapCount = (int) Math.ceil(mapSize.x / texture.getWidth());
	}
	
	@Override
	public void act(float delta){
        setPosition(getX() - xSpeed*delta, getY() - ySpeed*delta);
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
