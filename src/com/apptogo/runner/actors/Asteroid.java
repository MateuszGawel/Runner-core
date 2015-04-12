package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Asteroid extends Actor implements Poolable{
	public boolean alive;
	
	private TextureAtlas asteroidAtlas;
	private Random random = new Random();
	private float speedX, speedY, rotationSpeed;

	private TextureRegion currentFrame;

	public Asteroid(){
        this.alive = false;
        asteroidAtlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/levels/spaceAtlas.pack");
        setPosition(1500, 0);
	}

    public void init() {
    	//currentFrame = asteroidAtlas.findRegion("asteroid"+(random.nextInt(4)+1));
    	currentFrame = ResourcesManager.getInstance().getAtlasRegion( "asteroid"+(random.nextInt(4)+1) );
    	
    	setPosition(1300/PPM, random.nextInt(900)/PPM - 50/PPM);
    	setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM);
		setWidth(currentFrame.getRegionWidth()/PPM);
		setHeight(currentFrame.getRegionHeight()/PPM);
        alive = true;	
        speedX = random.nextInt(15) + 4;
        speedY = random.nextFloat() - 0.5f;
        rotationSpeed = random.nextInt(4)-2;
    }
    
	@Override
	public void reset() {
		setPosition(1500/PPM, 0);
        alive = false;
	}
	
	@Override
	public void act(float delta){
		if(getX() < -10/PPM || getY() > 810/PPM || getY() < -10/PPM) reset();
		if(alive){
			setX(getX() - speedX*delta);
			setY(getY() - speedY*delta);
			setRotation(getRotation() + rotationSpeed);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}
