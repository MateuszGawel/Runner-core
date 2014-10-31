package com.apptogo.runner.actors;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Countdown extends Obstacle{

	public GameWorld world;
	protected Music music;
	private Sound countdown1;
	private Sound countdown2;
	private Sound countdown3;
	private Sound countdownGo;
	
	public enum CountdownAnimationState{
		STATIC, NORMAL
	}
	
	private int frameCounter = 0;
	public Countdown(GameWorld world){
		super("gfx/game/levels/countdown.pack", "countdown", 1, 0.05f, CountdownAnimationState.STATIC);
		this.world = world;
		countdown1 = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/countdown1.ogg");
		countdown2 = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/countdown2.ogg");
		countdown3 = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/countdown3.ogg");
		countdownGo = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/countdownGo.ogg");
		ScreensManager.getInstance().getCurrentScreen().guiStage.addActor(this);
		updatePosition = false;	
		music = ((GameScreen)ScreensManager.getInstance().getCurrentScreen()).world.music;
		animationManager.createAnimation(new MyAnimation(1f, CountdownAnimationState.NORMAL, animationManager.createFrames(4, "countdown"), false){
			@Override
			public void onAnimationFinished(){
				setVisible(false);
				animationManager.setCurrentAnimationState(CountdownAnimationState.STATIC);
				//currentFrame = animationManager.animate(0f);
				animate = false;
				//music.play();
				music.setLooping(true);
			}
			@Override
			public void additionalTaskDuringAnimation(){
				if(getFrameNumber() == frameCounter){
					Logger.log(this, "TUTAJ WSTAWIÆ JAKIŒ EFEKT");
					switch(frameCounter){
						case 0:
							countdown3.play();
							break;
						case 1:
							countdown2.play();
							break;
						case 2:
							countdown1.play();
							break;
						case 3:
							countdownGo.play();
							break;
						default:
							break;
					}
					frameCounter++;
				}
			}
		});
	}
	
	public void startCountdown(){
		animationManager.setCurrentAnimationState(CountdownAnimationState.NORMAL);
		animate = true;
	}
	
	public void act(float delta){
		super.act(delta);
        setWidth(currentFrame.getRegionWidth());
        setHeight(currentFrame.getRegionHeight());
		setPosition(Runner.SCREEN_WIDTH/2 - currentFrame.getRegionWidth()/2, Runner.SCREEN_HEIGHT/2 - currentFrame.getRegionHeight()/2);
	}
}
