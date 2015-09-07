package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.animation.AnimationManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SwampSegment extends Obstacle{

	public enum SwampAnimationState{
		ANIMATING
	}
	private AnimationManager animationManager;
	
	public SwampSegment(){
		animationManager = new AnimationManager("gfx/game/levels/forestAtlas.pack", new Random().nextFloat()*3, true);	
		animationManager.createAnimation(45, 0.05f, "swamp", SwampAnimationState.ANIMATING, true);
		animationManager.setCurrentAnimationState(SwampAnimationState.ANIMATING);
		currentFrame = animationManager.animate(0f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        super.draw(batch, parentAlpha);
		
		
	}
	
	@Override
	public void act(float delta){

        
        if(animationManager != null)
        	currentFrame = animationManager.animate(delta);
	}
	
}
