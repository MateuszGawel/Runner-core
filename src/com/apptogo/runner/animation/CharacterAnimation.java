package com.apptogo.runner.animation;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
				
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, animationManager.createFrames(18, "bandit_run"), true)
		{
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(0.03f);
			}
		});
		
		animationManager.createAnimation(new MyAnimation(0.06f, CharacterAnimationState.IDLE, animationManager.createFrames(20, "bandit_idle"), true)
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
		batch.draw(currentFrame, x + ((AtlasRegion)currentFrame).offsetX, y + ((AtlasRegion)currentFrame).offsetY); 
	}
}
