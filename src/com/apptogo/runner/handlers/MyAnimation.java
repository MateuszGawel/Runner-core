package com.apptogo.runner.handlers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MyAnimation extends Animation{

	final TextureRegion[] keyFrames;
	private float[] frameDuration;
	private int loopCount = 0;
	private int maxLoopCount;
	
	private float timeElapsed = 0;
	private int frameNumber = 0;

	
	private PlayMode playMode = PlayMode.NORMAL;
	
	public MyAnimation (float[] frameDuration, TextureRegion... keyFrames) {
		super(frameDuration[0], keyFrames);
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}


	@Override
	public int getKeyFrameIndex (float stateTime) {
		if (keyFrames.length == 1) return 0;

		if(loopCount <= maxLoopCount && (stateTime - timeElapsed) / frameDuration[frameNumber] >= 1){
			timeElapsed += frameDuration[frameNumber];
			frameNumber++;
			if(playMode == PlayMode.LOOP){
				if(frameNumber % keyFrames.length == 0) loopCount++;
			}
		}
		
		switch (playMode) {
		case NORMAL:
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			break;
		case LOOP:
			if(loopCount >= maxLoopCount)
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
		return frameNumber;
	}
	
	@Override
	public TextureRegion getKeyFrame (float stateTime, boolean looping) {
		// we set the play mode by overriding the previous mode based on looping
		// parameter value
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
	}
	
	public TextureRegion getKeyFrame (float stateTime, boolean looping, int maxLoopCount) {
		this.maxLoopCount = maxLoopCount;
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
	}
}