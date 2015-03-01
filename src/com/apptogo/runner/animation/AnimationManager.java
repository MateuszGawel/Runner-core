package com.apptogo.runner.animation;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationManager {

	private static TextureAtlas atlas;
	private Object currentAnimationState;
	private Object prevAnimationState;
	private List<MyAnimation> animations = new ArrayList<MyAnimation>();
	public float stateTime = 0;
	private TextureRegion currentFrame;
	private float initStateTime = 0;
	private boolean animate = true;
	
	public AnimationManager(String atlasName){
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), atlasName);
	}
	
	public AnimationManager(String atlasName, float initStateTime, boolean test){
		this(atlasName);
		this.stateTime = initStateTime;
		this.initStateTime = initStateTime;
	}
	
	public Vector2 getMaxOffset(String name)
	{
		Array<AtlasRegion> fs = atlas.getRegions();
		
		float minOffsetX = 100000.0f, minOffsetY = 0.0f;
		
		for(AtlasRegion a: fs)
		{
			if( a.name.contains("alien") )
				if( a.offsetX < minOffsetX )
				{
					minOffsetX = a.offsetX;
				}
			
				if( a.offsetY < minOffsetY )
				{
					minOffsetY = a.offsetY;
				}
		}
		
		return new Vector2(minOffsetX,minOffsetY);
	}
	
	public AtlasRegion[] createFrames(int framesCount, String name){
		AtlasRegion[] frames = new AtlasRegion[framesCount];

		float minOffsetX = atlas.findRegion(name + 0).offsetX;
		float minOffsetY = atlas.findRegion(name + 0).offsetY;
		
		Array<AtlasRegion> fs = atlas.getRegions();
		
		if( name.contains("alien") )
		{Logger.log(this, "ALIEN FOUND");
			for(AtlasRegion a: fs)
			{
				if( a.name.contains("alien") )
					if( a.offsetX < minOffsetX )
						minOffsetX = a.offsetX;
			}
		}
		else if( name.contains("archer") )
		{Logger.log(this, "ARCHER FOUND");
			for(AtlasRegion a: fs)
			{
				if( a.name.contains("archer") )
					if( a.offsetX < minOffsetX )
						minOffsetX = a.offsetX;
			}
		}
		else if( name.contains("bandit") )
		{Logger.log(this, "BANDIT FOUND");
			for(AtlasRegion a: fs)
			{
				if( a.name.contains("bandit") )
					if( a.offsetX < minOffsetX )
						minOffsetX = a.offsetX;
			}
		}
		else Logger.log(this, "NIE ZNALAZLEM TAKIEGO :( ");
		
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
			
			/*if( frames[i].offsetX < minOffsetX )
			{
				minOffsetX = frames[i].offsetX;
			}
			
			if( frames[i].offsetX > maxOffsetX )
			{
				maxOffsetX = frames[i].offsetX;
			}
			
			if( frames[i].offsetY < minOffsetY )
			{
				minOffsetY = frames[i].offsetY;
			}
			
			if( frames[i].offsetY > maxOffsetY )
			{
				maxOffsetY = frames[i].offsetY;
			}*/
		}
		
		for(int i=0; i<framesCount; i++)
		{	
				//frames[i].offsetX -= minOffsetX;
				//to zle dziala - tak jakbym kilka razy od jednej klatki odejmowal
				
				Logger.log(this, "TWORZENIE, name: " + frames[i].name + ", offsetY: " + frames[i].offsetY );
				
				//frames[i].offsetY -= minOffsetY;
		}
		
		Logger.log(this, frames[0].name + ": " + minOffsetX + " | " + minOffsetY );
		
		return frames;
	}
	
	public void createAnimation(MyAnimation animation){
		animations.add(animation);
	}
	
	public void createAnimation(int framesCount, float frameTime, String name, Object animationState, boolean looping){
		/*AtlasRegion[] frames = new AtlasRegion[framesCount];
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
		}*/
		
		AtlasRegion[] frames = createFrames(framesCount, name);
		
		//narazie obsluguje utworzenie tylko z rownym czasem i framemami podanymi w tablicy
		animations.add(new MyAnimation(frameTime, animationState, frames, looping));
	}
	
	public void createAnimation(int framesCount, float frameTime, String name, Object animationState, boolean looping, int loopCount){
		/*AtlasRegion[] frames = new AtlasRegion[framesCount];
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
		}*/
		
		AtlasRegion[] frames = createFrames(framesCount, name);
		
		//narazie obsluguje utworzenie tylko z rownym czasem i framemami podanymi w tablicy
		animations.add(new MyAnimation(frameTime, animationState, frames, looping, loopCount));
	}
	
	public void setCurrentAnimationState(Object animationState){
		this.prevAnimationState = this.currentAnimationState;
		this.currentAnimationState = animationState;
		stateTime = initStateTime;
	}
	
	public TextureRegion animate(float delta){
		if(animate){
			if(prevAnimationState == null || prevAnimationState != currentAnimationState){
				//stateTime = initStateTime;
				prevAnimationState = currentAnimationState;
			}
			
			
			for(MyAnimation animation : animations){
				if(animation.getAnimationState() == currentAnimationState){
					if(stateTime == 0){
						animation.resetLoops();
					}
					stateTime += delta;
					currentFrame = animation.getKeyFrame(stateTime);
				}
			}
		}
		return currentFrame;
	}
	
	public Object getCurrentAnimationState(){ return this.currentAnimationState; }
	public void setAnimate(boolean animate) {
		stateTime = 0;
		this.animate = animate;
	}
}
