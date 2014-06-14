package com.apptogo.runner.animators;

import com.apptogo.runner.actors.Player;
import com.apptogo.runner.actors.Player.PlayerAnimationState;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimator {
	
	private Player player;
	private float stateTime;

	private TextureRegion currentFrame;
	private TextureRegion[] playerFrames;
	private TextureAtlas stickmanAtlas;
	
	/*--animations--*/
	//private final int IDLE_FRAMES_COUNT = 10;
	//private final int BORED_FRAMES_COUNT = 10;
	private final int RUNNING_FRAMES_COUNT = 20;
	
	//private AtlasRegion[] idleFrames;
	//private AtlasRegion[] boredFrames;
	private AtlasRegion[] runningFrames;
	
	//private Animation idleAnimation;
	//private MyAnimation boredAnimation;
	private Animation runningAnimation;
	
	//private float[] idleFrameTimes = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};
	//private float[] boredFrameTimes = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};

	public PlayerAnimator(Player player){
		this.player = player;
		stickmanAtlas = ResourcesManager.getInstance().getGameResource("gfx/game/characters/bandit.pack");
		stateTime = 0f;
		
		runningFrames = new AtlasRegion[RUNNING_FRAMES_COUNT];
		for(int i=0; i<RUNNING_FRAMES_COUNT; i++){
			runningFrames[i] = stickmanAtlas.findRegion("run" + i);
		}
		runningAnimation = new Animation(0.03f, runningFrames);
		/*
		idleFrames = new AtlasRegion[IDLE_FRAMES_COUNT];
		boredFrames = new AtlasRegion[BORED_FRAMES_COUNT];

		for(int i=0; i<IDLE_FRAMES_COUNT; i++){
			idleFrames[i] = stickmanAtlas.findRegion("idle" + i);
		}
		
		for(int i=0; i<BORED_FRAMES_COUNT; i++){
			boredFrames[i] = stickmanAtlas.findRegion("bored" + i);
		}
		
		idleAnimation = new Animation(0.1f, idleFrames);
		boredAnimation = new MyAnimation(boredFrameTimes, boredFrames);
		*/
	}

	/*---ANIMATIONS---*/
	public TextureRegion animate(float delta){
		if(player.getCurrentAnimationState() == PlayerAnimationState.IDLE){
			currentFrame = animateIdle(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.JUMPING){
			currentFrame = animateIdle(delta);
			//currentFrame = animateJump(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.RUNNING){
			currentFrame = animateRunning(delta);
			//currentFrame = animateRun(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.DIETOP){
			currentFrame = animateIdle(delta);
			//currentFrame = animateDieTop(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.LANDING){
			currentFrame = animateRunning(delta);
			/*currentFrame = animateLand(delta);
			if(landAnimation.isAnimationFinished(stateTime)){
				if(player.getPlayerSpeed() > 0)
					player.setCurrentAnimationState(PlayerAnimationState.RUNNING);
				else
					player.setCurrentAnimationState(PlayerAnimationState.IDLE);
			}
			*/
		}
		
		return currentFrame;
	}
	
	
	private TextureRegion animateIdle(float delta){
		stateTime += delta;
		//return runningAnimation.getKeyFrame(stateTime, true);
		return runningFrames[5];
	}
	
	
	private TextureRegion animateRunning(float delta){
		stateTime += delta;
		return runningAnimation.getKeyFrame(stateTime, true);
	}
	
	public void resetTime(){
		stateTime = 0f;
	}

}
