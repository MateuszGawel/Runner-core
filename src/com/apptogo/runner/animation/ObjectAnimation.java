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

public class ObjectAnimation extends Actor
{	
	protected AnimationManager animationManager;
	protected TextureRegion currentFrame;
	
	protected boolean isFinished = false;
	
	protected boolean scaleFrames;
	protected float scale;
		
	public ObjectAnimation(String atlasName, String frameName, int runningFramesCount, float x, float y, boolean doStart, boolean loop)
	{				
		this.setPosition(x, y);
		
		this.animationManager = new AnimationManager( atlasName );

		animationManager.createAnimation(new MyAnimation(0.02f, CharacterAnimationState.RUNNING, animationManager.createFrames(runningFramesCount, frameName), loop)
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
		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
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