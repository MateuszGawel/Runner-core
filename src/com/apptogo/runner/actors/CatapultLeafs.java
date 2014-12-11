package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class CatapultLeafs extends Obstacle{

	public enum LeafAnimationState{
		STATIC, WORKING
	}
	
	
	public CatapultLeafs(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/leaf.pack", "leaf", 1, 0.02f, LeafAnimationState.STATIC);
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "catapultLeafs");
		setOffset(-15f/PPM, -15f/PPM);
		setAnimate(false);
		setVisible(false);
		animationManager.createAnimation(new MyAnimation(0.02f, LeafAnimationState.WORKING, animationManager.createFrames(16, "leaf"), false){
			@Override
			public void onAnimationFinished(){
				setVisible(false);
				animationManager.setCurrentAnimationState(LeafAnimationState.STATIC);
				//currentFrame = animationManager.animate(0f);
				setAnimate(false);
			}
		});
	}
	
	public void init(){
		setVisible(true);
		setAnimate(true);
		animationManager.setCurrentAnimationState(LeafAnimationState.WORKING);
	}
}
