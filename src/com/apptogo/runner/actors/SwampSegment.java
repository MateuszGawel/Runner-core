package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.animation.AnimationManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SwampSegment extends Actor{

	public enum SwampAnimationState{
		ANIMATING
	}
	private TextureRegion currentFrame;
	private AnimationManager animationManager;
	
	public SwampSegment(){
		animationManager = new AnimationManager("gfx/game/levels/swamp.pack", new Random().nextFloat()*3, true);	
		animationManager.createAnimation(45, 0.05f, "swamp", SwampAnimationState.ANIMATING, true);
		animationManager.setCurrentAnimationState(SwampAnimationState.ANIMATING);
		currentFrame = animationManager.animate(0f);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	@Override
	public void act(float delta){
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        
        if(animationManager != null)
        	currentFrame = animationManager.animate(delta);
	}
	
}
