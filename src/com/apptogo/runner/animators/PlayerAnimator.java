package com.apptogo.runner.animators;

import com.apptogo.runner.actors.Enemy.PlayerState;
import com.apptogo.runner.actors.Player;
import com.apptogo.runner.actors.Player.PlayerAnimationState;
import com.apptogo.runner.handlers.ResourcesManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimator {
	
	private Player player;
	
	private final int SHEET_ROWS = 11;
	private final int SHEET_COLUMNS = 12;
	
	private TextureRegion currentFrame;
	private TextureRegion[] playerFrames;
	
	private TextureRegion[] breatheFrames;
	private TextureRegion[] blinkFrames;
	private TextureRegion[] runFrames;
	private TextureRegion[] startFrames;
	private TextureRegion[] dieTopFrames;
	private TextureRegion[] dieBottomFrames;
	private TextureRegion[] slideFrames;
	private TextureRegion[] jumpFrames;
	private TextureRegion[] landFrames;
	private TextureRegion[] standupFrames;
	
	private Animation breatheAnimation;
	private Animation blinkAnimation;
	private Animation runAnimation;
	private Animation startAnimation;
	private Animation dieTopAnimation;
	private Animation dieBottomAnimation;
	private Animation slideAnimation;
	private Animation jumpAnimation;
	private Animation landAnimation;
	private Animation standupAnimation;
	
	private boolean animationSwitchFlag = false;
	
	private float stateTime;
	
	public PlayerAnimator(Player player){
		this.player = player;
		
		Texture playerSheet = (Texture)ResourcesManager.getInstance().getGameResource("gfx/game/playerSheet.png");
		TextureRegion[][] playerRegionsTemp = TextureRegion.split(playerSheet, playerSheet.getWidth()/12, playerSheet.getHeight()/11);
		
		playerFrames = new TextureRegion[SHEET_ROWS * SHEET_COLUMNS];
        int index = 0;
        for (int i = 0; i < SHEET_ROWS; i++) {
            for (int j = 0; j < SHEET_COLUMNS; j++) {
            	playerFrames[index++] = playerRegionsTemp[i][j];
            }
        }
        
        blinkFrames = getFrames(0, 5, playerFrames);
    	breatheFrames  = getFrames(6, 23, playerFrames);
    	dieTopFrames = getFrames(24, 36, playerFrames);
    	dieBottomFrames = getFrames(37, 48, playerFrames);
    	jumpFrames = getFrames(49, 61, playerFrames);
    	landFrames = getFrames(62, 74, playerFrames);
    	runFrames = getFrames(75, 105, playerFrames);
    	slideFrames = getFrames(106, 112, playerFrames);
    	standupFrames = getFrames(113, 118, playerFrames);
    	startFrames = getFrames(119, 131, playerFrames);
    	
    	breatheAnimation = new Animation(0.1f, breatheFrames);
    	blinkAnimation = new Animation(0.02f, blinkFrames);
    	runAnimation = new Animation(0.02f, runFrames);
    	startAnimation = new Animation(0.010f, startFrames);
    	dieTopAnimation = new Animation(0.02f, dieTopFrames);
    	dieBottomAnimation = new Animation(0.02f, dieBottomFrames);
    	slideAnimation = new Animation(0.02f, slideFrames);
    	jumpAnimation = new Animation(0.02f, jumpFrames);
    	landAnimation = new Animation(0.02f, landFrames);
    	standupAnimation = new Animation(0.02f, standupFrames);
    	
    	stateTime = 0f;
	}

	private TextureRegion[] getFrames(int from, int to, TextureRegion[] frames){
		TextureRegion[] temp = new TextureRegion[to - from + 1];
		int index = 0;
		for(int i=from; i<=to; i++){
			temp[index] = frames[i];
			index++;
		}
		return temp;
	}
	
	/*---ANIMATIONS---*/
	public TextureRegion animate(float delta){
		if(player.getCurrentAnimationState() == PlayerAnimationState.IDLE){
			currentFrame = animateIdle(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.JUMPING){
			currentFrame = animateJump(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.RUNNING){
			currentFrame = animateRun(delta);
		}
		else if(player.getCurrentAnimationState() == PlayerAnimationState.DIETOP){
			currentFrame = animateDieTop(delta);
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
		
		return currentFrame;
	}
	
	private TextureRegion animateIdle(float delta){
		stateTime += delta;
		return breatheAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateJump(float delta){
		stateTime += delta;
		return jumpAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateRun(float delta){
		stateTime += delta;
		return runAnimation.getKeyFrame(stateTime, true);
	}
	
	private TextureRegion animateDieTop(float delta){
		stateTime += delta;
		return dieTopAnimation.getKeyFrame(stateTime, false);
	}
	
	private TextureRegion animateLand(float delta){
		stateTime += delta;
		return landAnimation.getKeyFrame(stateTime, false);
	}
	
	public void resetTime(){
		stateTime = 0f;
	}

}
