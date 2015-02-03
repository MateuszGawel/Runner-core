package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxBackground extends Actor{

	
	private float xFactor = 0;
	private float yFactor = 0;
	private Character character;
	private float x, y;
	private Vector2 mapSize;
	private float currentY;
	private float percent;
	private TextureRegion textureRegion;
	private int initialRegionWidth;
	
	public ParallaxBackground(TextureRegion textureRegion, Vector2 mapSize, float xFactor, float yFactor, Character player, float x, float y){
		this.character = player;
		this.x = x;
		this.y = y;
		this.initialRegionWidth = textureRegion.getRegionWidth();
		this.textureRegion = textureRegion;
		setPosition(x/PPM, y/PPM);
		setWidth(getWidth()/PPM);
		setHeight(getHeight()/PPM);
		this.xFactor = xFactor;
		this.yFactor = yFactor;
		this.mapSize = mapSize;
	}
	
	@Override
	public void act(float delta){
		currentY = y + (character.getY() - (float)mapSize.y/2/PPM) * yFactor;
		setWidth(textureRegion.getRegionWidth()/PPM);
		setHeight(textureRegion.getRegionHeight()/PPM);
		setPosition(0, currentY);
	}
	
	private void drawRegion(Batch batch, float offSet){

		textureRegion.setRegionX((int)(character.getBody().getPosition().x*PPM*xFactor - (offset-1)*textureRegion.getRegionWidth()));
		textureRegion.setRegionWidth(initialRegionWidth);
		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

		textureRegion.setRegionX((int)(character.getBody().getPosition().x*PPM*xFactor - offset*textureRegion.getRegionWidth()));
		textureRegion.setRegionWidth(initialRegionWidth);
		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
	}
	
	private float offset;
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha); 

		if(xFactor == 0){
			percent = character.getBody().getPosition().x / (mapSize.x/PPM);		
			textureRegion.setRegionX((int)((textureRegion.getRegionWidth()-Runner.SCREEN_WIDTH) * percent));
			textureRegion.setRegionWidth(initialRegionWidth);	
			batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
		}
		else{
			drawRegion(batch, offset);
			if(textureRegion.getRegionX() > textureRegion.getRegionWidth() - Runner.SCREEN_WIDTH){
				offset++;
			}
		}
	}
}
