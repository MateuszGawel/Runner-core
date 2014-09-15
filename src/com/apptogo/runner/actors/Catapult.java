package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Catapult extends Obstacle{

	public enum CatapultAnimationState{
		STATIC, WORKING
	}
	
	private CatapultLeafs leafs;
	
	public Catapult(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/catapult.pack", "catapult", 1, 0.05f, CatapultAnimationState.STATIC);

		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleBody, "catapult");
		setVisible(false);
		setOffset(-20/PPM, -40/PPM);

		animationManager.createAnimation(new MyAnimation(0.02f, CatapultAnimationState.WORKING, animationManager.createFrames(16, "catapult"), false){
			@Override
			public void onAnimationFinished(){
				setVisible(false);
				animationManager.setCurrentAnimationState(CatapultAnimationState.STATIC);
				//currentFrame = animationManager.animate(0f);
				animate = false;
			}
		});
		leafs = new CatapultLeafs(object, world, gameWorld);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		if(getBody().getUserData().equals("catapultWorking")){
			setVisible(true);
			animate = true;
			animationManager.setCurrentAnimationState(CatapultAnimationState.WORKING); //(*)
			getBody().setUserData("catapult");
			leafs.init();
		}
	}
}
