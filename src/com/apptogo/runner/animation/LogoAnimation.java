package com.apptogo.runner.animation;

import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

public class LogoAnimation extends Actor
{	
	protected AnimationManager animationManager;
	protected TextureRegion currentFrame;
	
	protected boolean isFinished = false;
		
	public LogoAnimation(String atlasName, String frameName, int runningFramesCount, float x, float y, boolean doStart)
	{				
		this.setPosition(x, y);
		
		this.animationManager = new AnimationManager( atlasName );

		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.RUNNING, animationManager.createFrames(runningFramesCount, frameName), false)
		{
			@Override
			public void additionalTaskDuringAnimation()
			{
				this.setFrameDuration(0.01f);
			}
			
			@Override
			public void onAnimationFinished()
			{
				isFinished = true;
			}
		});
		
		animationManager.createAnimation(new MyAnimation(0.01f, CharacterAnimationState.IDLE, animationManager.createFrames(1, frameName), false)
		{
			@Override
			public void additionalTaskDuringAnimation(){
				this.setFrameDuration(0.01f);
			}
		});
		
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
		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
	}
	
	public boolean isFinished()
	{
		return isFinished;
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
		batch.draw(currentFrame, this.getX(), this.getY());	
	}
}
