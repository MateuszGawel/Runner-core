package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Character;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RepeatingParallaxBackground extends Actor{

	private float xFactor = 0;
	private float yFactor = 0;
	private Character player;
	private Vector2 mapSize;
	private Texture texture;
	private float x, y;
	private int xWrapCount;
	private float currentY;
	
	public RepeatingParallaxBackground(Texture texture, float xFactor, float yFactor, Vector2 mapSize, Character player, float x, float y){
		this.texture = texture;
		this.xFactor = xFactor;
		this.yFactor = yFactor;
		this.mapSize = mapSize;
		this.player = player;
		this.x = x;
		this.y = y;
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		xWrapCount = (int) Math.ceil(mapSize.x / texture.getWidth());
	}
	
	@Override
	public void act(float delta){
		currentY = y + (player.getY() - (float)mapSize.y/2/PPM) * yFactor;
		setPosition(player.getX()*xFactor, currentY);
		
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
