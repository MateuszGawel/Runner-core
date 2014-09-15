package com.apptogo.runner.animation;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {

	private static TextureAtlas atlas;
	private Object currentAnimationState;
	private Object prevAnimationState;
	private List<MyAnimation> animations = new ArrayList<MyAnimation>();
	private float stateTime = 0;
	private TextureRegion currentFrame;
	
	public AnimationManager(String atlasName){
		atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), atlasName);
	}
	
	public AtlasRegion[] createFrames(int framesCount, String name){
		AtlasRegion[] frames = new AtlasRegion[framesCount];
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
		}
		return frames;
	}
	
	public void createAnimation(MyAnimation animation){
		animations.add(animation);
	}
	
	public void createAnimation(int framesCount, float frameTime, String name, Object animationState, boolean looping){
		AtlasRegion[] frames = new AtlasRegion[framesCount];
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
		}
		//narazie obsluguje utworzenie tylko z rownym czasem i framemami podanymi w tablicy
		animations.add(new MyAnimation(frameTime, animationState, frames, looping));
	}
	
	public void createAnimation(int framesCount, float frameTime, String name, Object animationState, boolean looping, int loopCount){
		AtlasRegion[] frames = new AtlasRegion[framesCount];
		for(int i=0; i<framesCount; i++){
			frames[i] = atlas.findRegion(name + i);
		}
		//narazie obsluguje utworzenie tylko z rownym czasem i framemami podanymi w tablicy
		animations.add(new MyAnimation(frameTime, animationState, frames, looping, loopCount));
	}
	
	public void setCurrentAnimationState(Object animationState){
		this.prevAnimationState = this.currentAnimationState;
		this.currentAnimationState = animationState;
	}
	
	public TextureRegion animate(float delta){
		if(prevAnimationState != currentAnimationState){
			stateTime = 0;
		}
		prevAnimationState = currentAnimationState;
		
		for(MyAnimation animation : animations){
			if(animation.getAnimationState() == currentAnimationState){
				if(stateTime == 0)
					animation.resetLoops();
				stateTime += delta;
				currentFrame = animation.getKeyFrame(stateTime);
			}
		}
		
		return currentFrame;
	}
	
	public Object getCurrentAnimationState(){ return this.currentAnimationState; }
}
