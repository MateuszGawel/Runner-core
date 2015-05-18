package com.apptogo.runner.actors;

import com.apptogo.runner.animation.MyAnimation;

public class Animation extends Obstacle
{
	private int framesCount;
	
	public Animation(String frameRegionName, int framesCount, float frameDuration, Object animationState, boolean start, boolean loop)
	{
		super();
		
		isGameObstacle = false;
		
		this.framesCount = framesCount;
		
		this.createAnimation(frameRegionName, framesCount, frameDuration, animationState, loop);
		
		this.setWidth( this.animationManager.getCurrentAnimation().getKeyFrame(0).getRegionWidth() );
		
		setAnimate(true);
	}
	
	public void start()
	{
		this.currentFrame = this.animationManager.animate(0f);
		
		this.animationManager.setAnimate(true);
	}
	
	public void stop()
	{
		this.animationManager.setAnimate(false);
	}
	
	public boolean isFinished()
	{
		MyAnimation currentAnimation = this.animationManager.getCurrentAnimation();
		
		if( currentAnimation.frameNumber >= this.framesCount - 1 )
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}
	
	/*protected AnimationManager animationManager;
	protected TextureRegion currentFrame;
	
	protected boolean isFinished = false;
	
	protected boolean scaleFrames;
	protected float scale;
	
	protected int progress = 0;
	protected int framesCount = 0;
		
	public Animation(TextureAtlas textureAtlas, String frameName, int runningFramesCount, float x, float y, boolean doStart, boolean loop)
	{
		this.framesCount = runningFramesCount;
		
		this.setPosition(x, y);
		
		this.animationManager = new AnimationManager( );
		this.animationManager.atlas = textureAtlas;

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
	
	public Animation(String frameName, int runningFramesCount, float x, float y, boolean doStart, boolean loop)
	{
		this.framesCount = runningFramesCount;
		
		this.setPosition(x, y);
		
		this.animationManager = new AnimationManager( );
		//this.animationManager.atlas = textureAtlas;

		animationManager.createAnimation(new MyAnimation(0.3f, CharacterAnimationState.RUNNING, animationManager.createFrames(runningFramesCount, frameName), loop)
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
	
	
	public Animation(String atlasName, String frameName, int runningFramesCount, float x, float y, boolean doStart, boolean loop)
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
		//animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
		isFinished = false;
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
	}*/
}
