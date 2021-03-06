package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.userdata.UserData;
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
		super(object, world, "catapult", 1, 0.05f, CatapultAnimationState.STATIC, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));

		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "catapult");
		setVisible(false);
		setOffset(-20/PPM, -40/PPM);

		animationManager.createAnimation(new MyAnimation(0.03f, CatapultAnimationState.WORKING, animationManager.createFrames(16, "catapult"), false){
			@Override
			public void onAnimationFinished(){
				CustomActionManager.getInstance().registerAction(new CustomAction(0.2f) {					
					@Override
					public void perform() {
						setVisible(false);
						animationManager.setCurrentAnimationState(CatapultAnimationState.STATIC);
						setAnimate(false);
					}
				});
				

			}
		});
		leafs = new CatapultLeafs(object, world, gameWorld);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	
		if(((UserData)getBody().getUserData()).key.equals("catapultWorking"))
		{
			setVisible(true);
			setAnimate(true);
			animationManager.setCurrentAnimationState(CatapultAnimationState.WORKING); //(*)
			getBody().setUserData( new UserData("catapult") );
			leafs.init();
		}
	}
}
