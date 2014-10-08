package com.apptogo.runner.animation;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ObjectAnimation extends Actor
{	
	protected AnimationManager animationManager;
	protected TextureRegion currentFrame;
	
	protected boolean isFinished = false;
	
	protected boolean scaleFrames;
	protected float scale;
	
	protected int progress = 0;
	protected int framesCount = 0;
		
	public ObjectAnimation(String atlasName, String frameName, int runningFramesCount, float x, float y, boolean doStart, boolean loop)
	{				
		this.framesCount = runningFramesCount;
		
		this.setPosition(x, y);
		
		this.animationManager = new AnimationManager( atlasName );

		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.RUNNING, animationManager.createFrames(runningFramesCount, frameName), loop)
		{
			@Override
			public void onAnimationFinished()
			{
				isFinished = true;
			}
		});
		
		animationManager.createAnimation(new MyAnimation(0.03f, CharacterAnimationState.IDLE, animationManager.createFrames(1, frameName), false));
		
		if( doStart )
		{
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);		
		}
		else
		{
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);	
		}
	}
	
	public void start()
	{
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
		isFinished = false;
		Logger.log(this, "ANIMACJA START");
	}
	
	public boolean isFinished()
	{
		return isFinished;
	}
	
	public void scaleFrames(float scale)
	{
		scaleFrames = true;
		this.scale = scale;
	}
	
	@Override
	public void act(float delta) 
	{
		super.act(delta);
		currentFrame = animationManager.animate(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		if(scaleFrames)
		{		
			batch.draw(currentFrame, this.getX(), this.getY(), 0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scale, scale, 0);
		}
		else
		{
			batch.draw(currentFrame, this.getX(), this.getY());	
		}
	}
}
