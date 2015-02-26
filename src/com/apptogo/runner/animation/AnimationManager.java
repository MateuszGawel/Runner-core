package com.apptogo.runner.animation;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	
	public AtlasRegion[] createFrames(int framesCount, String name){
		AtlasRegion[] frames = new AtlasRegion[framesCount];
		
		float minOffsetX = atlas.findRegion(name + 0).offsetX;
		float minOffsetY = atlas.findRegion(name + 0).offsetY;
		
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
			
			if( frames[i].offsetX < minOffsetX )
			{
				minOffsetX = frames[i].offsetX;
			}
			
			if( frames[i].offsetY < minOffsetY )
			{
				minOffsetY = frames[i].offsetY;
			}
		}
		
		for(int i=0; i<framesCount; i++)
		{	
				frames[i].offsetX -= minOffsetX;
				//to zle dziala - tak jakbym kilka razy od jednej klatki odejmowal
				//frames[i].offsetY -= 10.0f;
		}
		
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
