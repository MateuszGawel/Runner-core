package com.apptogo.runner.animators;

import com.apptogo.runner.actors.Player;
import com.apptogo.runner.actors.Player.PlayerAnimationState;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimator {
	
	private Player player;
	private float stateTime;

	private TextureRegion currentFrame;
	private TextureAtlas banditAtlas;
	
	/*--animations--*/	
	private final int RUN_FRAMES_COUNT = 18;
	private final int IDLE_FRAMES_COUNT = 22;
	private final int JUMP_FRAMES_COUNT = 6;
	private final int LAND_FRAMES_COUNT = 6;
	private final int FLY_FRAMES_COUNT = 33;
	private final int SLIDE_FRAMES_COUNT = 8;
	private final int BEGINSLIDE_FRAMES_COUNT = 6;
	private final int STANDUP_FRAMES_COUNT = 6;
	private final int DIEBOTTOM_FRAMES_COUNT = 9;
	private final int DIETOP_FRAMES_COUNT = 9;
	private final int CROUCH_FRAMES_COUNT = 10;
	private final int MOONWALK_FRAMES_COUNT = 30;
	
	private AtlasRegion[] runFrames;
	private AtlasRegion[] idleFrames;
	private AtlasRegion[] jumpFrames;
	private AtlasRegion[] landFrames;
	private AtlasRegion[] flyFrames;
	private AtlasRegion[] slideFrames;
	private AtlasRegion[] beginSlideFrames;
	private AtlasRegion[] standUpFrames;
	private AtlasRegion[] dieBottomFrames;
	private AtlasRegion[] dieTopFrames;
	private AtlasRegion[] crouchFrames;
	private AtlasRegion[] moonwalkFrames;

	private Animation runAnimation;
	private Animation idleAnimation;
	private Animation jumpAnimation;
	private Animation landAnimation;
	private Animation flyAnimation;
	private Animation slideAnimation;
	private Animation beginSlideAnimation;
	private Animation standUpAnimation;
	private Animation dieBottomAnimation;
	private Animation dieTopAnimation;
	private Animation crouchAnimation;
	private Animation moonwalkAnimation;

	public PlayerAnimator(Player player){Logger.log(this,  "ANIMATOR");
		this.player = player;
		banditAtlas = ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/bandit.pack");
		stateTime = 0f;
		
		idleFrames = new AtlasRegion[IDLE_FRAMES_COUNT];
		for(int i=0; i<IDLE_FRAMES_COUNT; i++){
			idleFrames[i] = banditAtlas.findRegion("idle" + i);
		}
		idleAnimation = new Animation(0.06f, idleFrames);
		
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
		
		standUpFrames = new AtlasRegion[STANDUP_FRAMES_COUNT];
		for(int i=0; i<STANDUP_FRAMES_COUNT; i++){
			standUpFrames[i] = banditAtlas.findRegion("standup" + i);
		}
		standUpAnimation = new Animation(0.03f, standUpFrames);

		dieBottomFrames = new AtlasRegion[DIEBOTTOM_FRAMES_COUNT];
		for(int i=0; i<DIEBOTTOM_FRAMES_COUNT; i++){
			dieBottomFrames[i] = banditAtlas.findRegion("diebottom" + i);
		}
		dieBottomAnimation = new Animation(0.03f, dieBottomFrames);
		
		dieTopFrames = new AtlasRegion[DIETOP_FRAMES_COUNT];
		for(int i=0; i<DIETOP_FRAMES_COUNT; i++){
			dieTopFrames[i] = banditAtlas.findRegion("dietop" + i);
		}
		dieTopAnimation = new Animation(0.03f, dieTopFrames);
		
		crouchFrames = new AtlasRegion[CROUCH_FRAMES_COUNT];
		for(int i=0; i<CROUCH_FRAMES_COUNT; i++){
			crouchFrames[i] = banditAtlas.findRegion("crouch" + i);
		}
		crouchAnimation = new Animation(0.03f, crouchFrames);
		
		moonwalkFrames = new AtlasRegion[MOONWALK_FRAMES_COUNT];
		for(int i=0; i<MOONWALK_FRAMES_COUNT; i++){
			moonwalkFrames[i] = banditAtlas.findRegion("moonwalk" + i);
		}
		moonwalkAnimation = new Animation(0.03f, moonwalkFrames);
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
		else if(player.getCurrentAnimationState() == PlayerAnimationState.DIEINGTOP){
			currentFrame = animateDieTop(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.DIEINGBOTTOM){
			currentFrame = animateDieBottom(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.CROUCHING){
			currentFrame = animateCrouch(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.MOONWALKING){
			currentFrame = animateMoonwalk(delta);
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
	
	private TextureRegion animateDieTop(float delta){
		stateTime += delta;
		return dieTopAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateDieBottom(float delta){
		stateTime += delta;
		return dieBottomAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateCrouch(float delta){
		stateTime += delta;
		return crouchAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateMoonwalk(float delta){
		stateTime += delta;
		return moonwalkAnimation.getKeyFrame(stateTime, false);
	}
	
	public void resetTime(){
		stateTime = 0f;
	}

}
