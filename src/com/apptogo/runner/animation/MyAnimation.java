package com.apptogo.runner.animation;

import com.apptogo.runner.logger.Logger;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class MyAnimation extends Animation{

	final TextureRegion[] keyFrames;
	private float[] frameDuration;
	private int loopCount = 0;
	private int maxLoopCount = 0;
	private boolean animationFinished;
	private float timeElapsed = 0;
	private int frameNumber = 0;
	private boolean looping;
	private Object animationState;
	
	private PlayMode playMode = PlayMode.NORMAL;
	
	public MyAnimation (float[] frameDuration, Object animationState, AtlasRegion[] keyFrames, boolean looping) {
		super(frameDuration[0], keyFrames);
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
		this.animationState = animationState;
		this.looping = looping;
	}
	
	public MyAnimation (float frameDuration, Object animationState, AtlasRegion[] keyFrames, boolean looping) {
		super(frameDuration, keyFrames);
		this.frameDuration = new float[keyFrames.length];
		for(int i=0; i<keyFrames.length; i++){
			this.frameDuration[i] = frameDuration;
		}
		this.keyFrames = keyFrames;
		this.animationState = animationState;
		this.looping = looping;
	}
	
	public MyAnimation (float frameDuration, Object animationState, AtlasRegion[] keyFrames, boolean looping, int loopCount) {
		super(frameDuration, keyFrames);
		//Logger.log(this, "dostalem maxa: " + loopCount);
		this.frameDuration = new float[keyFrames.length];
		for(int i=0; i<keyFrames.length; i++){
			this.frameDuration[i] = frameDuration;
		}
		this.keyFrames = keyFrames;
		this.animationState = animationState;
		this.looping = looping;
		this.maxLoopCount = loopCount;
	}
	

	public void setFrameDuration(float frameDuration){
		for(int i=0; i<keyFrames.length; i++){
			this.frameDuration[i] = frameDuration;
		}
	}

	@Override
	public int getKeyFrameIndex (float stateTime) {
		//Logger.log(this, "maxLoop: " + maxLoopCount + " currentLoop: " + loopCount);
		if(stateTime < frameDuration[0])
			resetLoops();
		if (keyFrames.length == 1) return 0;
		if(loopCount <= maxLoopCount && (stateTime - timeElapsed) / frameDuration[frameNumber] >= 1){
			timeElapsed += frameDuration[frameNumber];
			frameNumber++;
			if(playMode == PlayMode.LOOP)
				if(maxLoopCount!= 0 && frameNumber % keyFrames.length == 0) loopCount++;
		}
		else if(loopCount > maxLoopCount){
			this.animationFinished = true;
		}
		
		switch (playMode) {
		case NORMAL:
			if(frameNumber > keyFrames.length - 1)
				this.animationFinished = true;
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			break;
		case LOOP:
			if(maxLoopCount!= 0 && loopCount >= maxLoopCount)
				frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			else
				frameNumber = frameNumber % keyFrames.length;
			break;
		case LOOP_PINGPONG:
			frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
			if (frameNumber >= keyFrames.length) frameNumber = keyFrames.length - 2 - (frameNumber - keyFrames.length);
			break;
		case LOOP_RANDOM:
			frameNumber = MathUtils.random(keyFrames.length - 1);
			break;
		case REVERSED:
			frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
			break;
		case LOOP_REVERSED:
			frameNumber = frameNumber % keyFrames.length;
			frameNumber = keyFrames.length - frameNumber - 1;
			break;
		}
		//Logger.log(this, "zwracam numer: " + frameNumber);
		return frameNumber;
	}
	
	/*
	@Override
	public TextureRegion getKeyFrame (float stateTime) {
		PlayMode oldPlayMode = playMode;
		if (looping && (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.NORMAL)
				playMode = PlayMode.LOOP;
			else
				playMode = PlayMode.LOOP_REVERSED;
		} else if (!looping && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.LOOP_REVERSED)
				playMode = PlayMode.REVERSED;
			else
				playMode = PlayMode.LOOP;
		}

		TextureRegion frame = getKeyFrame(stateTime);
		playMode = oldPlayMode;
		return frame;
	}*/
	
	public TextureRegion getKeyFrame (float stateTime) {
		additionalTaskDuringAnimation();
		PlayMode oldPlayMode = playMode;
		if (looping && (playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.NORMAL)
				playMode = PlayMode.LOOP;
			else
				playMode = PlayMode.LOOP_REVERSED;
		} else if (!looping && !(playMode == PlayMode.NORMAL || playMode == PlayMode.REVERSED)) {
			if (playMode == PlayMode.LOOP_REVERSED)
				playMode = PlayMode.REVERSED;
			else
				playMode = PlayMode.LOOP;
		}
		TextureRegion frame = keyFrames[getKeyFrameIndex(stateTime)];
		playMode = oldPlayMode;
		if(isAnimationFinished())
			onAnimationFinished();
		return frame;
	}

	public boolean isAnimationFinished () {
		return animationFinished;
	}
	
	public void onAnimationFinished(){
		//to override
	}
	
	public void additionalTaskDuringAnimation(){
		//to override
	}
	
	public void resetLoops(){
		this.loopCount = 0;
		this.animationFinished = false;
		this.timeElapsed = 0;
		this.frameNumber = 0;
	}
	public int getFrameNumber(){
		return this.frameNumber;
	}
	public Object getAnimationState(){ return this.animationState; }
}