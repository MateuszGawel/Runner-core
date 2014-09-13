package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CharacterAnimation extends Actor 
{
	protected AnimationManager animationManager;
	protected TextureRegion currentFrame;
	
	protected float x,y;
	
	private boolean running = true;
	
	public CharacterAnimation(String atlasName, float x, float y, boolean running)
	{
		this(atlasName,x,y);
		
		if(running)
			start();
		else
			stop();
	}
	
	public CharacterAnimation(String atlasName, float x, float y)
	{		
		this.x = x;
		this.y = y;
		
		animationManager = new AnimationManager(atlasName);
				
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, AnimationManager.createFrames(18, "run"), true)
		{
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(0.03f);
			}
		});
		
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, AnimationManager.createFrames(20, "idle"), true)
		{
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(0.03f);
			}
		});
		

		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
	}
	
	public void toggle()
	{
		if(running)
			stop();
		else
			start();
	}
	
	public void stop()
	{
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		running = false;
	}
	
	public void start()
	{
		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
		running = true;
	}
	
	@Override
	public void act(float delta) 
	{
		currentFrame = animationManager.animate(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		batch.draw(currentFrame, x, y);	
	}
}
