package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

public class Countdown extends Obstacle{

	public enum CountdownAnimationState{
		STATIC, NORMAL
	}
	
	private int frameCounter = 0;
	public Countdown(){
		super("gfx/game/levels/countdown.pack", "countdown", 1, 0.05f, CountdownAnimationState.STATIC);
		ScreensManager.getInstance().getCurrentScreen().guiStage.addActor(this);
		updatePosition = false;	
		
		animationManager.createAnimation(new MyAnimation(1f, CountdownAnimationState.NORMAL, animationManager.createFrames(4, "countdown"), false){
			@Override
			public void onAnimationFinished(){
				setVisible(false);
				animationManager.setCurrentAnimationState(CountdownAnimationState.STATIC);
				//currentFrame = animationManager.animate(0f);
				animate = false;
			}
			@Override
			public void additionalTaskDuringAnimation(){
				if(getFrameNumber() == frameCounter){
					Logger.log(this, "TUTAJ WSTAWIÆ JAKIŒ EFEKT");

					frameCounter++;
				}
			}
		});
	}
	
	public void startCountdown(){
		animationManager.setCurrentAnimationState(CountdownAnimationState.NORMAL);
		Sound sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/countdown3.ogg");
		sound.play();
		animate = true;
	}
	
	public void act(float delta){
		super.act(delta);
		setPosition(Runner.SCREEN_WIDTH/2/PPM - currentFrame.getRegionWidth()/PPM/2, Runner.SCREEN_HEIGHT/2/PPM - currentFrame.getRegionHeight()/PPM/2);
	}
}
