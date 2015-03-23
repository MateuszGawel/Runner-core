package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ConstantParallaxBackground extends Image{

	float xSpeed = 0;
	float ySpeed = 0;
	Texture texture;
	float x, y;
	
	public ConstantParallaxBackground(Texture texture, float xSpeed, float ySpeed, float x, float y){
		super(texture);
		this.texture = texture;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.x = x;
		this.y = y;
		setPosition(x/PPM, y/PPM);
		setWidth(getWidth()/PPM);
		setHeight(getHeight()/PPM);
	}
	
	@Override
	public void act(float delta){
        setPosition(getX() - xSpeed*delta, getY() - ySpeed*delta);
	}
}
