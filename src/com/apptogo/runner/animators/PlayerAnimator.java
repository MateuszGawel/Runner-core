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
	private TextureAtlas banditAtlas;
	
	/*--animations--*/

	private final int RUN_FRAMES_COUNT = 20;
	private final int IDLE_FRAMES_COUNT = 15;
	private final int JUMP_FRAMES_COUNT = 8;
	private final int LAND_FRAMES_COUNT = 15;
	private final int FLY_FRAMES_COUNT = 10;
	private final int SLIDE_FRAMES_COUNT = 10;
	private final int BEGINSLIDE_FRAMES_COUNT = 15;
	private final int GETUP_FRAMES_COUNT = 15;

	private AtlasRegion[] runFrames;
	private AtlasRegion[] idleFrames;
	private AtlasRegion[] jumpFrames;
	private AtlasRegion[] landFrames;
	private AtlasRegion[] flyFrames;
	private AtlasRegion[] slideFrames;
	private AtlasRegion[] beginSlideFrames;
	private AtlasRegion[] standUpFrames;

	private Animation runAnimation;
	private Animation idleAnimation;
	private Animation jumpAnimation;
	private Animation landAnimation;
	private Animation flyAnimation;
	private Animation slideAnimation;
	private Animation beginSlideAnimation;
	private Animation standUpAnimation;

	public PlayerAnimator(Player player){
		this.player = player;
		banditAtlas = ResourcesManager.getInstance().getGameResource("gfx/game/characters/bandit.pack");
		stateTime = 0f;
		
		idleFrames = new AtlasRegion[IDLE_FRAMES_COUNT];
		for(int i=0; i<IDLE_FRAMES_COUNT; i++){
			idleFrames[i] = banditAtlas.findRegion("idle" + i);
		}
		idleAnimation = new Animation(0.3f, idleFrames);
		
		runFrames = new AtlasRegion[RUN_FRAMES_COUNT];
		for(int i=0; i<RUN_FRAMES_COUNT; i++){
			runFrames[i] = banditAtlas.findRegion("run" + i);
		}
		runAnimation = new Animation(0.03f, runFrames);
		
		jumpFrames = new AtlasRegion[JUMP_FRAMES_COUNT];
		for(int i=0; i<JUMP_FRAMES_COUNT; i++){
			jumpFrames[i] = banditAtlas.findRegion("jump" + i);
		}
		jumpAnimation = new Animation(0.03f, jumpFrames);
		
		landFrames = new AtlasRegion[LAND_FRAMES_COUNT];
		for(int i=0; i<LAND_FRAMES_COUNT; i++){
			landFrames[i] = banditAtlas.findRegion("land" + i);
		}
		landAnimation = new Animation(0.03f, landFrames);
		
		flyFrames = new AtlasRegion[FLY_FRAMES_COUNT];
		for(int i=0; i<FLY_FRAMES_COUNT; i++){
			flyFrames[i] = banditAtlas.findRegion("fly" + i);
		}
		flyAnimation = new Animation(0.03f, flyFrames);
		
		slideFrames = new AtlasRegion[SLIDE_FRAMES_COUNT];
		for(int i=0; i<SLIDE_FRAMES_COUNT; i++){
			slideFrames[i] = banditAtlas.findRegion("slide" + i);
		}
		slideAnimation = new Animation(0.03f, slideFrames);
		
		beginSlideFrames = new AtlasRegion[BEGINSLIDE_FRAMES_COUNT];
		for(int i=0; i<BEGINSLIDE_FRAMES_COUNT; i++){
			beginSlideFrames[i] = banditAtlas.findRegion("beginslide" + i);
		}
		beginSlideAnimation = new Animation(0.03f, beginSlideFrames);
		
		standUpFrames = new AtlasRegion[GETUP_FRAMES_COUNT];
		for(int i=0; i<GETUP_FRAMES_COUNT; i++){
			standUpFrames[i] = banditAtlas.findRegion("standup" + i);
		}
		standUpAnimation = new Animation(0.03f, standUpFrames);

	}

	/*---ANIMATIONS---*/
	public TextureRegion animate(float delta){
		if(player.getCurrentAnimationState() == PlayerAnimationState.IDLE){
			currentFrame = animateIdle(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.JUMPING){
			currentFrame = animateJump(delta);
			if(jumpAnimation.isAnimationFinished(stateTime)){
					player.setCurrentAnimationState(PlayerAnimationState.FLYING);
			}
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.RUNNING){
			currentFrame = animateRun(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.BEGINSLIDING){
			currentFrame = animateBeginSlide(delta);
			if(beginSlideAnimation.isAnimationFinished(stateTime)){
				player.setCurrentAnimationState(PlayerAnimationState.SLIDING);
			}
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.LANDING){
			currentFrame = animateLand(delta);
			if(landAnimation.isAnimationFinished(stateTime)){
				if(player.getPlayerSpeed() > 0)
					player.setCurrentAnimationState(PlayerAnimationState.RUNNING);
				else
					player.setCurrentAnimationState(PlayerAnimationState.IDLE);
			}
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.FLYING){
			currentFrame = animateFly(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.SLIDING){
			currentFrame = animateSlide(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.STANDINGUP){
			currentFrame = animateStandUp(delta);
			if(standUpAnimation.isAnimationFinished(stateTime)){
				player.setCurrentAnimationState(PlayerAnimationState.RUNNING);
			}
		}
		
		return currentFrame;
	}
	
	
	private TextureRegion animateIdle(float delta){
		stateTime += delta;
		return idleAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateRun(float delta){
		stateTime += delta;
		return runAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateJump(float delta){
		stateTime += delta;
		return jumpAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateLand(float delta){
		stateTime += delta;
		return landAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateFly(float delta){
		stateTime += delta;
		return flyAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateBeginSlide(float delta){
		stateTime += delta;
		return beginSlideAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateSlide(float delta){
		stateTime += delta;
		return slideAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateStandUp(float delta){
		stateTime += delta;
		return standUpAnimation.getKeyFrame(stateTime, false);
	}
	
	public void resetTime(){
		stateTime = 0f;
	}

}
